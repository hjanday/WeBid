package com.webid.webid.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.webid.webid.model.Auction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuctionCache implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int MAX_ENTRIES = 5; // Adjust as needed

    // LRU cache: key is the search term, value is a list of Optional<Auction>
    private final Map<String, List<Optional<Auction>>> cache = new LinkedHashMap<String, List<Optional<Auction>>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1210852929468364761L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, List<Optional<Auction>>> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    public void put(String searchTerm, List<Optional<Auction>> auctions) {
        cache.put(searchTerm, auctions);
    }

    public List<Optional<Auction>> get(String searchTerm) {
        return cache.get(searchTerm);
    }

    public boolean contains(String searchTerm) {
        return cache.containsKey(searchTerm);
    }

    public Map<String, List<Optional<Auction>>> getCache() {
        return cache;
    }
}
