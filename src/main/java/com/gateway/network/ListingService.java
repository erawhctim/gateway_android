package com.gateway.network;

import com.gateway.event.ApiError;
import com.gateway.event.ListingsLoadedEvent;
import com.gateway.model.Listing;
import com.squareup.otto.Bus;


import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.gateway.network.ListingApi.ListingResponse;

public class ListingService {

    private ListingApi mApi;
    private Bus mBus;

    @Inject
    public ListingService(ListingApi api, Bus bus) {
        mApi = api;
        mBus = bus;
        mBus.register(this);
    }

    public void loadListings() {
        mApi.listListings(new Callback<ListingResponse>() {
            @Override
            public void success(ListingResponse listingResponse, Response response) {
                if (response.getStatus() == 200) {
                    mBus.post(new ListingsLoadedEvent(
                        listingResponse.listings != null
                            ? listingResponse.listings
                            : new ArrayList<Listing>()
                    ));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(new ApiError(error));
            }
        });
    }
}
