package com.webid.webid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webid.webid.model.Auction;
import com.webid.webid.model.Payment;
import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;
import com.webid.webid.service.AuctionService;
import com.webid.webid.service.UserService;
import com.webid.webid.service.PaymentService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final Map<String, String> paymentStatus = new HashMap<>();
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;

    // Simulate a payment process
    @PostMapping("/{auctionID}/{shipDays}/pay")
    public ResponseEntity<?> makePayment(@PathVariable Long auctionID, @PathVariable int shipDays) {
        try {
            Payment payment = paymentService.makePayment(auctionID, shipDays);
            return ResponseEntity.ok(Map.of("message", "Payment successfully processed"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    // Get a payment by ID
    @GetMapping("/{auctionId}")
    public Optional<Payment> getPaymentByAuctionId(@PathVariable Long auctionId) {
        return paymentService.getPaymentByAuctionId(auctionId);
    }

    // Check payment status
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<Map<String, String>> getPaymentStatus(@PathVariable String paymentId) {
        String status = paymentStatus.getOrDefault(paymentId, "Not Found");

        Map<String, String> response = new HashMap<>();
        response.put("paymentId", paymentId);

        response.put("status", status);

        return ResponseEntity.ok(response);
    }
}
