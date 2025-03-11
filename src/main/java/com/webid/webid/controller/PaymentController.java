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
    @PostMapping("/{userID}/{auctionID}/{shipDays}/pay")
    public ResponseEntity<Object> makePayment(@PathVariable Long userID, @PathVariable Long auctionID,
            @PathVariable int shipDays) {

        // obtain user and auction via userService and auctionService
        Optional<Auction> optionalAuction = auctionService.getAuctionById(auctionID);
        Optional<User> optionalUser = userService.getUserById(userID);

        // Check if the auction exists
        if (!optionalAuction.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Auction not found.");
        }
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found.");
        }

        // Retrieve the auction and user
        Auction auction = optionalAuction.get();
        User user = optionalUser.get();

        // verify auction belongs to user
        if (auction.getCurrentBidderID() != user.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Auction's current bidder does not belong to user");
        }

        // verify auction is over
        // if (!auction.isOver()) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("Auction is currently ongoing!");
        // }

        // verify bid type and time is allowed
        // type is FORWARD: check if endTime has passed
        // if (auction.getAuctionType().name().equals("FORWARD") &&
        // auction.getEndTime().isBefore(Instant.now())) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("Auction is not yet over!");
        // }

        // type is DUTCH: no checks needed ... payment should occur after
        // api/auctions/complete
        // create payment and add to db
        Payment payment = paymentService.createPayment(user.getId(), auction.getId(), auction.getAuctionType().name(),
                auction.getCurrentBid(), auction.isExpeditedShipping(), auction.getExpeditedShippingCost(), shipDays);

        return ResponseEntity.ok("Payment has completed");
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
