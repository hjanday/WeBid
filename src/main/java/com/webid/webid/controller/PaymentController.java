package com.webid.webid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final Map<String, String> paymentStatus = new HashMap<>();

    // Simulate a payment process
    @PostMapping("/{userID}/{auctionID}/pay")
    public ResponseEntity<Map<String, String>> makePayment(@RequestParam Long userID, @RequestParam Long auctionID,
            @RequestParam double amount) {
        String paymentId = UUID.randomUUID().toString();
        paymentStatus.put(paymentId, "Processing"); // Payment is initially "Processing"

        // obtain user and auction via userService and auctionService

        // need to obtain user information of payment
        // some logic needed here to set value to success, e.g. if name entered in card
        // details are the same as auction name

        // Simulate successful payment
        paymentStatus.put(paymentId, "Success");

        // Simulate failed payment
        paymentStatus.put(paymentId, "Denied");

        // not storing any payment ids, just keep it dynamic; stored in paymentStatus
        Map<String, String> response = new HashMap<>();
        response.put("paymentId", paymentId);
        response.put("status", "Success");

        return ResponseEntity.ok(response);
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
