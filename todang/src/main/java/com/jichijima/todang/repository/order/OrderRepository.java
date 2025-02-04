package com.jichijima.todang.repository.order;

import com.jichijima.todang.model.entity.menu.Menu;
import com.jichijima.todang.model.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
