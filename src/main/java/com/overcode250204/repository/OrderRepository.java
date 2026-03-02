package com.overcode250204.repository;

import com.overcode250204.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    Order findOrderByCustomer_Id(String customerId);
}
