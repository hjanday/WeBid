package com.webid.webid.service;

import com.webid.webid.exceptions.ResourceAlreadyExistsException;
import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.model.Bid;
import com.webid.webid.repository.AuctionRepository;
import com.webid.webid.repository.BidRepository;
import com.webid.webid.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuctionService {

    @Autowired
    private UserRepository userRepository;
    private AuctionRepository auctionRepository;
    private BidRepository bidRepository;
    // private NotificationService notifService;

    // Get all auctions
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    // Get auction by ID
    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    public List<Auction> findAuctionByItemName(String itemName) {
        if (auctionRepository.findByItemName(itemName).isEmpty()) {
            throw new ResourceAlreadyExistsException("No auctions found");
        }

        try {
            return auctionRepository.findByItemName(itemName);
        }

        catch (DataIntegrityViolationException ex) {
            throw new ResourceAlreadyExistsException("Data Integrity Error: " + ex.getMessage());
        }

    }

    // Create a new auction
    public Auction createAuction(Auction auction, User user) throws AccessDeniedException {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // String username = authentication.getName();

        // User user = userRepository.findByEmail(username)
        //         .orElseThrow(() -> new RuntimeException("User not found"));

        auction.setOwner(user);

        return auctionRepository.save(auction);

    }

    // Save an auction
    public Auction saveAuction(Auction auction) {
        try {
            return auctionRepository.save(auction);
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceAlreadyExistsException("Data Integrity Error: " + ex.getMessage());
        }
    }

    // Delete an auction
    public void deleteAuction(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if(auction.getOwner().getId().equals(user.getId())){
            
            // User should not be able to delete an active auciton.
            
            auctionRepository.deleteById(id);
        }
        
    }

    

    // // Find auctions by status (example method)
    // public List<Auction> getAuctionsByStatus(String status) {
    // return auctionRepository.findByStatus(status);
    // }

    // Dutch Auction completed
    public boolean confirmBid(Auction foundAuction, User user) {
        if (foundAuction == null) {
            return false;
        } else { // auction is found; server processes purchase
            foundAuction.setCurrentBidderID(user.getId());
            foundAuction.completeAuction();
            // notifService.notify(user, "Your bid has been confirmed");
            auctionRepository.save(foundAuction);
            // dutch auction successful/completed
            return true;
        }
    }

    public Auction updateDutch(long auctionID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        // get auction
        Optional<Auction> existingAuction = auctionRepository.findById(auctionID);
        if (!existingAuction.isPresent()) {
            return null;
        }
        Auction auction = existingAuction.get();

        // verify dutch typing
        if (!auction.getAuctionType().name().equals("DUTCH")) {
            return null;
        }

        // only owner may update the auction
        if (auction.getOwner().getId().equals(user.getId())) {
            // Uses bid_increment to decrement values and check if auction is already at
            // lowest bid
            if (auction.getCurrentBid() > auction.getBidIncrement()
                    && auction.getCurrentBid() != auction.getLowestBid()) {
                auction.setCurrentBid(auction.getCurrentBid() - auction.getBidIncrement());
                // ON dutch auction: Check if currentbid is floor price and start timer.
                if (auction.getCurrentBid() <= auction.getLowestBid()) {
                    auction.setCurrentBid(auction.getLowestBid());
                    auction.setStartTime(Instant.now());
                    auction.setEndTime(Instant.now().plus(24, ChronoUnit.HOURS));
                }
                auctionRepository.save(auction);
                return auction;
            }
        }
        return null;
    }

    public Auction completeDutch(long auctionID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        // get auction and user for userID
        Optional<Auction> existingAuction = auctionRepository.findById(auctionID);
        if (!existingAuction.isPresent()) {
            return null;
        }
        Auction auction = existingAuction.get();

        // verify dutch typing
        if (!auction.getAuctionType().name().equals("DUTCH")) {
            return null;
        }
        // only non owner may complete the auction
        if (!auction.getOwner().getId().equals(user.getId())) {
            auction.setCurrentBidderID(user.getId());
            auction.completeAuction();
            auctionRepository.save(auction);
            return auction;
        }
        return null;
    }

    public void selectExpeditedShipping(Auction foundAuction) {
        foundAuction.setExpeditedShipping(true);

    }

    public void deselectExpeditedShipping(Auction foundAuction) {
        foundAuction.setExpeditedShipping(false);

    }

    // Verify owner
    public void verifyOwner(User user, Auction auction) {
        if (!user.getId().equals(auction.getOwner().getId())) {
            throw new IllegalArgumentException("Only the owner of the auction may change the bid.");
        }
    }

    // verify non-owner
    public void verifyNonOwner(User user, Auction auction) {
        if (user.getId().equals(auction.getOwner().getId())) {
            throw new IllegalArgumentException("The owner of the auction may not bid.");
        }
    }

    // Verify Request
    public void verifyRequest(Auction auction, String type) {

        type.toUpperCase(); // set type to all uppercase

        // Check if auction is over
        if (auction.isOver()) {
            throw new IllegalArgumentException("Auction is already over.");
        }

        // Check if auction type is correct
        if (!auction.getAuctionType().name().equals(type)) {
            throw new IllegalArgumentException("Request is for the wrong type.");
        }
    }

}
