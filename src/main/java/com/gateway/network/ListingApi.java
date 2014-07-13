package com.gateway.network;


import com.gateway.model.Listing;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface ListingApi {

    @GET("/listlistings")
    void listListings(Callback<ListingResponse> response);

    static class ListingResponse {
        List<Listing> listings;
    }
}
