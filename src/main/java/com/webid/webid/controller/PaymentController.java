package com.webid.webid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webid.webid.model.Payment;
import com.webid.webid.model.User;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.PaymentService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final Map<String, String> paymentStatus = new HashMap<>();

    @Autowired
    private PaymentService paymentService;

    // Simulate a payment process
    @PostMapping("/{auctionID}/{shipDays}/pay")
    public ResponseEntity<Object> makePayment(@CurrentUser User currentUser, @PathVariable Long auctionID,
            @PathVariable int shipDays) {

        Payment payment = paymentService.makePayment(currentUser, auctionID, shipDays);
        if (payment == null) {
            return ResponseEntity.badRequest().body("Payment could not be added for various reasons.");
        } else {
            return ResponseEntity.ok("Payment successfully processed");
        }

    }

    // Get a payment by ID
    @GetMapping("/{auctionId}")
    public Optional<Payment> getPaymentByAuctionId(@CurrentUser User currentUser, @PathVariable Long auctionId) {
        return paymentService.getPaymentByAuctionId(currentUser, auctionId);
    }

    // Check payment status
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<Map<String, String>> getPaymentStatus(@CurrentUser User currentUser,
            @PathVariable String paymentId) {
        String status = paymentStatus.getOrDefault(paymentId, "Not Found");

        Map<String, String> response = new HashMap<>();
        response.put("paymentId", paymentId);

        response.put("status", status);

        return ResponseEntity.ok(response);
    }
}
