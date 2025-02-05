package com.jichijima.todang.service.order;

import com.jichijima.todang.model.dto.order.OrderMenuRequest;
import com.jichijima.todang.model.dto.order.OrderRequest;
import com.jichijima.todang.model.entity.order.Order;
import com.jichijima.todang.model.entity.order.OrderMenu;
import com.jichijima.todang.repository.order.OrderMenuRepository;
import com.jichijima.todang.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderMenuService {

    private final OrderMenuRepository orderMenuRepository;

    // 주문 내역 메뉴 목록 조회
    public List<OrderMenu> getOrderMenus(Long orderId){
        return orderMenuRepository.findByOrderId(orderId);
    }

    // 주문 내역 메뉴 추가
    public Boolean createOrderMenu(OrderMenuRequest orderMenuRequest){
        OrderMenu orderMenu = new OrderMenu();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (orderMenuRequest.getOrderId() != null) orderMenu.setOrderId(orderMenuRequest.getOrderId());
        if (orderMenuRequest.getMenuId() != null) orderMenu.setMenuId(orderMenuRequest.getMenuId());

        // 주문 내역 메뉴를 저장하고 결과 확인
        OrderMenu savedOrderMenu = orderMenuRepository.save(orderMenu);
        return savedOrderMenu != null;
    }
}
