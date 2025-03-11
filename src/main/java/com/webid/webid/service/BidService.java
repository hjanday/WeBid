package com.webid.webid.service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.Bid;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.repository.BidRepository;
import com.webid.webid.repository.UserRepository;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;

    public BidService(BidRepository bidRepository, AuctionRepository auctionRepository, UserRepository userRepository) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    public Bid placeBid(Long auctionId, double amount) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        Bid bid = new Bid();
        bid.setAmount(amount);
        bid.setTimestamp(Instant.now());
        bid.setUser(user);
        bid.setAuction(auction);

        return bidRepository.save(bid);
    }

    // You can add other business logic for managing bids as needed
}
