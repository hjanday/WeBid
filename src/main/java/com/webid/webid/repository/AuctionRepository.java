package com.webid.webid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webid.webid.model.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    // Custom queries can be added here if needed
}
