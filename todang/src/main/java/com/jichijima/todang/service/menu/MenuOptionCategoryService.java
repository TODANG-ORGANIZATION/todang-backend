package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.dto.menu.MenuOptionCategoryRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import com.jichijima.todang.repository.menu.MenuOptionCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuOptionCategoryService {

    private final MenuOptionCategoryRepository menuOptionCategoryRepository;

    private final MenuOptionService menuOptionService;

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
    @Transactional
    public boolean deleteMenuOptionCategory(Long menuOptionCategoryId){
        Optional<MenuOptionCategory> menuOptionCategory = menuOptionCategoryRepository.findById(menuOptionCategoryId);
        if (menuOptionCategory.isPresent()){
            // 메뉴 옵션 카테고리에 존재하는 메뉴 옵션 삭제
            menuOptionService.deleteMenuOptionByMenuOptionCategoryId(menuOptionCategoryId);
            // 메뉴 옵션 카테고리 삭제
            menuOptionCategoryRepository.delete(menuOptionCategory.get());
            return true;
        }else
            return false;
    }

    // 메뉴 옵션 카테고리 menuId로 삭제
    @Transactional
    public boolean deleteMenuOptionCategoryByMenuId(Long menuId){
        // 삭제될 메뉴 옵션 카테고리 Id 가져오기
        List<Long> menuOptionCategoryIds = menuOptionCategoryRepository.findMenuOptionCategoryIdsByMenuId(menuId);

        // 메뉴 옵션 카테고리와 관련된 메뉴 옵션 전부 삭제
        for (Long menuOptionCategoryId : menuOptionCategoryIds)
            menuOptionService.deleteMenuOptionByMenuOptionCategoryId(menuOptionCategoryId);

        // 특정 메뉴 아이디에 존재하는 장바구니 품목 메뉴 옵션 전부 삭제
        int deletedCount = menuOptionCategoryRepository.deleteByMenuId(menuId);

        return deletedCount > 0;
    }
}
