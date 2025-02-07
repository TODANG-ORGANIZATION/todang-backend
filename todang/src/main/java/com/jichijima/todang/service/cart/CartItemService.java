package com.jichijima.todang.service.cart;

import com.jichijima.todang.model.dto.cart.CartItemRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.cart.CartItem;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.repository.cart.CartItemRepository;
import com.jichijima.todang.repository.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    private final CartItemMenuOptionService cartItemMenuOptionService;

    // 장바구니 품목 조회
    public List<CartItem> getCartItems(Long userId){
        // 회원 아이디가 쿼리스트링에 존재하면 해당 회원 아이디와 일치하는 메뉴 가져오기
        if (userId != null)
            return cartItemRepository.findByUserId(userId);
        else
            return cartItemRepository.findAll();

    }

    // 장바구니 품목 추가
    public boolean createCartItem(CartItemRequest cartItemRequest){
        CartItem cartItem = new CartItem();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (cartItemRequest.getRestaurantId() != null) cartItem.setRestaurantId(cartItemRequest.getRestaurantId());
        if (cartItemRequest.getUserId() != null) cartItem.setUserId(cartItemRequest.getUserId());
        if (cartItemRequest.getMenuId() != null) cartItem.setMenuId(cartItemRequest.getMenuId());
        if (cartItemRequest.getQuantity() != null) cartItem.setQuantity(cartItemRequest.getQuantity());

        // 장바구니 품목을 저장하고 결과 확인
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return savedCartItem != null;
    }

    // 장바구니 부분 수정
    public boolean patchCartItem(Long cartItemId, CartItemRequest cartItemRequest){
        return cartItemRepository.findById(cartItemId)
                .map(cartItem -> {
                    boolean isUpdated = false;
                    if (cartItemRequest.getQuantity() != null){
                        cartItem.setQuantity(cartItemRequest.getQuantity());
                        isUpdated = true;
                    }

                    if (isUpdated){
                        cartItemRepository.save(cartItem);
                        return true;
                    }else{
                        return false;
                    }
                })
                .orElse(false);
    }

    // 장바구니 품목 삭제
    public boolean deleteCartItem(Long cartItemId){
        // 해당 장바구니 품목이 존재하면 삭제
        if (cartItemRepository.existsById(cartItemId)){
            cartItemRepository.deleteById(cartItemId);
            cartItemMenuOptionService.deleteCartItemMenuOptions(cartItemId);
            return true;
        }
        return false;
    }
}
