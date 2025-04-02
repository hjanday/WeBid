package com.webid.webid.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(unique = true, nullable = false)
    private Long auctionID;
    @Column(nullable = false)
    private String auctionType;

    @Column(nullable = false)
    private double itemPrice;
    @Column(nullable = false)
    private boolean expeditedShipping;
    @Column(nullable = false)
    private double expeditedShippingCost;
    @Column(nullable = false)
    private double totalCost;
    private int shippingDays;

    public Payment() {
    }

    private Payment(User user, Long auctionID, String auctionType, double itemPrice, boolean expeditedShipping,
            double expeditedShippingCost, int shippingDays) {
        this.user = user;
        this.auctionID = auctionID;
        this.auctionType = auctionType;
        this.itemPrice = itemPrice;
        this.expeditedShipping = expeditedShipping;
        this.expeditedShippingCost = expeditedShippingCost;
        this.shippingDays = shippingDays;

        this.totalCost = this.itemPrice + (this.expeditedShipping ? this.expeditedShippingCost : 0);

    }

    public static Payment create(User user, Long auctionID, String auctionType, double itemPrice,
            boolean expeditedShipping,
            double expeditedShippingCost, int shippingDays) {
        return new Payment(user, auctionID, auctionType, itemPrice, expeditedShipping, expeditedShippingCost,
                shippingDays);
    }

}
