package com.overcode250204.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private CustomerDTO customer;
    private List<OrderItemDTO> orderItems;
    private PaymentDTO payment;
}
