package com.webid.notification_microservice;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.webid.notification_microservice.Notification;
import com.webid.notification_microservice.NotificationRepository;
import com.webid.notification_microservice.dto.AuctionDTO;

@Service
public class NotificationService implements Observer {

    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${auction.service.url}")
    private String auctionServiceUrl;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void notify(Long userId, String message) {
        // First verify user exists by calling user service
        try {
            restTemplate.getForObject(
                userServiceUrl + "/api/users/{userId}",
                Object.class,
                userId
            );
        } catch (Exception e) {
            throw new RuntimeException("User not found: " + userId);
        }

        // Create notification
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    // Get notifications for a user
    public List<String> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(Notification::getMessage)
                .collect(Collectors.toList());
    }

    // Notify users when an auction ends
    public void notifyAuctionEnded(Long auctionId) {
        try {
            // Get auction details from auction service
            AuctionDTO auction = restTemplate.getForObject(
                auctionServiceUrl + "/api/auctions/{auctionId}",
                AuctionDTO.class,
                auctionId
            );

            if (auction == null) {
                throw new RuntimeException("Auction not found: " + auctionId);
            }

            // Get bidders from auction service
            List<Long> bidderIds = restTemplate.exchange(
                auctionServiceUrl + "/api/auctions/{auctionId}/bidders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {},
                auctionId
            ).getBody();

            // Notify each bidder
            if (bidderIds != null) {
                bidderIds.forEach(userId -> 
                    notify(userId, 
                        String.format("Auction for %s has ended.", auction.getItemName())
                    )
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process auction end notification: " + e.getMessage());
        }
    }
}
