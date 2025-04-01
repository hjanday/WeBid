package com.webid.webid.service;

import com.webid.webid.exceptions.ResourceAlreadyExistsException;
import com.webid.webid.model.Auction;
import com.webid.webid.model.RoleEnum;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;
    private NotificationService notifService;

    // Get all auctions
    public List<Auction> getAllAuctions(User user) {
        return auctionRepository.findAll();
    }

    // Get auction by ID
    public Optional<Auction> getAuctionById(User user, Long id) {
        return auctionRepository.findById(id);
    }

    public List<Auction> findAuctionByItemName(User user, String itemName) {
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

        auction.setOwner(user);
        auction.setStartTime(Instant.now());
        auction.setEndTime(Instant.now().plus(24, ChronoUnit.HOURS));

        // send notification to user that they have created a notification
        // notifService.notify(user, "You have successfully created the auction: " + auction.getItemName());
        return auctionRepository.save(auction);

    }

    // Delete an auction
    public void deleteAuction(Long id, User user) {

        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        if (auction.getOwner().getId().equals(user.getId()) || user.getRoles().get(0) == RoleEnum.ROLE_ADMIN) {

            // User should not be able to delete an active auciton.

            auctionRepository.deleteById(id);
        }

    }

    // Auction completed
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

    // dutch updates
    public Auction updateDutch(long auctionID, User user) {

        // get auction
        Optional<Auction> existingAuction = auctionRepository.findById(auctionID);
        if (!existingAuction.isPresent()) {
            throw new IllegalArgumentException("Auction does not exist.");
        }
        Auction auction = existingAuction.get();

        // verify dutch typing
        if (!auction.getAuctionType().name().equals("DUTCH")) {
            throw new IllegalArgumentException("Auction is not of dutch type.");
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

        // throw error message if auction couldn't be returned
        throw new IllegalArgumentException("Only the owner can edit a dutch auction");
    }

    // Dutch Completes
    public Auction completeDutch(long auctionID, User user) {

        // get auction and user for userID
        Optional<Auction> existingAuction = auctionRepository.findById(auctionID);
        if (!existingAuction.isPresent()) {
            throw new IllegalArgumentException("Auction does not exist.");
        }
        Auction auction = existingAuction.get();

        // verify dutch typing
        if (!auction.getAuctionType().name().equals("DUTCH")) {
            throw new IllegalArgumentException("Auction is not of dutch type.");
        }
        // only non owner may complete the auction
        if (!auction.getOwner().getId().equals(user.getId())) {
            auction.setCurrentBidderID(user.getId());
            auction.completeAuction();
            auctionRepository.save(auction);
            // notifService.notify(user, String.format("You have successfully purchased %s for $%.2f",
            // auction.getItemName(), auction.getCurrentBid()));
            return auction;
        } else {
            throw new IllegalArgumentException("You are the owner of the auction and cannot complete it.");
        }
    }

    public void setExpeditedShipping(long auctionID, boolean expShip, User user) {

        // get auction
        Optional<Auction> existingAuction = auctionRepository.findById(auctionID);
        if (!existingAuction.isPresent()) {
            throw new IllegalArgumentException("Auction does not exist");
        }
        Auction auction = existingAuction.get();

        // check that currentbidder is the user to be able to set expedited shipping
        if (auction.getCurrentBidderID() != (user.getId())) {
            throw new IllegalArgumentException("User did not win the auction!");
        }

        // set expeditied shipping
        auction.setExpeditedShipping(expShip);
        auctionRepository.save(auction);

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
