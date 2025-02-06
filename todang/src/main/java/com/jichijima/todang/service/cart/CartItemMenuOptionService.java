package com.jichijima.todang.service.cart;

import com.jichijima.todang.model.dto.cart.CartItemMenuOptionRequest;
import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.entity.cart.CartItem;
import com.jichijima.todang.model.entity.cart.CartItemMenuOption;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.repository.cart.CartItemMenuOptionRepository;
import com.jichijima.todang.repository.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemMenuOptionService {
    private final CartItemMenuOptionRepository cartItemMenuOptionRepository;

    // 장바구니 품목 메뉴 옵션 목록 조회
    public List<CartItemMenuOption> getCartItemMenuOptions(Long cartItemId){
        // 장바구니 품목 아이디가 존재하면 해당 장바구니 품목 아이디와 일치하는 메뉴 옵션 가져오기
        if (cartItemId != null)
            return cartItemMenuOptionRepository.findByCartItemId(cartItemId);
        else
            return cartItemMenuOptionRepository.findAll();
    }

    // 장바구니 품목 메뉴 옵션 추가
    public boolean createCartItemMenuOption(Long CartItemId, CartItemMenuOptionRequest cartItemMenuOptionRequest){
        CartItemMenuOption cartItemMenuOption = new CartItemMenuOption();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (CartItemId != null) cartItemMenuOption.setCartItemId(CartItemId);
        if (cartItemMenuOptionRequest.getMenuOptionId() != null) cartItemMenuOption.setMenuOptionId(cartItemMenuOptionRequest.getMenuOptionId());

        CartItemMenuOption savedCartItemMenuOption = cartItemMenuOptionRepository.save(cartItemMenuOption);
        return savedCartItemMenuOption != null;
    }

    // 장바구니 품목 메뉴 옵션 삭제
    public boolean deleteCartItemMenuOptions(Long cartItemId){
        // 특정 장바구니 품목 아이디에 존재하는 장바구니 품목 메뉴 옵션 전부 삭제
        int deletedCount = cartItemMenuOptionRepository.deleteByCartItemId(cartItemId);
        return deletedCount > 0;
    }
}
