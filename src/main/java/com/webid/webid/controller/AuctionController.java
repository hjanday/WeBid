package com.webid.webid.controller;

import com.webid.webid.model.*;
import com.webid.webid.service.AuctionService;
import com.webid.webid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserRepository userRepository;

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

    // Get auctions by status
    // @GetMapping("/status/{status}")
    // public List<Auction> getAuctionsByStatus(@PathVariable String status) {
    // return auctionService.getAuctionsByStatus(status);
    // }

    // Create auction
    @PostMapping
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    // Delete an auction
    @DeleteMapping("/{id}")
    public void deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
    }

    // update a forwad auction
    @PutMapping("update/{id}/{userName}/{bidAmount}")
    public String putMethodName(@PathVariable Long id, @PathVariable String userName, @PathVariable Double bidAmount,
            @RequestBody Auction auction) {
        Optional<Auction> existingAuction = auctionService.getAuctionById(id);
        // check if auction exists
        if (!existingAuction.isPresent()) {
            return "Auction invalid";
        }
        Auction foundAuction = existingAuction.get();

        // Check if user exists, should be using userService Not yet implemented
        Optional<User> existingUser = userRepository.findByUsername(userName);
        if (!existingUser.isPresent()) {
            return "User invalid";
        }
        User foundUser = existingUser.get();

        if (auctionService.setNewBid(foundAuction, bidAmount, foundUser)) {
            return "New Bid has been set";
        } else {
            return "Error";
        }

    }
}