package com.jichijima.todang.controller.order;

import com.jichijima.todang.model.dto.menu.MenuRequest;
import com.jichijima.todang.model.dto.order.OrderMenuRequest;
import com.jichijima.todang.model.dto.order.OrderRequest;
import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.order.Order;
import com.jichijima.todang.model.entity.order.OrderMenu;
import com.jichijima.todang.service.menu.MenuService;
import com.jichijima.todang.service.order.OrderMenuService;
import com.jichijima.todang.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMenuService orderMenuService;

    // 주문 내역 목록 조회
    @GetMapping("")
    public ResponseEntity<List<Order>> getOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long restaurantId
    ){
        List<Order> orders = orderService.getOrders(userId, restaurantId);
        return ResponseEntity.ok(orders);
    }

    // 주문 내역 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId){
        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent())
            return ResponseEntity.ok(order.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 주문 내역 메뉴 목록 조회
    @GetMapping("/{orderId}/order-menus")
    public ResponseEntity<List<OrderMenu>> getOrderMenus(@PathVariable Long orderId){
        List<OrderMenu> orderMenus = orderMenuService.getOrderMenus(orderId);
        return ResponseEntity.ok(orderMenus);
    }

    // 주문 내역 추가
    @PostMapping("")
    public ResponseEntity<?> insertOrder(@RequestBody OrderRequest orderRequest){
        boolean isCreated = orderService.createOrder(orderRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 주문 내역 메뉴 추가
    @PostMapping("/menus")
    public ResponseEntity<?> insertOrderMenus(@RequestParam OrderMenuRequest orderMenuRequest){
        boolean isCreated = orderMenuService.createOrderMenu(orderMenuRequest);
        if (isCreated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추가에 실패했습니다.");
    }

    // 주문 내역 부분 수정
    @PatchMapping("/{orderId}")
    public ResponseEntity<?> patchOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest){
        boolean isUpdated = orderService.patchOrder(orderId, orderRequest);
        if (isUpdated)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정할 주문 내역을 찾지 못했습니다.");
    }
}
