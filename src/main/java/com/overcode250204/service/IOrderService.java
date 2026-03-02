package com.overcode250204.service;

import com.overcode250204.dto.OrderDTO;
import com.overcode250204.entity.Order;

public interface IOrderService {
    boolean processOrder(OrderDTO order);

    OrderDTO getOrderByCustomerId(String customerId);
}
