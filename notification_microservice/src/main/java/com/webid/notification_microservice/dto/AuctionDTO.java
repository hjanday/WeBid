package com.webid.notification_microservice.dto;

import lombok.Data;

@Data
public class AuctionDTO {
    private Long id;
    private String itemName;
    private Long sellerId;
    private Double currentPrice;
    private Double startingPrice;
    private String status;
} 