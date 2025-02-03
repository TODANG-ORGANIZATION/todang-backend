package com.jichijima.todang.controller.menu;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 여러 쿼리 스트링을 받아서 메뉴 목록 조회
    @GetMapping("")
    public ResponseEntity<List<Menu>> getMenus(
            @RequestParam(required = false) Long restaurantId
    ){
        List<Menu> menus = menuService.getMenus(restaurantId);
        return ResponseEntity.ok(menus);
    }

    // 메뉴 ID로 단일 메뉴 조회
    @GetMapping("/{menuId}")
    public ResponseEntity<Menu> getMenu(@PathVariable Long menuId){
        Optional<Menu> menu = menuService.getMenuById(menuId);
        if (menu.isPresent())
            return ResponseEntity.ok(menu.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 새로운 메뉴 추가
    @PostMapping("")
    public ResponseEntity<?> insertMenu(@RequestBody MenuRequest menuRequest){
        Menu createdMenu = menuService.createMenu(menuRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    // 메뉴 정보 수정
    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequest menuRequest){
        boolean isUpdated = menuService.updateMenu(menuId, menuRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴를 찾지 못했습니다.");
    }

    // 메뉴 정보 일부 수정
    @PatchMapping("/{menuId}")
    public ResponseEntity<?> patchMenu(@PathVariable Long menuId, @RequestBody MenuRequest menuRequest){
        boolean isUpdated = menuService.patchMenu(menuId, menuRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴를 찾지 못했습니다.");
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId){
        boolean isDeleted = menuService.deleteMenu(menuId);
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 메뉴를 찾지 못했습니다.");
    }


}
