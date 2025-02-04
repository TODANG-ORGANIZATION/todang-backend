package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.dto.menu.MenuOptionRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.repository.menu.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;

    // 메뉴 옵션 목록 조회
    public List<MenuOption> getMenuOptions(Long menuId){
        // 메뉴 아이디가 존재하면 해당 메뉴 아이디와 일치하는 메뉴 가져오기
        if (menuId != null)
            return menuOptionRepository.findByMenuId(menuId);
        else
            return menuOptionRepository.findAll();
    }

    // 특정 메뉴 옵션 조회
    public Optional<MenuOption> getMenuOptionById(Long menuOptionId){
        return menuOptionRepository.findById(menuOptionId);
    }

    // 메뉴 옵션 추가
    public boolean createMenuOption(MenuOptionRequest menuOptionRequest){
        MenuOption menuOption = new MenuOption();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (menuOptionRequest.getMenuId() != null) menuOption.setMenuId(menuOptionRequest.getMenuId());
        if (menuOptionRequest.getMenuOptionCategoryId() != null) menuOption.setMenuOptionCategoryId(menuOptionRequest.getMenuOptionCategoryId());
        if (menuOptionRequest.getName() != null) menuOption.setName(menuOptionRequest.getName());
        if (menuOptionRequest.getPrice() != null) menuOption.setPrice(menuOptionRequest.getPrice());

        // 메뉴 옵션을 저장하고 결과 확인
        MenuOption savedMenuOption = menuOptionRepository.save(menuOption);
        return savedMenuOption != null;
    }

    // 메뉴 옵션 수정
    public boolean updateMenuOption(Long menuOptionId, MenuOptionRequest menuOptionRequest){
        return menuOptionRepository.findById(menuOptionId)
                .map(menuOption -> {
                    menuOption.setName(menuOptionRequest.getName());
                    menuOption.setPrice(menuOptionRequest.getPrice());
                    menuOptionRepository.save(menuOption);
                    return true;
                })
                .orElse(false);
    }

    // 옵션 삭제
    public boolean deleteMenuOption(Long menuOptionId){
        Optional<MenuOption> menuOption = menuOptionRepository.findById(menuOptionId);
        if (menuOption.isPresent()){
            menuOptionRepository.delete(menuOption.get());
            return true;
        }else
            return false;
    }
}
