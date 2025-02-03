package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.repository.menu.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

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
    public boolean deleteMenu(Long menuId){
        Optional<Menu> menu = menuRepository.findById(menuId);
        if (menu.isPresent()){
            menuRepository.delete(menu.get());
            return true;
        }else
            return false;
    }
}
