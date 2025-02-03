package com.jichijima.todang.repository.menu;

import com.jichijima.todang.model.entity.menu.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    List<MenuOption> findByMenuId(Long menuId);
    List<MenuOption> findByMenuOptionCategoryId(Long menuOptionCategoryId);
}
