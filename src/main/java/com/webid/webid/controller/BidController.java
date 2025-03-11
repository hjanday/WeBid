package com.webid.webid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.Bid;
import com.webid.webid.service.AuctionService;
import com.webid.webid.service.BidService;
import com.webid.webid.service.UserService;

@RestController
@RequestMapping("/api/bid")
public class BidController {
    
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidService bidService;

    @PostMapping("/{auctionId}")
    public ResponseEntity<Object> placeBid(@PathVariable long auctionId, @RequestParam double bidAmount) {

        Bid bid = bidService.placeBid(auctionId, bidAmount);
        
        return  ResponseEntity.ok(bid);
    }  

}
