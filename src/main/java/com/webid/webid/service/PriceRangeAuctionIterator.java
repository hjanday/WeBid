package com.webid.webid.service;

import java.util.List;

import com.webid.webid.model.Auction;

public class PriceRangeAuctionIterator implements AuctionIterator {
    private List<Auction> auctions;
    private int position = 0;
    private double minPrice;
    private double maxPrice;

    public PriceRangeAuctionIterator(List<Auction> auctions, double minPrice, double maxPrice) {
        this.auctions = auctions;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        moveToNextValid();
    }

    private void moveToNextValid() {
        while (position < auctions.size()) {
            double price = auctions.get(position).getCurrentBid();
            if (price >= minPrice && price <= maxPrice) {
                break;
            }
            position++;
        }
    }

    @Override
    public boolean hasNext() {
        return position < auctions.size();
    }

    @Override
    public Auction next() {
        Auction auction = auctions.get(position++);
        moveToNextValid(); // Move to next valid after returning current
        return auction;
    }
}

