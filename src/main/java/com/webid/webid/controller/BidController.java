package com.webid.webid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.Bid;
import com.webid.webid.model.User;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.BidService;

@RestController
@RequestMapping("/api/bid")
public class BidController {


    @Autowired
    private BidService bidService;

    @PostMapping("/{auctionId}")
    public ResponseEntity<Object> placeBid(@CurrentUser User currentUser, @PathVariable long auctionId, @RequestParam double bidAmount) {

        Bid bid = bidService.placeBid(auctionId, bidAmount, currentUser);

        if (bid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bid could not be placed ... please check bid amounts and users");
        } else {
            return ResponseEntity.ok(bid);
        }

    }

}
