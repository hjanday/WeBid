package com.webid.webid.service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.Bid;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.repository.BidRepository;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private AuctionService auctionService;
    private NotificationService notifService;

    public Bid placeBid(Long auctionId, double amount, User user) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // logic to verify bid
        auctionService.verifyRequest(auction, auction.getAuctionType().name());
        auctionService.verifyNonOwner(user, auction);

        // if the bid is already over, return null unless the user is the currentBidder
        if (auction.isOver() || auction.getEndTime().isBefore(Instant.now())) {
            // bid is over, check user:
            throw new IllegalArgumentException("The Bid is over, if you are the current bidder, please select to pay.");
        }

        // verify that the bidder is not the currentbidder of the auction
        if (auction.getCurrentBidderID() == user.getId()) {
            // don't allow user to change bid
            throw new IllegalArgumentException("User cannot outbid themself.");
        }

        if ((auction.getCurrentBid() == null)
                || (auction.getCurrentBidderID() == 0 && amount >= auction.getCurrentBid()) // if there are no
                                                                                            // bidders, user can bid
                || (auction.getBidIncrement() == 0 ? amount > auction.getCurrentBid() // logic for bidding with bid
                                                                                      // increment
                        : amount >= (auction.getCurrentBid() + auction.getBidIncrement()))) {

            // create and save a new bid
            Bid bid = new Bid();
            bid.setAmount(amount);
            bid.setTimestamp(Instant.now());
            bid.setUser(user);
            bid.setAuction(auction);
            bidRepository.save(bid);

            // update auction information, and save, even though JPA does this
            // automatically, for good measures
            auction.setCurrentBid(amount);
            auction.setCurrentBidderID(user.getId());
            auctionRepository.save(auction);

            // Notify the owner of the auction
            notifService.notify(auction.getOwner(), String.format("A new bid of $%.2f has been placed on %s",
                    auction.getCurrentBid(), auction.getItemName()));

            // Find previous bidders and notify all.
            List<Bid> prevBidders = bidRepository.findByAuctionId(auction.getId());
            Set<User> prevUsers = prevBidders.stream()
                    .map(Bid::getUser) // Extracts the User from each Bid
                    .collect(Collectors.toSet()); // Collects into a Set to ensure uniqueness

            for (User u : prevUsers) {
                if (u.getId().equals(auction.getCurrentBidderID())) {
                    notifService.notify(u, String.format("You have successfully placed a $%.2f bid on %s",
                            auction.getCurrentBid(), auction.getItemName()));
                } else {
                    notifService.notify(u, String.format("A new bid of $%.2f has been placed on %s",
                            auction.getCurrentBid(), auction.getItemName()));
                }

            }

            return bid;
        } else {
            if (amount <= auction.getCurrentBid()) {
                throw new IllegalArgumentException("You must enter a higher bid.");
            } else if (amount < auction.getCurrentBid() + auction.getBidIncrement()) {
                throw new IllegalArgumentException("You must enter a bid higher than the increment.");
            } else {
                throw new IllegalArgumentException("The bid could not be created ... ");
            }
        }
    }

}
