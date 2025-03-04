package com.webid.webid.service;

import java.util.ArrayList;
import java.util.Optional;

import com.webid.webid.model.Auction;
import com.webid.webid.repository.AuctionRepository;

public class AuctionService {
	
	 private final AuctionRepository ar;
	    public AuctionService(AuctionRepository ar) {
	        this.ar = ar;
	    }
    public ArrayList<Auction>search(String itemName) {
        return this.ar.findAuctionByItemName(itemName);
    }
    public Auction select(ArrayList<Auction> items, Long id) {
    	for(int i = 0; i < items.size(); i ++ ) {
    		if (items.get(i).getID()==id) {
    			return items.get(i);
    		}
    	}
    	return null;
        
    }
}
