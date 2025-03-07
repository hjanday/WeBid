package com.webid.webid.service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.Bid;
import com.webid.webid.model.User;
import com.webid.webid.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    public Bid placeBid(Auction auction, User user, double amount) {
        Bid newBid = new Bid(auction, user, amount);
        return bidRepository.save(newBid);
    }

    // You can add other business logic for managing bids as needed
}
