package com.webid.webid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webid.webid.model.Auction;
import com.webid.webid.repository.AuctionRepository;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Transactional
    public Auction createAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    @Transactional(readOnly = true)
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    @Transactional
    public Auction updateAuction(Long id, Auction auctionDetails) {
        if (auctionRepository.existsById(id)) {
            Auction auction = auctionRepository.findById(id).get();
            auction.setItemName(auctionDetails.getItemName());
            auction.setOwnerId(auctionDetails.getOwnerId());
            auction.setCurrentBid(auctionDetails.getCurrentBid());
            auction.setBidderId(auctionDetails.getBidderId());
            auction.setAuctionType(auctionDetails.getAuctionType());

            return auctionRepository.save(auction);
        } else {
            throw new RuntimeException("Auction not found");
        }
    }

    @Transactional
    public void deleteAuction(Long id) {
        if (auctionRepository.existsById(id)) {
            auctionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Auction not found");
        }
    }
}
