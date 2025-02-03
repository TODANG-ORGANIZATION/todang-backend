package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.entity.menu.MenuCategory;
import com.jichijima.todang.repository.menu.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;

    // 모든 메뉴 카테고리 조회
    public List<MenuCategory> getAllMenuCategories(){
        return menuCategoryRepository.findAll();
    }

    // 특정 카테고리 조회
    public Optional<MenuCategory> getMenuCategoryById(Long id){
        return menuCategoryRepository.findById(id);
    }

    // 카테고리 추가
    public MenuCategory createMenuCategory(MenuCategory menuCategory){
        return menuCategoryRepository.save(menuCategory);
    }

    // 카테고리 삭제
    public void deleteMenuCategory(Long id){
        menuCategoryRepository.deleteById(id);
    }
}
