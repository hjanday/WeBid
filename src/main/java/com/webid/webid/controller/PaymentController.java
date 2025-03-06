// package com.webid.webid.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.webid.webid.model.Auction;
// import com.webid.webid.model.User;
// import com.webid.webid.repository.UserRepository;
// import com.webid.webid.service.AuctionService;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;
// import java.util.UUID;

// @RestController
// @RequestMapping("/payments")
// public class PaymentController {

// private final Map<String, String> paymentStatus = new HashMap<>();
// @Autowired
// private AuctionService auctionService;
// @Autowired
// private UserRepository userRepository;

// // Simulate a payment process
// @PostMapping("/{userID}/{auctionID}/pay")
// public ResponseEntity<Map<String, String>> makePayment(@RequestParam Long
// userID, @RequestParam Long auctionID,
// @RequestParam double amount) {
// String paymentId = UUID.randomUUID().toString();
// paymentStatus.put(paymentId, "Processing"); // Payment is initially
// "Processing"

// // obtain user and auction via userService and auctionService
// Optional<Auction> existingAuction = auctionService.getAuctionById(auctionID);
// // check if auction exists
// if (!existingAuction.isPresent()) {
// return null;
// }
// Auction foundAuction = existingAuction.get();
// Optional<User> existingUser = userRepository.findById(userID);
// if (!existingUser.isPresent()) {
// return null;
// }
// User foundUser = existingUser.get();

// // need to obtain user information of payment
// // some logic needed here to set value to success, e.g. if name entered in
// card
// // details are the same as auction name
// // not storing any payment ids, just keep it dynamic; stored in paymentStatus
// if (foundUser.getId() == foundAuction.getCurrentBidderID()) {
// // Simulate successful payment
// paymentStatus.put(paymentId, "Success");
// Map<String, String> response = new HashMap<>();
// response.put("paymentId", paymentId);
// response.put("status", "Success");

// return ResponseEntity.ok(response);
// } else {
// // Simulate failed payment
// paymentStatus.put(paymentId, "Denied");
// Map<String, String> responseDeny = new HashMap<>();
// responseDeny.put("paymentId", paymentId);
// responseDeny.put("status", "Denied");

// return ResponseEntity.ok(responseDeny);
// }

// }

// // Check payment status
// @GetMapping("/status/{paymentId}")
// public ResponseEntity<Map<String, String>> getPaymentStatus(@PathVariable
// String paymentId) {
// String status = paymentStatus.getOrDefault(paymentId, "Not Found");

// Map<String, String> response = new HashMap<>();
// response.put("paymentId", paymentId);

// response.put("status", status);

// return ResponseEntity.ok(response);
// }
// }
