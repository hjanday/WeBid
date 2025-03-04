package com.webid.webid.repository;

import com.webid.webid.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Query("SELECT i FROM Item i WHERE i.name LIKE %:keyword% OR i.description LIKE %:keyword%")
    ArrayList<Auction> findAuctionByItemName(@Param("itemName") String itemName);

    Auction select(Long itemId);

}
