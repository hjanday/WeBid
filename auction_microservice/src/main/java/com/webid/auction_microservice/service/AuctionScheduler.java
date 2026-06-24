package com.webid.webid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.repository.AuctionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionScheduler {
    // @Autowired
    // private  AuctionRepository auctionRepository;
    // private  NotificationService notificationService;


    // @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    // public void checkAuctionExpiry() {
    //     List<Auction> expiredAuctions = auctionRepository.findByEndTimeBeforeAndNotifiedFalse(LocalDateTime.now());

    //     for (Auction auction : expiredAuctions) {
    //         for (User u: auction.getPrevBidder()){
    //             if(!u.getId().equals(auction.getCurrentBidderID())){
    //                 notificationService.notify(u,String.format("The Auction on %s is over with a bid of %f",auction.getItemName(),auction.getCurrentBid()));
    //             }else if (u.getId().equals(auction.getCurrentBidderID())){
    //                 notificationService.notify(u,String.format("Congratulations on winning the bid on %s with a bid of %f, please proceed to check out",auction.getItemName(),auction.getCurrentBid()));

    //             }
               
    //             auction.setOver(true); 
    //             auctionRepository.save(auction);
    //         }
    //     }
    // }
}

