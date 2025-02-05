package com.jichijima.todang.service.order;

import com.jichijima.todang.model.dto.order.OrderMenuOptionRequest;
import com.jichijima.todang.model.dto.order.OrderMenuRequest;
import com.jichijima.todang.model.entity.order.OrderMenu;
import com.jichijima.todang.model.entity.order.OrderMenuOption;
import com.jichijima.todang.repository.order.OrderMenuOptionRepository;
import com.jichijima.todang.repository.order.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderMenuOptionService {
    private final OrderMenuOptionRepository orderMenuOptionRepository;

    // 주문 내역 메뉴 옵션 목록 조회
    public List<OrderMenuOption> getOrderMenuOptions(Long orderMenuId){
        return orderMenuOptionRepository.findByOrderMenuId(orderMenuId);
    }

    // 주문 내역 메뉴 추가
    public Boolean createOrderMenuOption(OrderMenuOptionRequest orderMenuOptionRequest){
        OrderMenuOption orderMenuOption = new OrderMenuOption();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (orderMenuOptionRequest.getOrderMenuId() != null) orderMenuOption.setOrderMenuId(orderMenuOptionRequest.getOrderMenuId());
        if (orderMenuOptionRequest.getMenuOptionId() != null) orderMenuOption.setMenuOptionId(orderMenuOptionRequest.getMenuOptionId());

        // 주문 내역 메뉴 옵션을 저장하고 결과 확인
        OrderMenuOption savedOrderMenuOption = orderMenuOptionRepository.save(orderMenuOption);
        return savedOrderMenuOption != null;
    }
}
