package com.webid.webid.repository;

import com.webid.webid.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Optional: You can define custom query methods as needed
    Optional<Payment> findById(Long id); // For example, find all auctions with a particular status
    // List<Auction> findByStatus(String status);
}