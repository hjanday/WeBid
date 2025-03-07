package com.webid.webid.repository;

import com.webid.webid.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    // You can add custom queries here if needed, for example:
    // List<Bid> findByAuctionId(Long auctionId);
    // List<Bid> findByUserId(Long userId);
}
