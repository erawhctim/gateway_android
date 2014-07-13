package com.gateway.event;


import com.gateway.model.Listing;

import java.util.List;

public final class ListingsLoadedEvent {

    private List<Listing> listings;

    public ListingsLoadedEvent(List<Listing> listings) {
        this.listings = listings;
    }

    public List<Listing> getListings() {
        return listings;
    }
}
