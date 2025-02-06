package com.jichijima.todang.repository.menu;

import com.jichijima.todang.model.entity.menu.MenuOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    List<MenuOption> findByMenuId(Long menuId);
    List<MenuOption> findByMenuOptionCategoryId(Long menuOptionCategoryId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MenuOption m WHERE m.menuOptionCategoryId = :menuOptionCategoryId")
    int deleteByMenuOptionCategoryId(@Param("menuOptionCategoryId") Long menuOptionCategoryId);
}
