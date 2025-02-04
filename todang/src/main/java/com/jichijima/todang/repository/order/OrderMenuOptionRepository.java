package com.jichijima.todang.repository.order;

import com.jichijima.todang.model.entity.menu.MenuOption;
import com.jichijima.todang.model.entity.order.OrderMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuOptionRepository extends JpaRepository<OrderMenuOption, Long> {

}
