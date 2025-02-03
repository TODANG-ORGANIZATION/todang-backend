package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import com.jichijima.todang.repository.menu.MenuOptionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuOptionCategoryService {

    private final MenuOptionCategoryRepository menuOptionCategoryRepository;

    // 특정 메뉴의 옵션 카테고리 조회
    public List<MenuOptionCategory> getOptionCategoriesByMenuId(Long menuId){
        return menuOptionCategoryRepository.findByMenuId(menuId);
    }

    // 옵션 카테고리 추가
    public MenuOptionCategory createMenuOptionCategory(MenuOptionCategory menuOptionCategory){
        return menuOptionCategoryRepository.save(menuOptionCategory);
    }

    // 옵션 카테고리 삭제
    public void deleteMenuOptionCategory(Long id){
        menuOptionCategoryRepository.deleteById(id);
    }
}
