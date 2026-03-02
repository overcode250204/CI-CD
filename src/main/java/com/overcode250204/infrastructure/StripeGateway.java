package com.overcode250204.infrastructure;

import com.overcode250204.service.IPaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class StripeGateway implements IPaymentGateway {
    @Override
    public String processPayment(double amount) {
        System.out.println("Connecting to Stripe API...");
        System.out.println("Charging: " + amount);
        return "STRIPE_TRANS_" + System.currentTimeMillis();
    }
}
