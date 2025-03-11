package com.webid.webid.model;

import java.time.Instant;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity

@Table(name = "auctions")
public class Auction {

    // Define enum for auction type
    public enum AuctionType {
        DUTCH,
        FORWARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String itemName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private long ownerID;
    @Column(nullable = false)
    private double lowestBid;
    @Column(nullable = true)
    private Double currentBid;
    @Column(nullable = true)
    private long currentBidderID;
    @Column(nullable = false)
    private double bidIncrement;
    @Column(nullable = true)
    private Instant startTime;
    @Column(nullable = true)
    private Instant endTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionType auctionType;
    @Column(nullable = true)
    private ArrayList<User> prevBidder;
    @Column(nullable = false)
    private boolean over = false;
    @Column(nullable = false)
    private float expeditedShippingCost;
    @Column(nullable = false)
    private boolean expeditedShipping = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Auction() {
        // // only set start and endtime immediately on Forward bid; otherwise on dutch,
        // // set timer when floor (lowest) price is reached.
        // if (this.getAuctionType().name().equals("FORWARD")) {
        // this.startTime = Instant.now();
        // this.endTime = this.startTime.plus(24, ChronoUnit.HOURS);
        // }
    }

    public Auction completeAuction() {
        // set auction to be completed at this time
        this.endTime = Instant.now();
        this.over = true;
        return this;
    }
}