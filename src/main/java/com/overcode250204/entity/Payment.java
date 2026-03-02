package com.overcode250204.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "payment_table")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "amount")
    private double amount;
    @Column(name = "payment_date")
    private Date paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
