package com.jichijima.todang.repository.menu;

import com.jichijima.todang.model.entity.menu.Menu;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantId(Long restaurantId);

    // menuId로 삭제시 일괄 삭제용.
    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.menuCategoryId = :menuCategoryId")
    int deleteByMenuCategoryId(@Param("menuId") Long menuCategoryId);

    // menuId로 MenuOptionCategoryId 가져오기
    @Query("SELECT m.id FROM Menu m WHERE m.menuCategoryId = :menuCategoryId")
    List<Long> findMenuIdsByMenuCategoryId(@Param("menuCategoryId") Long menuCategoryId);
}
