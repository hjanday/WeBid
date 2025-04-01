package com.webid.webid.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webid.webid.model.User;
import com.webid.webid.model.Bid;
import com.webid.webid.model.Notification;

import jakarta.transaction.Transactional;

import com.webid.webid.repository.NotificationRepository;

@Service
public class NotificationService implements Observer {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    private User getUserFromApiByEmail(String email) {
        try{
            String url = "http://localhost:8080/api/users/email/" + email;
            return restTemplate.getForObject(url, User.class);
        }catch(Error e){
            throw new RuntimeException("User not found");
        }
    }
    private User getUserFromApiByID(Long id) {
        String url = "http://localhost:8080/api/users/" + id;
        try {
            User user = restTemplate.getForObject(url, User.class);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            return user;
        } catch (RestClientException e) {
            throw new RuntimeException("User not found", e);
        }
    }

    private List<Bid> getAuctionByID(Long id) {
        try {
            String url = "http://localhost:8080/api/Bid/find/" + id;
            ResponseEntity<List<Bid>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Bid>>() {
                    });
            return response.getBody();
        } catch (Error e) {
            throw new RuntimeException("Auction not found");
        }

    }

    @Override
    public void notify(Long Id, String message) {
        Notification notification = new Notification();
        notification.setUser(getUserFromApiByID(Id));
        notification.setMessage(message);

        // Save notification in notification DB
        notificationRepository.save(notification);

        // set most recent notification to be the user's current notification
        getUserFromApiByID(Id).setNotif(message);
    }

    // returns the user's most recent notification
    public String getNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = getUserFromApiByEmail(username);

        return user.getNotif();
    }

    // returns a list of user notifications
    public List<String> getUserNotifications(User user) {
        return notificationRepository.findByUserIdOrderByIdDesc(user.getId()).stream()
                .map(Notification::getMessage)
                .collect(Collectors.toList());
    }

    // sends a notification to users when auction is ended. note that this requres
    // both auction and bids.
    public void notifyEnded(long auctionID) {
        // Find previous bidders and notify all.
        List<Bid> prevBidders = getAuctionByID(auctionID);
        Set<User> prevUsers = prevBidders.stream()
                .map(Bid::getUser) // Extracts the User from each Bid
                .collect(Collectors.toSet()); // Collects into a Set to ensure uniqueness

        for (User u : prevUsers) {
            notify(u.getId(), String.format("Auction %s has come to an end.",
                    getAuctionByID(auctionID).getFirst().getAuction().getItemName()));
        }
    }

    // deletes user notifications
    @Transactional
    public void deleteNotifications(User user) {
        notificationRepository.deleteByUserId(user.getId());
    }
}

