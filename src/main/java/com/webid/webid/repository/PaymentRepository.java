package com.webid.webid.repository;

import com.webid.webid.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Optional: You can define custom query methods as needed

    @Query("SELECT p FROM Payment p WHERE p.auctionID = :auctionID")
    Optional<Payment> findByAuctionId(@Param("auctionID") Long auctionID);
    // List<Auction> findByStatus(String status);
}