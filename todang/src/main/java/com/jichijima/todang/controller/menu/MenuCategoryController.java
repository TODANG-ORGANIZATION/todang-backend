package com.jichijima.todang.controller.menu;

import com.jichijima.todang.model.dto.menu.MenuCategoryRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.menu.MenuCategory;
import com.jichijima.todang.service.menu.MenuCategoryService;
import com.jichijima.todang.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-categories")
@RequiredArgsConstructor
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    // 메뉴 카테고리 목록 조회
    @GetMapping("")
    public ResponseEntity<List<MenuCategory>> getMenuCategories(
            @RequestParam(required = false) Long restaurantId
    ){
        List<MenuCategory> menuCategories = menuCategoryService.getMenuCategories(restaurantId);
        return ResponseEntity.ok(menuCategories);
    }

    // 메뉴 카테고리 목록 추가
    @PostMapping("")
    public ResponseEntity<?> insertMenu(@RequestBody MenuCategoryRequest menuCategoryRequest){
        boolean isCreated = menuCategoryService.createMenuCategory(menuCategoryRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 메뉴 카테고리 수정
    @PutMapping("/{menuCategoryId}")
    public ResponseEntity<?> updateMenuCategory(@PathVariable Long menuCategoryId, @RequestBody MenuCategoryRequest menuCategoryRequest){
        boolean isUpdated = menuCategoryService.updateMenuCategory(menuCategoryId, menuCategoryRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴 카테고리를 찾지 못했습니다.");
    }

    // 메뉴 카테고리 삭제
    @DeleteMapping("/{menuCategoryId}")
    public ResponseEntity<?> deleteMenuCategory(@PathVariable Long menuCategoryId){
        boolean isDeleted = menuCategoryService.deleteMenuCategory(menuCategoryId);
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 메뉴 카테고리를 찾지 못했습니다.");
    }

}
