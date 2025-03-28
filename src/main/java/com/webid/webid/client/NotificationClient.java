package com.webid.webid.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Component
public class NotificationClient {
    private final RestTemplate restTemplate;
    
    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyUser(Long userId, String message) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(message, headers);
        
        restTemplate.postForObject(
            notificationServiceUrl + "/api/notification/{userId}",
            request,
            String.class,
            userId
        );
    }

    public void notifyAuctionEnded(Long auctionId) {
        restTemplate.postForObject(
            notificationServiceUrl + "/api/notification/completed/{auctionId}",
            null,
            String.class,
            auctionId
        );
    }
} 