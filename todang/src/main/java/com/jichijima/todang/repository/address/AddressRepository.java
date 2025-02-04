package com.jichijima.todang.repository.address;

import com.jichijima.todang.model.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userID);
    List<Address> findByUserIdAndIsPrimary(Long userId, Boolean isPrimary);
}
