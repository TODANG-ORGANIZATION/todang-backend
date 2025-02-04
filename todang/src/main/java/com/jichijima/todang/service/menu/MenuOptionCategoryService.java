package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.dto.menu.MenuOptionCategoryRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import com.jichijima.todang.repository.menu.MenuOptionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuOptionCategoryService {

    private final MenuOptionCategoryRepository menuOptionCategoryRepository;

    // 메뉴 옵션 카테고리 조회 (QueryString menuId)
    public List<MenuOptionCategory> getMenuOptionCategories(Long menuId){
        // 메뉴 아이디가 존재하면 해당 메뉴 아이디와 일치하는 메뉴 가져오기
        if (menuId != null)
            return menuOptionCategoryRepository.findByMenuId(menuId);
        else
            return menuOptionCategoryRepository.findAll();
    }

    // 새로운 메뉴 옵션 카테고리 추가
    public boolean createMenuOptionCategory(MenuOptionCategoryRequest menuOptionCategoryRequest){
        MenuOptionCategory menuOptionCategory = new MenuOptionCategory();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (menuOptionCategoryRequest.getMenuId() != null) menuOptionCategory.setMenuId(menuOptionCategoryRequest.getMenuId());
        if (menuOptionCategoryRequest.getName() != null) menuOptionCategory.setName(menuOptionCategoryRequest.getName());
        if (menuOptionCategoryRequest.getIsNecessary() != null) menuOptionCategory.setIsNecessary(menuOptionCategoryRequest.getIsNecessary());
        if (menuOptionCategoryRequest.getCntMax() != null) menuOptionCategory.setCntMax(menuOptionCategoryRequest.getCntMax());
        if (menuOptionCategoryRequest.getCntMin() != null) menuOptionCategory.setCntMin(menuOptionCategoryRequest.getCntMin());

        // 메뉴 옵션 카테고리를 저장하고 결과를 확인
        MenuOptionCategory savedMenuOptionCategory = menuOptionCategoryRepository.save(menuOptionCategory);
        return savedMenuOptionCategory != null; // 메뉴 옵션 카테고리가 저장되었으면 true, 아니면 false
    }

    // 메뉴 옵션 카테고리 수정
    public boolean updateMenuOptionCategory(Long menuOptionCategoryId, MenuOptionCategoryRequest menuOptionCategoryRequest){
        return menuOptionCategoryRepository.findById(menuOptionCategoryId)
                .map(menuOptionCategory -> {
                    menuOptionCategory.setName(menuOptionCategoryRequest.getName());
                    menuOptionCategory.setIsNecessary(menuOptionCategoryRequest.getIsNecessary());
                    menuOptionCategory.setCntMin(menuOptionCategory.getCntMin());
                    menuOptionCategory.setCntMax(menuOptionCategoryRequest.getCntMax());
                    menuOptionCategoryRepository.save(menuOptionCategory);
                    return true;
                })
                .orElse(false);
    }

    // 메뉴 옵션 카테고리 삭제
    public boolean deleteMenuOptionCategory(Long menuOptionCategoryId){
        Optional<MenuOptionCategory> menuOptionCategory = menuOptionCategoryRepository.findById(menuOptionCategoryId);
        if (menuOptionCategory.isPresent()){
            menuOptionCategoryRepository.delete(menuOptionCategory.get());
            return true;
        }else
            return false;
    }
}
