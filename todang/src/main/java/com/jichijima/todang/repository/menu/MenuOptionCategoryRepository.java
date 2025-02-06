package com.jichijima.todang.repository.menu;

import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MenuOptionCategoryRepository extends JpaRepository<MenuOptionCategory, Long> {
    List<MenuOptionCategory> findByMenuId(Long menuId);

    // menuId로 삭제시 일괄 삭제용.
    @Modifying
    @Transactional
    @Query("DELETE FROM MenuOptionCategory m WHERE m.menuId = :menuId")
    int deleteByMenuId(@Param("menuId") Long menuId);
    
    // menuId로 MenuOptionCategoryId 가져오기
    @Query("SELECT m.id FROM MenuOptionCategory m WHERE m.menuId = :menuId")
    List<Long> findMenuOptionCategoryIdsByMenuId(@Param("menuId") Long menuId);
}
