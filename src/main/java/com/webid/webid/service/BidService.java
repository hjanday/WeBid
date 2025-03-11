package com.webid.webid.service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.Bid;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.repository.BidRepository;
import com.webid.webid.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;
    private AuctionService auctionService;
    private NotificationService notifService;

    public Bid placeBid(Long auctionId, double amount) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // logic to verify bid
        auctionService.verifyRequest(auction, auction.getAuctionType().name());
        auctionService.verifyNonOwner(user, auction);

        if ((auction.getCurrentBid() == null)
                || (auction.getCurrentBidderID() == 0 && amount >= auction.getCurrentBid()) // if there are no
                                                                                            // bidders, user can bid
                || (auction.getBidIncrement() == 0 ? amount > auction.getCurrentBid() // logic for bidding with bid
                                                                                      // increment
                        : amount >= (auction.getCurrentBid() + auction.getBidIncrement()))) {

            // maybe add a previous bid to see how much the bid was before this new one
            Bid bid = new Bid();

            bid.setAmount(amount);
            bid.setTimestamp(Instant.now());
            bid.setUser(user);
            bid.setAuction(auction);

            auction.setCurrentBid(amount);
            auction.setCurrentBidderID(user.getId());

            // Find previous bidders and notify all.
            List<Bid> prevBidders = bidRepository.findByAuctionId(auction.getId());
            List<User> prevUsers = prevBidders.stream()
                    .map(Bid::getUser) // Extracts the User from each Bid
                    .collect(Collectors.toList());
            System.out.println(prevBidders);
            System.out.println(prevBidders.get(0).getUser());
            System.out.println();
            System.out.println();
            System.out.println();

            for (User u : prevUsers) {

                if (u.getId().equals(auction.getCurrentBidderID())) {
                    notifService.notify(u, String.format("You have successfully %f bid on %s",
                            auction.getCurrentBid(), auction.getItemName()));
                } else {
                    notifService.notify(u, String.format("A new bid of %f has been placed on %s",
                            auction.getCurrentBid(), auction.getItemName()));
                }

            }

            return bidRepository.save(bid);
        } else {
            return null; // return a null bid if it could not be created
        }
    }

    // You can add other business logic for managing bids as needed
}
