package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.repository.menu.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuOptionCategoryService menuOptionCategoryService;

    // 모든 메뉴 조회
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public List<Menu> getMenus(Long restaurantId){
        // 레스토랑 아이디가 존재하면 해당 레스토랑 아이디와 일치하는 메뉴 가져오기
        if (restaurantId != null)
            return menuRepository.findByRestaurantId(restaurantId);
        else
            return menuRepository.findAll();
    }

    // 특정 메뉴 조회 (ID 기반)
    public Optional<Menu> getMenuById(Long menuId){
        return menuRepository.findById(menuId);
    }

    // 새로운 메뉴 추가
    public boolean createMenu(MenuRequest menuRequest) {
        Menu menu = new Menu();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (menuRequest.getRestaurantId() != null) menu.setRestaurantId(menuRequest.getRestaurantId());
        if (menuRequest.getMenuCategoryId() != null) menu.setMenuCategoryId(menuRequest.getMenuCategoryId());
        if (menuRequest.getName() != null) menu.setName(menuRequest.getName());
        if (menuRequest.getPrice() != null) menu.setPrice(menuRequest.getPrice());
        if (menuRequest.getMenuPhoto() != null) menu.setMenuPhoto(menuRequest.getMenuPhoto());

        // 메뉴를 저장하고 결과 확인
        Menu savedMenu = menuRepository.save(menu);
        return savedMenu != null;  // 메뉴가 저장되었으면 true, 아니면 false
    }

    // 메뉴 수정
    public boolean updateMenu(Long menuId, MenuRequest menuRequest) {
        return menuRepository.findById(menuId)
                .map(menu -> {
                    menu.setName(menuRequest.getName());
                    menu.setPrice(menuRequest.getPrice());
                    menu.setMenuPhoto(menuRequest.getMenuPhoto());
                    menuRepository.save(menu);
                    return true;
                })
                .orElse(false);
    }

    // 메뉴 정보 부분 수정 (PATCH)
    public boolean patchMenu(Long menuId, MenuRequest menuRequest){
        return menuRepository.findById(menuId)
                .map(existingMenu -> {
                    boolean isUpdated = false;
                    if (menuRequest.getName() != null) {
                        existingMenu.setName(menuRequest.getName());
                        isUpdated = true;
                    }
                    if (menuRequest.getPrice() != null){
                        existingMenu.setPrice(menuRequest.getPrice());
                        isUpdated = true;
                    }
                    if (menuRequest.getMenuPhoto() != null){
                        existingMenu.setMenuPhoto(menuRequest.getMenuPhoto());
                        isUpdated = true;
                    }
                    if (menuRequest.getSoldout() != null){
                       existingMenu.setSoldout(menuRequest.getSoldout());
                       isUpdated = true;
                    }

                    if (isUpdated){
                        menuRepository.save(existingMenu);
                        return true;
                    }else{
                        return false;
                    }
                })
                .orElse(false); // 메뉴를 찾지 못한 경우 false 반환
    }

    // 메뉴 삭제
    @Transactional
    public boolean deleteMenu(Long menuId){
        // 해당 메뉴가 존재하면 삭제
        if (menuRepository.existsById(menuId)){
            // 메뉴에 존재하는 메뉴 옵션 카테고리 삭제
            menuOptionCategoryService.deleteMenuOptionCategoryByMenuId(menuId);
            // 메뉴 삭제
            menuRepository.deleteById(menuId);
            return true;
        }
        return false;
    }

    // 메뉴 카테고리 Id로 메뉴 삭제
    @Transactional
    public boolean deleteMenuByMenuCategoryId(Long menuCategoryId){
        // 삭제될 메뉴 Id 가져오기
        List<Long> menuIds = menuRepository.findMenuIdsByMenuCategoryId(menuCategoryId);

        // 메뉴와 관련된 메뉴 옵션 카테고리 전부 삭제
        for (Long menuId : menuIds)
            menuOptionCategoryService.deleteMenuOptionCategoryByMenuId(menuId);

        // 특정 메뉴 카테고리 아이디에 존재하는 메뉴 전부 삭제
        int deletedCount = menuRepository.deleteByMenuCategoryId(menuCategoryId);

        return deletedCount > 0;
    }
}
