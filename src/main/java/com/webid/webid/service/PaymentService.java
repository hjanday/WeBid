package com.webid.webid.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

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
    public Payment createPayment(User currentUser, Long userID, Long itemID, String auctionType, double itemPrice,
            boolean expeditedShipping, double expeditedShippingCost, int shippingDays) {
        Payment payment = Payment.create(currentUser, itemID, auctionType, itemPrice, expeditedShipping,
                expeditedShippingCost, shippingDays);
        return paymentRepository.save(payment);
    }

    /**
     * Retrieves all Payment records.
     */
    public List<Payment> getAllPayments(User currentUser) {
        return paymentRepository.findAll();
    }

    /**
     * Retrieves a Payment record by its ID.
     */
    public Payment getPaymentById(User currentUser, Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Optional<Payment> getPaymentByAuctionId(User currentUser, long auctionId) {

        return paymentRepository.findByAuctionId(auctionId);
    }

    public Payment makePayment(User currentUser, long auctionId, int shipDays) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        // verify auction belongs to user
        if (auction.getCurrentBidderID() != currentUser.getId()) {
            return null;
        } else {

            Payment payment = Payment.create(currentUser, auction.getId(), auction.getAuctionType().name(),
                    auction.getCurrentBid(), auction.isExpeditedShipping(), auction.getExpeditedShippingCost(),
                    shipDays);
            paymentRepository.save(payment);

            // after payment is made, complete the auction
            auction.completeAuction();
            auctionRepository.save(auction);

            // notify users that payment has been confirmed and bid is over
            // Find previous bidders and notify all, for dutch auctions this would be empty
            List<Bid> prevBidders = bidRepository.findByAuctionId(auction.getId());
            Set<User> prevUsers = prevBidders.stream()
                    .map(Bid::getUser)
                    .collect(Collectors.toSet());

            // notify the owner that the item has been purchased
            notifService.notify(auction.getOwner(), String.format("Your auction %s has been purchased for $%.2f",
                    auction.getItemName(), auction.getCurrentBid()));

            // notify all previous bidders and winner (only goes into this with forward
            // auction since bid would exist)
            for (User u : prevUsers) {

                if (u.getId().equals(auction.getCurrentBidderID())) {
                    notifService.notify(u, String.format("You have successfully purchased %s with $%.2f",
                            auction.getItemName(), auction.getCurrentBid()));
                } else {
                    notifService.notify(u, String.format("Auction %s has been completed and purchased for $%.2f",
                            auction.getItemName(), auction.getCurrentBid()));
                }

            }

            return payment;
        }
    }
}
