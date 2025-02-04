package com.jichijima.todang.service.order;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.dto.order.OrderRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.order.Order;
import com.jichijima.todang.repository.menu.MenuRepository;
import com.jichijima.todang.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 내역 목록 조회
    public List<Order> getOrders(Long userId, Long restaurantId){
        // 들어오는 쿼리 스트링 종류에 따라서 검색하는 조건을 달리 함.
        if (restaurantId != null && userId != null)
            return orderRepository.findByUserIdAndRestaurantId(userId, restaurantId);
        else if (userId != null)
            return orderRepository.findByUserId(userId);
        else if (restaurantId != null)
            return orderRepository.findByRestaurantId(restaurantId);
        else
            return orderRepository.findAll();
    }

    // 특정 주문 내역 조회 (ID 기반)
    public Optional<Order> getOrderById(Long orderId){
        return orderRepository.findById(orderId);
    }

    // 주문 내역 추가
    public Boolean createOrder(OrderRequest orderRequest){
        Order order = new Order();

        // 요청 값이 null이 아닌 경우에만 필드 설정
        if (orderRequest.getRestaurantId() != null) order.setRestaurantId(orderRequest.getRestaurantId());
        if (orderRequest.getUserId() != null) order.setUserId(orderRequest.getUserId());
        if (orderRequest.getState() != null) order.setState(orderRequest.getState());
        if (orderRequest.getPrice() != null) order.setPrice(orderRequest.getPrice());
        if (orderRequest.getEstimatedTime() != null) order.setEstimatedTime(orderRequest.getEstimatedTime());
        if (orderRequest.getFinishDatetime() != null) order.setFinishDatetime(orderRequest.getFinishDatetime());
        if (orderRequest.getRequirement() != null) orderRequest.setRequirement(orderRequest.getRequirement());
        if (orderRequest.getTel() != null) order.setTel(orderRequest.getTel());

        // 주문 내역을 저장하고 결과 확인
        Order savedOrder = orderRepository.save(order);
        return savedOrder != null;
    }

    // 주문 내역 부분 수정
    public boolean patchOrder(Long orderId, OrderRequest orderRequest){
        return orderRepository.findById(orderId)
                .map(order -> {
                    boolean isUpdated = false;
                    if (orderRequest.getState() != null){
                        order.setState(orderRequest.getState());
                        isUpdated = true;
                    }
                    if (orderRequest.getPrice() != null){
                        order.setPrice(orderRequest.getPrice());
                        isUpdated = true;
                    }
                    if (orderRequest.getEstimatedTime() != null){
                        order.setEstimatedTime(orderRequest.getEstimatedTime());
                        isUpdated = true;
                    }
                    if (orderRequest.getOrderDatetime() != null){
                        order.setOrderDatetime(orderRequest.getOrderDatetime());
                        isUpdated = true;
                    }
                    if (orderRequest.getFinishDatetime() != null){
                        order.setFinishDatetime(orderRequest.getFinishDatetime());
                        isUpdated = true;
                    }
                    if (orderRequest.getRequirement() != null){
                        order.setRequirement(orderRequest.getRequirement());
                        isUpdated = true;
                    }
                    if (orderRequest.getTel() != null){
                        order.setTel(orderRequest.getTel());
                        isUpdated = true;
                    }

                    if (isUpdated){
                        orderRepository.save(order);
                        return true;
                    }else{
                        return false;
                    }
                })
                .orElse(false);
    }
}
