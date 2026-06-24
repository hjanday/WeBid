package com.webid.webid.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webid.webid.model.User;
import com.webid.webid.model.Bid;
import com.webid.webid.model.Notification;
import com.webid.webid.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.webid.webid.repository.BidRepository;
import com.webid.webid.repository.NotificationRepository;


@Service
public class NotificationService implements Observer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public void notify(User user, String message) {
        // Save notification in local DB
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);

        // set most recent notification to be the user's current notification
        user.setNotif(message);
        userRepository.save(user);

        // Send notification to microservice
        //notificationClient.notifyUser(user.getId(), message);
    }

    // returns the user's most recent notification
    public String getNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getNotif();
    }

    // returns a list of user notifications
    public List<String> getUserNotifications(User user) {
        return notificationRepository.findByUserIdOrderByIdDesc(user.getId()).stream()
                .map(Notification::getMessage)
                .collect(Collectors.toList());
    }

    // sends a notification to users when auction is ended
    public void notifyEnded(long auctionID) {
        // Find previous bidders and notify all
        List<Bid> prevBidders = bidRepository.findByAuctionId(auctionID);
        Set<User> prevUsers = prevBidders.stream()
                .map(Bid::getUser)
                .collect(Collectors.toSet());

        for (User u : prevUsers) {
            notify(u, String.format("Auction %s has come to an end.",
                    bidRepository.findByAuctionId(auctionID).getFirst().getAuction().getItemName()));
        }

        // Notify microservice about auction end
        //notificationClient.notifyAuctionEnded(auctionID);
    }
    // deletes user notifications
    @Transactional
    public void deleteNotifications(User user){
        notificationRepository.deleteByUserId(user.getId());
    }
}
