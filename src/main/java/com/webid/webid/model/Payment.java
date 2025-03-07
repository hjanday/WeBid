package com.webid.webid.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity

@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userID;
    @Column(unique = true, nullable = false)
    private Long auctionID;
    @Column(nullable = false)
    private String auctionType;

    @Column(nullable = false)
    private float itemPrice;
    @Column(nullable = false)
    private boolean expeditedShipping;
    @Column(nullable = false)
    private float expeditedShippingCost;
    @Column(nullable = false)
    private float totalCost = expeditedShippingCost+itemPrice;
    private int shippingDays;

    private Payment(Long userID, Long auctionID, String auctionType, float itemPrice, boolean expeditedShipping,
    float expeditedShippingCost,int shippingDays) {
        this.userID = userID;
        this.auctionID = auctionID;
        this.auctionType = auctionType;
        this.itemPrice = itemPrice;
        this.expeditedShipping = expeditedShipping;
        this.expeditedShippingCost = expeditedShippingCost;
        
        this.shippingDays = shippingDays;
        

    }

    public static Payment create(Long userID, Long auctionID, String auctionType, float itemPrice, boolean expeditedShipping,
    float expeditedShippingCost,int shippingDays) {
        return new Payment(userID, auctionID, auctionType, itemPrice, expeditedShipping, expeditedShippingCost, shippingDays);
    }

}
