package com.jichijima.todang.service.menu;

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
    public Menu createMenu(Menu menu){
        return menuRepository.save(menu);
    }

    // 메뉴 수정
    public boolean updateMenu(Long menuId, Menu updateMenu) {
        return menuRepository.findById(menuId)
                .map(menu -> {
                    menu.setName(updateMenu.getName());
                    menu.setPrice(updateMenu.getPrice());
                    menu.setMenuPhoto(updateMenu.getMenuPhoto());
                    menu.setSoldout(updateMenu.getSoldout());
                    menuRepository.save(menu);
                    return true;
                })
                .orElse(false);
    }

    // 메뉴 정보 부분 수정 (PATCH)
    public boolean patchMenu(Long menuId, Menu menu){
        return menuRepository.findById(menuId)
                .map(existingMenu -> {
                    boolean isUpdated = false;
                    if (menu.getName() != null) {
                        existingMenu.setName(menu.getName());
                        isUpdated = true;
                    }
                    if (menu.getPrice() != null){
                        existingMenu.setPrice(menu.getPrice());
                        isUpdated = true;
                    }
                    if (menu.getMenuPhoto() != null){
                        existingMenu.setMenuPhoto(menu.getMenuPhoto());
                        isUpdated = true;
                    }
                    if (menu.getSoldout() != null){
                       existingMenu.setSoldout(menu.getSoldout());
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
