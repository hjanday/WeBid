package com.webid.webid.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.Auction;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.service.AuctionService;
@RestController
@RequestMapping("/api/Auction")
public class AuctionController {
    private final AuctionService as;
    public AuctionController(AuctionService as) {
        
        this.as = as;
    }
    @PostMapping("/Search")
    public ArrayList<Auction> findItem(String itemName){
    	try {
    		return this.as.search(itemName);
    	}catch(Exception e) {
    		return null;
    	}
    	
    }
    @PostMapping("/Select")

    public Auction selectItem(ArrayList<Auction> items, Long id) {
    	
        return this.as.select(items,id);

    
    	
    }
	
}
