package com.overcode250204.controller;

import com.overcode250204.dto.OrderDTO;
import com.overcode250204.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderDTO request) {
        boolean response = orderService.processOrder(request);
        return new ResponseEntity<>("Placed order is " + response, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<OrderDTO> getOrderByCustomerId(@PathVariable String customerId) {
        OrderDTO response = orderService.getOrderByCustomerId(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
