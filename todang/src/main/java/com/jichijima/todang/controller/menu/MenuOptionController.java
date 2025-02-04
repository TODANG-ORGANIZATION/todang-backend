package com.jichijima.todang.controller.menu;

import com.jichijima.todang.model.dto.menu.MenuOptionCategoryRequest;
import com.jichijima.todang.model.dto.menu.MenuOptionRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.model.entity.menu.MenuOptionCategory;
import com.jichijima.todang.service.menu.MenuOptionCategoryService;
import com.jichijima.todang.service.menu.MenuOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-options")
@RequiredArgsConstructor
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    // 메뉴 옵션 목록 조회
    @GetMapping("")
    public ResponseEntity<List<MenuOption>> getMenuOptions(
            @RequestParam(required = false) Long menuId
    ){
        List<MenuOption> menuOptions = menuOptionService.getMenuOptions(menuId);
        return ResponseEntity.ok(menuOptions);
    }

    // 메뉴 옵션 ID로 메뉴 옵션 조회
    @GetMapping("/{menuOptionId}")
    public ResponseEntity<MenuOption> getMenuOption(@PathVariable Long menuOptionId){
        Optional<MenuOption> menuOption = menuOptionService.getMenuOptionById(menuOptionId);
        if (menuOption.isPresent())
            return ResponseEntity.ok(menuOption.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 메뉴 옵션 추가
    @PostMapping("")
    public ResponseEntity<?> insertMenuOption(@RequestBody MenuOptionRequest menuOptionRequest){
        boolean isCreated = menuOptionService.createMenuOption(menuOptionRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 메뉴 옵션 수정
    @PutMapping("/{menuOptionId}")
    public ResponseEntity<?> updateMenuOption(@PathVariable Long menuOptionId, @RequestBody MenuOptionRequest menuOptionRequest){
        boolean isUpdated = menuOptionService.updateMenuOption(menuOptionId, menuOptionRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴 옵션을 찾지 못했습니다.");
    }

    // 메뉴 옵션 삭제
    @DeleteMapping("{menuOptionId}")
    public ResponseEntity<?> deleteMenuOption(@PathVariable Long menuOptionId){
        boolean isDeleted = menuOptionService.deleteMenuOption(menuOptionId);
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 메뉴 옵션을 찾지 못했습니다.");
    }
}
