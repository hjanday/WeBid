package com.webid.webid.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;
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

    public Payment(Long userID, Long auctionID, String auctionType, double itemPrice, boolean expeditedShipping,
            double expeditedShippingCost, int shippingDays) {
        this.userID = userID;
        this.auctionID = auctionID;
        this.auctionType = auctionType;
        this.itemPrice = itemPrice;
        this.expeditedShipping = expeditedShipping;
        this.expeditedShippingCost = expeditedShippingCost;
        this.shippingDays = shippingDays;

        this.totalCost = this.itemPrice + (this.expeditedShipping ? this.expeditedShippingCost : 0);

    }

    public static Payment create(Long userID, Long auctionID, String auctionType, double itemPrice,
            boolean expeditedShipping,
            double expeditedShippingCost, int shippingDays) {
        return new Payment(userID, auctionID, auctionType, itemPrice, expeditedShipping, expeditedShippingCost,
                shippingDays);
    }

}
