package com.webid.webid.model;

import java.time.Instant;

import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity

@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String itemName;
    @Column(nullable = false)
    private String description;

    @Column(unique = true, nullable = false)
    private long ownerID;

    @Column(nullable = true)
    private Double currentBid;

    @Column(nullable = true)
    private long currentBidderID;

    @Column(nullable = false)
    private double bidIncrement;

    @Column(nullable = false)
    private Instant startTime;
    @Column(nullable = false)
    private Instant endTime;
    @Column(nullable = false)
    private String auctionType;

    public Auction() {
    }

    private Auction(String itemName, String desc, long ownerID, Double currentBid, long currentBidderID,
            double bidIncrement, String auctionType) {
        this.itemName = itemName;
        this.description = desc;
        this.ownerID = ownerID;
        this.currentBid = currentBid;
        this.currentBidderID = currentBidderID;
        this.bidIncrement = bidIncrement;
        this.startTime = Instant.now();
        this.endTime = this.startTime.plus(24, ChronoUnit.HOURS);
        this.auctionType = auctionType;

    }

    public static Auction create(String itemName, String desc, long ownerID, Double currentBid, long currentBidderID,
            double bidIncrement, String auctionType) {
        return new Auction(itemName, desc, ownerID, currentBid, currentBidderID, bidIncrement, auctionType);
    }

    public Auction completeAuction() {
        // set auction to be completed at this time
        this.endTime = Instant.now();
        return this;
    }

}