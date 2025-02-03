package com.jichijima.todang.service.menu;

import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.repository.menu.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;

    // 특정 메뉴의 옵션 조회
    public List<MenuOption> getOptionsByMenuId(Long menuId){
        return menuOptionRepository.findByMenuId(menuId);
    }

    // 옵션 추가
    public MenuOption createMenuOption(MenuOption menuOption){
        return menuOptionRepository.save(menuOption);
    }

    // 옵션 삭제
    public void deleteMenuOption(Long id){
        menuOptionRepository.deleteById(id);
    }
}
