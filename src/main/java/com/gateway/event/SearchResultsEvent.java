package com.gateway.event;

import com.gateway.model.Listing;

import java.util.List;


public final class SearchResultsEvent {

    private List<Listing> listings;

    public SearchResultsEvent(List<Listing> listings) {
        this.listings = listings;
    }

    public List<Listing> getListings() {
        return listings;
    }
}
