package com.webid.webid.service;

import com.webid.webid.model.Auction;

public interface AuctionIterator {
    boolean hasNext();
    Auction next();
}
