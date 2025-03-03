package com.webid.webid.service;

import java.util.Optional;

import com.webid.webid.model.Auction;
import com.webid.webid.repository.AuctionRepository;

public class AuctionService {
	
	 private final AuctionRepository ar;
	    public AuctionService(AuctionRepository ar) {
	        this.ar = ar;
	    }
    public Optional<Auction>search(String itemName) {
        return this.ar.findAuctionByItemName(itemName);
    }
    public Auction select(Optional<Auction> items, Long id) {
    	return items.filter(auction -> auction.getID() == id).orElse(null);
        
    }
}
