package com.webid.webid.repository;

import com.webid.webid.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // Optional: You can define custom query methods as needed
    Optional<Auction> findById(Long id);

    // For example, find all auctions with a particular status
    // List<Auction> findByStatus(String status);
}