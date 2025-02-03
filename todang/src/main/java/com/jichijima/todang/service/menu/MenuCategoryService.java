package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.dto.menu.MenuCategoryRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
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
    public List<MenuCategory> getMenuCategories(Long restaurantId){
        // 레스토랑 아이디가 존재하면 해당 레스토랑 아이디와 일치하는 메뉴 가져오기
        if (restaurantId != null)
            return menuCategoryRepository.findByRestaurantId(restaurantId);
        else
            return menuCategoryRepository.findAll();
    }

    // 특정 카테고리 조회
    public Optional<MenuCategory> getMenuCategoryById(Long id){
        return menuCategoryRepository.findById(id);
    }

    // 메뉴 카테고리 추가
    public boolean createMenuCategory(MenuCategoryRequest menuCategoryRequest){
        MenuCategory menuCategory = new MenuCategory();

        if (menuCategoryRequest.getRestaurantId() != null) menuCategory.setRestaurantId(menuCategoryRequest.getRestaurantId());
        if (menuCategoryRequest.getName() != null) menuCategory.setName(menuCategoryRequest.getName());

        MenuCategory savedMenuCategory = menuCategoryRepository.save(menuCategory);
        return savedMenuCategory != null;  // 메뉴가 저장되었으면 true, 아니면 false
    }

    // 메뉴 카테고리 수정
    public boolean updateMenuCategory(Long menuCategoryId, MenuCategoryRequest menuCategoryRequest) {
        return menuCategoryRepository.findById(menuCategoryId)
                .map(menuCategory -> {
                    menuCategory.setRestaurantId(menuCategoryRequest.getRestaurantId());
                    menuCategory.setName(menuCategoryRequest.getName());
                    menuCategoryRepository.save(menuCategory);
                    return true;
                })
                .orElse(false);
    }
    
    // 메뉴 카테고리 삭제
    public boolean deleteMenuCategory(Long menuCategoryId){
        Optional<MenuCategory> menuCategory = menuCategoryRepository.findById(menuCategoryId);
        if (menuCategory.isPresent()){
            menuCategoryRepository.delete(menuCategory.get());
            return true;
        }else
            return false;
    }

}
