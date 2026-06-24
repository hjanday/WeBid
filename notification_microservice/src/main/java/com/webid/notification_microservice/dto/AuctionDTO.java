package com.webid.notification_microservice.dto;

import lombok.Data;

@Data
public class AuctionDTO {
    private Long id;
    private String itemName;
    private Double currentBid;
    private Long currentBidderID;
} 