package com.webid.webid.repository;

import com.webid.webid.model.Auction;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    // Optional: You can define custom query methods as needed
    Optional<Auction> findById(Long id);
    
    @Query("SELECT a FROM Auction a WHERE a.endTime<=:now and a.over=false")
    List<Auction> findByEndTimeBeforeAndNotifiedFalse(LocalDateTime now);

    @Transactional
    @Query("SELECT a FROM Auction a WHERE a.itemName LIKE CONCAT('%', :itemName, '%')")
    List<Auction> findByItemName(@Param("itemName") String itemName);
    

    // For example, find all auctions with a particular status
    // List<Auction> findByStatus(String status);
}