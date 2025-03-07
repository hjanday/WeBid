package com.webid.webid.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Instant timestamp;

    public Bid() {
        this.timestamp = Instant.now();
    }

    public Bid(Auction auction, User user, double amount) {
        this.auction = auction;
        this.user = user;
        this.amount = amount;
        this.timestamp = Instant.now();
    }

    // Getters and Setters
}
