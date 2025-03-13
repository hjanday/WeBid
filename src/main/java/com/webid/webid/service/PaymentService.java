package com.webid.webid.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.Bid;
import com.webid.webid.model.Payment;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.repository.BidRepository;
import com.webid.webid.repository.PaymentRepository;
import com.webid.webid.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;
    private BidRepository bidRepository;
    private NotificationService notifService;

    /**
     * Creates a new Payment record.
     */
    public Payment createPayment(Long userID, Long itemID, String auctionType, double itemPrice,
            boolean expeditedShipping, double expeditedShippingCost, int shippingDays) {
        Payment payment = Payment.create(userID, itemID, auctionType, itemPrice, expeditedShipping,
                expeditedShippingCost, shippingDays);
        return paymentRepository.save(payment);
    }

    /**
     * Retrieves all Payment records.
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Retrieves a Payment record by its ID.
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Optional<Payment> getPaymentByAuctionId(long auctionId) {

        return paymentRepository.findByAuctionId(auctionId);
    }

    public Payment makePayment(long auctionId, int shipDays) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        // verify auction belongs to user
        if (auction.getCurrentBidderID() != user.getId()) {
            return null;
        }

        // verify auction is currently over
        // if (!auction.isOver()) {
        // return null;
        // }

        Payment payment = Payment.create(user.getId(), auction.getId(), auction.getAuctionType().name(),
                auction.getCurrentBid(), auction.isExpeditedShipping(), auction.getExpeditedShippingCost(), shipDays);
        paymentRepository.save(payment);

        // notify users that payment has been confirmed and bid is over
        // Find previous bidders and notify all.
        List<Bid> prevBidders = bidRepository.findByAuctionId(auction.getId());
        List<User> prevUsers = prevBidders.stream()
                .map(Bid::getUser) // Extracts the User from each Bid
                .collect(Collectors.toList());

        for (User u : prevUsers) {

            if (u.getId().equals(auction.getCurrentBidderID())) {
                notifService.notify(u, String.format("You have successfully purchased %s with $ %f",
                        auction.getCurrentBid(), auction.getItemName()));
            } else {
                notifService.notify(u, String.format("Auction %s has been completed and purchased for $ %f",
                        auction.getCurrentBid(), auction.getItemName()));
            }

        }

        return payment;

    }

}
