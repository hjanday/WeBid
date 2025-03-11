package com.webid.webid.controller;

import com.webid.webid.model.*;
import com.webid.webid.service.AuctionService;
import com.webid.webid.service.BidService;
import com.webid.webid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidService bidService;

    // private UserRepository userRepository;

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
    public List<Auction> getAuctionByItemName(@RequestParam("itemName") String itemName){
        return auctionService.findAuctionByItemName(itemName);
    }
    

    // Get auctions by status
    // @GetMapping("/status/{status}")
    // public List<Auction> getAuctionsByStatus(@PathVariable String status) {
    // return auctionService.getAuctionsByStatus(status);
    // }

    // Create auction
    @PostMapping("/create")
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction, Principal principal) {


        // Fetch authenticated user
        // User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // // Set the ownerID before saving
        // auction.setOwnerID(user.getId());

        Auction createdAuction = auctionService.createAuction(auction);
        return ResponseEntity.ok(createdAuction);
    }

    // Delete an auction
    @DeleteMapping("/{id}")
    public void deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
    }

    // edits a Dutch auction
    @PutMapping("dutch/{auctionId}/{userId}")
    public ResponseEntity<Object> decrementDutch(@PathVariable Long auctionId, @PathVariable Long userId) {
        // Retrieve the auction by its ID
        Optional<Auction> optionalAuction = auctionService.getAuctionById(auctionId);
        Optional<User> optionalUser = userService.getUserById(userId);

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

        // verififcation of request
        try {
            auctionService.verifyRequest(auction, "DUTCH");
            auctionService.verifyOwner(user, auction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Uses bid_increment to decrement values
        if (auction.getCurrentBid() > auction.getBidIncrement()) {
            auction.setCurrentBid(auction.getCurrentBid() - auction.getBidIncrement());
            // ON dutch auction: Check if currentbid is floor price and start timer.
            if (auction.getCurrentBid() <= auction.getLowestBid()) {
                auction.setCurrentBid(auction.getLowestBid());
                auction.setStartTime(Instant.now());
                auction.setEndTime(Instant.now().plus(24, ChronoUnit.HOURS));
            }

            auctionService.saveAuction(auction); // Save the updated auction

            return ResponseEntity.ok("Auction successfully decremented");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Current bid is at the lowest!");
        }
    }

    // places bid for forward bidding
    @PutMapping("/update/{auctionId}/{bidderID}/{bidAmount}")
    public ResponseEntity<Object> placeBid(
            @PathVariable long auctionId,
            @PathVariable long bidderID,
            @PathVariable double bidAmount) {

        // Retrieve the auction by its ID
        Optional<Auction> optionalAuction = auctionService.getAuctionById(auctionId);
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

        // verification of request
        try {
            auctionService.verifyRequest(auction, "FORWARD");
            auctionService.verifyNonOwner(user, auction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Check if the bid amount is greater than the current bid
        if ((auction.getCurrentBid() == null)
                || (auction.getCurrentBidderID() == 0 && bidAmount >= auction.getCurrentBid()) // if there are no
                                                                                               // bidders, user can bid
                || (auction.getBidIncrement() == 0 ? bidAmount > auction.getCurrentBid() // logic for bidding with bid
                                                                                         // increment
                        : bidAmount >= (auction.getCurrentBid() + auction.getBidIncrement()))) {
            // Update the auction with the new bidder and bid amount
            auction.setCurrentBid(bidAmount);
            auction.setCurrentBidderID(bidderID);

            // Create and save a new Bid record
            Bid newBid = bidService.placeBid(auction, user, bidAmount);

            // Save the updated auction
            auctionService.saveAuction(auction);

            // Return the updated auction with status 200 OK
            return ResponseEntity.ok(auction);
        } else {
            // Return an error response if the bid is not higher than the current bid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bid amount must be greater than the current bid.");
        }
    }

    // // update a forward auction
    // @PutMapping("update/{id}/{userName}/{bidAmount}")
    // public String updateForward(@PathVariable Long id, @PathVariable String
    // userName, @PathVariable Double bidAmount,
    // @RequestBody Auction auction) {
    // Optional<Auction> existingAuction = auctionService.getAuctionById(id);
    // // check if auction exists
    // if (!existingAuction.isPresent()) {
    // return "Auction invalid";
    // }
    // Auction foundAuction = existingAuction.get();

    // // Check if user exists, should be using userService Not yet implemented
    // Optional<User> existingUser = userService.getUserbyUsername(userName);
    // if (!existingUser.isPresent()) {
    // return "User invalid";
    // }
    // User foundUser = existingUser.get();

    // if (auctionService.setNewBid(foundAuction, bidAmount, foundUser)) {
    // return "New Bid has been set (Forward)";
    // } else {
    // return "Error";
    // }
    // }

    // Complete a dutch auction
    @PutMapping("complete/{auctionId}/{bidderID}")
    public ResponseEntity<Object> updateDutch(@PathVariable long auctionId, @PathVariable Long bidderID) {
        // Retrieve the auction by its ID
        Optional<Auction> optionalAuction = auctionService.getAuctionById(auctionId);
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

        // verify owner is not the bidder
        try {
            auctionService.verifyNonOwner(user, auction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // set currentbidder; since dutch is basically a 'purchase' rather than a bid
        auction.setCurrentBidderID(bidderID);

        // check if user is correctly the bidder
        if (user.getId() == auction.getCurrentBidderID()) {
            // complete the auction and save new values
            auction.completeAuction();
            auctionService.saveAuction(auction); // saves the new auction values
            return ResponseEntity.ok(auction);
        } else {
            return ResponseEntity.badRequest().body("Only the current bidder can complete the auction.");
        }

    }
}