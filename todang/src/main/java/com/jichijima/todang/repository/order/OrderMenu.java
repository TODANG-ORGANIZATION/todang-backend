package com.jichijima.todang.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenu extends JpaRepository<OrderMenu, Long> {
}
