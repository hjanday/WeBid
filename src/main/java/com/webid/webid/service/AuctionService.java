package com.webid.webid.service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

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
        return auctionRepository.save(auction);
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
            // dutch auction successful/completed
            return true;
        }
    }
}
