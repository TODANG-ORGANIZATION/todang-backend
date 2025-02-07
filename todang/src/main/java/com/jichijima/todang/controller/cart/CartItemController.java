package com.jichijima.todang.controller.cart;

import com.jichijima.todang.model.dto.cart.CartItemMenuOptionRequest;
import com.jichijima.todang.model.dto.cart.CartItemRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.cart.CartItem;
import com.jichijima.todang.model.entity.cart.CartItemMenuOption;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.service.cart.CartItemMenuOptionService;
import com.jichijima.todang.service.cart.CartItemService;
import com.jichijima.todang.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartItemMenuOptionService cartItemMenuOptionService;

    // 장바구니 목록 조회
    @GetMapping("")
    public ResponseEntity<List<CartItem>> getCartItems(
            @RequestParam(required = false) Long userId
    ){
        List<CartItem> cartItems = cartItemService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    // 장바구니 품목 메뉴 옵션 조회
    @GetMapping("/{cartItemId}/menu-options")
    public ResponseEntity<List<CartItemMenuOption>> getCartItemMenuOptions(@PathVariable Long cartItemId){
        List<CartItemMenuOption> cartItemMenuOptions = cartItemMenuOptionService.getCartItemMenuOptions(cartItemId);
        return ResponseEntity.ok(cartItemMenuOptions);
    }

    // 장바구니 품목 추가
    @PostMapping("")
    public ResponseEntity<?> insertCartItem(@RequestBody CartItemRequest cartItemRequest){
        boolean isCreated = cartItemService.createCartItem(cartItemRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 장바구니 품목 메뉴 옵션 추가
    @PostMapping("/{cartItemId}/menu-options")
    public ResponseEntity<?> insertCartItemMenuOption(@PathVariable Long cartItemId, @RequestBody CartItemMenuOptionRequest cartItemMenuOptionRequest){
        boolean isCreated = cartItemMenuOptionService.createCartItemMenuOption(cartItemId, cartItemMenuOptionRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 장바구니 품목 수정
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItemRequest cartItemRequest){
        boolean isUpdated = cartItemService.patchCartItem(cartItemId, cartItemRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 메뉴를 찾지 못했습니다.");
    }

    // 장바구니 품목 삭제
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId){
        boolean isDeleted = cartItemService.deleteCartItem(cartItemId);
        // 삭제된 것이 있으면 장바구니 품목 메뉴 옵션도 삭제
        if (isDeleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 메뉴를 찾지 못했습니다.");
    }
}
