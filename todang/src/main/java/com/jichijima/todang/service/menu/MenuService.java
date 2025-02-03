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

    // 특정 메뉴 조회 (ID 기반)
    public Optional<Menu> getMenuById(Long id){
        return menuRepository.findById(id);
    }

    // 새로운 메뉴 추가
    public Menu createMenu(Menu menu){
        return menuRepository.save(menu);
    }

    // 메뉴 수정
    public Menu updateMenu(Long id, Menu updateMenu){
        return menuRepository.findById(id)
                .map(menu -> {
                    menu.setName(updateMenu.getName());
                    menu.setPrice(updateMenu.getPrice());
                    menu.setMenuPhoto(updateMenu.getMenuPhoto());
                    menu.setSoldout(updateMenu.getSoldout());
                    return menuRepository.save(menu);
                })
                .orElseThrow(() -> new EntityNotFoundException("Menu not found with id " + id));
    }

    // 메뉴 삭제
    public void deleteMenu(Long id){
        menuRepository.deleteById(id);
    }
}
