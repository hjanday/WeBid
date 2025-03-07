package com.webid.webid.controller;

import com.webid.webid.model.*;
import com.webid.webid.service.AuctionService;
import com.webid.webid.service.BidService;
import com.webid.webid.service.UserService;
import com.webid.webid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidService bidService;

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

    @PutMapping("/{auctionId}/{bidderID}/{bidAmount}")
    public ResponseEntity<Object> placeBid(
            @PathVariable long auctionId,
            @PathVariable long bidderID,
            @PathVariable double bidAmount) {

        // Retrieve the auction by its ID
        Optional<Auction> optionalAuction = auctionService.getAuctionById(auctionId);
        System.out.println(optionalAuction);
        Optional<User> optionalUser = userService.getUserById(bidderID);

        // Check if the auction exists
        if (!optionalAuction.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Auction not found.");
        }
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found.");
        }

        // Retrieve the auction and user
        Auction auction = optionalAuction.get();
        User user = optionalUser.get();

        // Check if the bid amount is greater than the current bid
        if (auction.getCurrentBid().equals(null) || bidAmount > auction.getCurrentBid()) {
            // Update the auction with the new bidder and bid amount
            auction.setCurrentBid(bidAmount);
            auction.setCurrentBidderID(bidderID);

            // Create and save a new Bid record
            Bid newBid = bidService.placeBid(auction, user, bidAmount);

            // Save the updated auction
            auctionService.createAuction(auction);

            // Return the updated auction with status 200 OK
            return ResponseEntity.ok(auction);
        } else {
            // Return an error response if the bid is not higher than the current bid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bid amount must be greater than the current bid.");
        }
    }

    // update a forward auction
    @PutMapping("update/{id}/{userName}/{bidAmount}")
    public String updateForward(@PathVariable Long id, @PathVariable String userName, @PathVariable Double bidAmount,
            @RequestBody Auction auction) {
        Optional<Auction> existingAuction = auctionService.getAuctionById(id);
        // check if auction exists
        if (!existingAuction.isPresent()) {
            return "Auction invalid";
        }
        Auction foundAuction = existingAuction.get();

        // Check if user exists, should be using userService Not yet implemented
        Optional<User> existingUser = userService.getUserbyUsername(userName);
        if (!existingUser.isPresent()) {
            return "User invalid";
        }
        User foundUser = existingUser.get();

        if (auctionService.setNewBid(foundAuction, bidAmount, foundUser)) {
            return "New Bid has been set (Forward)";
        } else {
            return "Error";
        }
    }

    // update a dutch auction
    @PutMapping("complete/{id}/{userName}")
    public String updateDutch(@PathVariable Long id, @PathVariable String userName, @RequestBody Auction auction) {
        Optional<Auction> existingAuction = auctionService.getAuctionById(id);
        // check if auction exists
        if (!existingAuction.isPresent()) {
            return "Auction invalid";
        }
        Auction foundAuction = existingAuction.get();

        // Check if user exists, should be using userService Not yet implemented
        Optional<User> existingUser = userService.getUserbyUsername(userName);
        if (!existingUser.isPresent()) {
            return "User invalid";
        }
        User foundUser = existingUser.get();

        if (auctionService.confirmBid(foundAuction, foundUser)) {
            return "Bid has been confirmed (Dutch)";
        } else {
            return "Error";
        }
    }
}