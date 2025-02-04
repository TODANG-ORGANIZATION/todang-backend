package com.jichijima.todang.repository.order;

import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.model.entity.order.Order;
import com.jichijima.todang.model.entity.order.OrderMenu;
import com.jichijima.todang.model.entity.order.OrderMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuOptionRepository extends JpaRepository<OrderMenuOption, Long> {

}
