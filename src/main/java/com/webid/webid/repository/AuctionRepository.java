package com.webid.webid.repository;

import com.webid.webid.model.Auction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuctionRepository extends CrudRepository<Auction, Long>{
    Optional<Auction> query(String term);
} 
