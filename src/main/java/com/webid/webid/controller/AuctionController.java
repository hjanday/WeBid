package com.webid.webid.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;

import com.webid.webid.model.Auction;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.service.AuctionService;

public class AuctionController {
    private final AuctionService as;
    public AuctionController(AuctionService as) {
        
        this.as = as;
    }
    public Optional<Auction> findItem(String itemName){
    	try {
    		return this.as.search(itemName);
    	}catch(Exception e) {
    		return Optional.empty();
    	}
    	
    }
    public Auction selectItem(Optional<Auction> items, Long id) {
    	
        return this.as.selectItem(items,id);

    
    	
    }
	
}
