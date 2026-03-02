package com.overcode250204.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Date;

@Entity(name = "order_table")
@Getter
@Setter
public class Order {

    @Id
    private String id;

    @Column(name = "create_date")
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public double calculateTotal() {
        if (items == null) return 0;
        return items.stream().mapToDouble(OrderItem::getSubTotal).sum();
    }
}