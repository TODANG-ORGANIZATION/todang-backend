package com.jichijima.todang.repository.menu;

import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MenuOptionCategoryRepository extends JpaRepository<MenuOptionCategory, Long> {
    List<MenuOptionCategory> findByMenuId(Long menuId);
}
