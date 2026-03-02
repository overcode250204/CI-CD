package com.overcode250204.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaymentDTO {
    private String transactionId;
    private double amount;
    private Date paymentDate;
}
