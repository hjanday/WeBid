package com.webid.webid.controller;

import java.util.ArrayList;
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

    public ArrayList<Auction> findItem(String itemName) {
        try {
            return this.as.search(itemName);
        } catch (Exception e) {
            return null;
        }

    }

    public Auction selectItem(ArrayList<Auction> items, Long id) {

        return this.as.select(items, id);

    }

}
