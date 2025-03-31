package com.webid.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webid.paymentservice.model.Payment;
import com.webid.paymentservice.service.PaymentService;

@RestController
@RequestMapping("api/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/makePayment")
    public Payment doPayment(@RequestBody Payment payment) throws JsonProcessingException {
        
        return paymentService.doPayment(payment);

    }
}
