package com.webid.webid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webid.webid.model.Auction;
import com.webid.webid.service.AuctionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    // Create a new auction
    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction) {
        Auction savedAuction = auctionService.createAuction(auction);
        return new ResponseEntity<>(savedAuction, HttpStatus.CREATED);
    }

    // Get all auctions
    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    // Get a specific auction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        Optional<Auction> auction = auctionService.getAuctionById(id);
        return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an auction
    @PutMapping("/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable Long id, @RequestBody Auction auctionDetails) {
        try {
            Auction updatedAuction = auctionService.updateAuction(id, auctionDetails);
            return ResponseEntity.ok(updatedAuction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an auction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        try {
            auctionService.deleteAuction(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
