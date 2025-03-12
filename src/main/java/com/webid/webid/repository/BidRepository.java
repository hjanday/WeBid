package com.webid.webid.repository;

import com.webid.webid.model.Bid;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b.user.id FROM Bid b WHERE b.auction.id = :auctionId")
    List<Bid> findByAuctionId(@Param("auctionId") Long auctionId);
    // You can add custom queries here if needed, for example:
    // List<Bid> findByAuctionId(Long auctionId);
    // List<Bid> findByUserId(Long userId);
}
