package com.webid.webid.service;

import com.webid.webid.exceptions.ResourceAlreadyExistsException;
import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;
    private NotificationService notifService;

    // Get all auctions
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    // Get auction by ID
    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    // Create a new auction
    public Auction createAuction(Auction auction) {
        // Check if an auction with the same item name exists already and throw exception that the itemName already exists
        if(auctionRepository.findByItemName(auction.getItemName()).isPresent()){
            throw new ResourceAlreadyExistsException("An auction with item name " + auction.getItemName() + " already exists.");
        }
        try{
        return auctionRepository.save(auction);
        }
        catch (DataIntegrityViolationException ex){
			throw new ResourceAlreadyExistsException("Data Integrity Error: " + ex.getMessage());
		}

    }

    // Delete an auction
    public void deleteAuction(Long id) {
        auctionRepository.deleteById(id);
    }

    // // Find auctions by status (example method)
    // public List<Auction> getAuctionsByStatus(String status) {
    // return auctionRepository.findByStatus(status);
    // }

    // Change Forward auction
    public boolean setNewBid(Auction foundAuction, double bidAmount, User user) {
        // find auction first; if item is not found immediately already returns false
        if (foundAuction == null) {
            return false;
        } else { // auction is real; server processes bid amount and new current bidder
            if (bidAmount > foundAuction.getCurrentBid()) {
                foundAuction.setCurrentBid(bidAmount);
                foundAuction.setCurrentBidderID(user.getId());
                if (!foundAuction.getPrevBidder().contains(user)) {
                    ArrayList<User> temp = foundAuction.getPrevBidder();
                    temp.add(user);

                    foundAuction.setPrevBidder(temp);

                }
                for (User i : foundAuction.getPrevBidder()) {
                    if (!i.getId().equals(user.getId())) {
                        notifService.notify(i,
                                String.format("A new bid has been placed on %s the new current bid is %f",
                                        foundAuction.getItemName(), foundAuction.getCurrentBid()));

                    } else {
                        notifService.notify(i, "Your bid has been confirmed");

                    }
                }
                auctionRepository.save(foundAuction);

                // notifies subscribed users

                return true;
            } else {
                // return bid too small exception
                return false; // for now just return false
            }
        }
    }

    // Dutch Auction completed
    public boolean confirmBid(Auction foundAuction, User user) {
        if (foundAuction == null) {
            return false;
        } else { // auction is found; server processes purchase
            foundAuction.setCurrentBidderID(user.getId());
            foundAuction.completeAuction();
            notifService.notify(user, "Your bid has been confirmed");
            auctionRepository.save(foundAuction);
            // dutch auction successful/completed
            return true;
        }
    }

    public void selectExpeditedShipping(Auction foundAuction) {
        foundAuction.setExpeditedShipping(true);

    }

    public void deselectExpeditedShipping(Auction foundAuction) {
        foundAuction.setExpeditedShipping(false);

    }
}
