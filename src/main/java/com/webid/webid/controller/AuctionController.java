package com.webid.webid.controller;

import com.webid.webid.model.*;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    // Create auction
    @PostMapping("/create")
    public ResponseEntity<Auction> createAuction(@CurrentUser User currentUser, @RequestBody Auction auction)
            throws AccessDeniedException {
        Auction createdAuction = auctionService.createAuction(auction, currentUser);

        return ResponseEntity.ok(createdAuction);
    }

    // Delete an auction
    @DeleteMapping("/{id}")
    public void deleteAuction(@CurrentUser User currentUser, @PathVariable Long id) {
        auctionService.deleteAuction(id, currentUser);
    }

    // Get all auctions
    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    // Get an auction by ID
    @GetMapping("/{id}")
    public Optional<Auction> getAuctionById(@PathVariable Long id) {
        return auctionService.getAuctionById(id);
    }

    // Get auctions by item name queries
    @GetMapping("/search")
    public List<Auction> getAuctionByItemName(@RequestParam String itemName) {
        return auctionService.findAuctionByItemName(itemName);
    }

    // Edits the expedited shipping of an auction to be true
    @PutMapping("{auctionID}")
    public ResponseEntity<Object> setExpeditedShipping(@CurrentUser User currentUser, @PathVariable Long auctionID,
            @RequestParam boolean expShip) {
        auctionService.setExpeditedShipping(auctionID, expShip, currentUser);
        return ResponseEntity.ok().body("Expedited shipping has been set to: " + expShip);
    }

    // edits a Dutch auction
    @PutMapping("dutch/{auctionId}")
    public ResponseEntity<Object> decrementDutch(@CurrentUser User currentUser, @PathVariable Long auctionId) {
        // Retrieve the auction by its ID
        Auction auction = auctionService.updateDutch(auctionId, currentUser);
        if (auction == null) {
            return ResponseEntity.badRequest().body("Dutch auction cannot be updated");
        } else {
            return ResponseEntity.ok("Dutch auction successfully decremented");
        }
    }

    // Complete a dutch auction
    @PutMapping("complete/{auctionId}")
    public ResponseEntity<Object> updateDutch(@CurrentUser User currentUser, @PathVariable long auctionId) {
        Auction auction = auctionService.completeDutch(auctionId, currentUser);
        if (auction == null) {
            return ResponseEntity.badRequest().body("Dutch auction cannot be completed");
        } else {
            return ResponseEntity.ok("Dutch auction successfully completed");
        }

    }
}