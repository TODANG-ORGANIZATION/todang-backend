package com.jichijima.todang.controller.menu;

import com.jichijima.todang.model.dto.menu.MenuCategoryRequest;
import com.jichijima.todang.model.dto.menu.MenuOptionCategoryRequest;
import com.jichijima.todang.model.entity.menu.MenuCategory;
import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import com.jichijima.todang.service.menu.MenuCategoryService;
import com.jichijima.todang.service.menu.MenuOptionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-option-categories")
@RequiredArgsConstructor
public class MenuOptionCategoryController {

    private final MenuOptionCategoryService menuOptionCategoryService;

    // 메뉴 옵션 카테고리 목록 조회
    @GetMapping("")
    public ResponseEntity<List<MenuOptionCategory>> getMenuOptionCategories(
            @RequestParam(required = false) Long menuId
    ){
        List<MenuOptionCategory> menuOptionCategories = menuOptionCategoryService.getMenuOptionCategories(menuId);
        return ResponseEntity.ok(menuOptionCategories);
    }

    // 메뉴 옵션 카테고리 추가
    @PostMapping("")
    public ResponseEntity<?> insertMenuOptionCategory(@RequestBody MenuOptionCategoryRequest menuOptionCategoryRequest){
        boolean isCreated = menuOptionCategoryService.createMenuOptionCategory(menuOptionCategoryRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 메뉴 옵션 카테고리 수정
    @PutMapping("/{menuOptionCategoryId}")
    public ResponseEntity<?> updateMenuOptionCategory(@PathVariable Long menuOptionCategoryId, @RequestBody MenuOptionCategoryRequest menuOptionCategoryRequest){
        boolean isUpdated = menuOptionCategoryService.updateMenuOptionCategory(menuOptionCategoryId, menuOptionCategoryRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴 옵션 카테고리를 찾지 못했습니다.");
    }

    // 메뉴 옵션 카테고리 삭제
    @DeleteMapping("/{menuOptionCategoryId}")
    public ResponseEntity<?> deleteMenuOptionCategory(@PathVariable Long menuOptionCategoryId){
        boolean isDeleted = menuOptionCategoryService.deleteMenuOptionCategory(menuOptionCategoryId);
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 메뉴 옵션 카테고리를 찾지 못했습니다.");
    }
}
