package com.webid.webid.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;

import com.webid.webid.model.Auction;
import com.webid.webid.repository.AuctionRepository;

public class AuctionController {
    private final AuctionRepository ar;
    public AuctionController(AuctionRepository ar) {
        this.ar = ar;
    }
    @GetMapping
    public Optional<Auction> search(String term) {
        return this.ar.query(term);
    }
	 
}
