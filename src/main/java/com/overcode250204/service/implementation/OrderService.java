package com.overcode250204.service.implementation;

import com.overcode250204.dto.CustomerDTO;
import com.overcode250204.dto.OrderDTO;
import com.overcode250204.dto.OrderItemDTO;
import com.overcode250204.dto.PaymentDTO;
import com.overcode250204.entity.*;
import com.overcode250204.repository.CustomerRepository;
import com.overcode250204.repository.OrderRepository;
import com.overcode250204.repository.ProductRepository;
import com.overcode250204.service.IOrderService;
import com.overcode250204.service.IPaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {

    private final IPaymentGateway paymentGateway;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    @Override
    public boolean processOrder(OrderDTO request) {
        boolean result = false;
        try {
            Customer customer = customerRepository.findById(request.getCustomer().getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found!"));

            Order order = new Order();
            order.setId(UUID.randomUUID().toString());
            order.setCustomer(customer);
            order.setCreatedDate(new Date());
            order.setStatus(OrderStatus.NEW);
            order.setItems(new ArrayList<>());

            for (OrderItemDTO item : request.getOrderItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPriceAtPurchase(product.getPrice());
                orderItem.setOrder(order);
                order.getItems().add(orderItem);
            }

            double totalAmount = order.calculateTotal();
            System.out.println("Total Amount Calculated: " + totalAmount);

            String transactionId = paymentGateway.processPayment(totalAmount);

            Payment payment = new Payment();
            payment.setAmount(totalAmount);
            payment.setTransactionId(transactionId);
            payment.setPaymentDate(new Date());
            payment.setStatus(PaymentStatus.SUCCESS);

            order.setPayment(payment);
            order.setStatus(OrderStatus.PAID);

            orderRepository.save(order);
            result = true;
        } catch (Exception e) {
            log.error("Error At OrderService (processOrder) : {}", e.getMessage());
            return result;
        }
        return result;
    }

    @Override
    public OrderDTO getOrderByCustomerId(String customerId) {
        OrderDTO dto = null;
        try {
             dto = mapToDTO(orderRepository.findOrderByCustomer_Id(customerId));
        } catch (Exception e) {
            log.error("Error At OrderService : {}", e.getMessage());
            return dto;
        }
        return dto;
    }

    private OrderDTO mapToDTO(Order order) {
        if (order == null) return null;
        OrderDTO dto = new OrderDTO();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(order.getCustomer().getId());
        dto.setCustomer(customerDTO);
        List<OrderItemDTO> items = order.getItems().stream()
                .map(this::toItemDTO)
                .toList();
        dto.setOrderItems(items);
        dto.setPayment(toPaymentDTO(order.getPayment()));

        return dto;

    }
    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    private PaymentDTO toPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(payment.getAmount());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }

}
