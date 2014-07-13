package com.gateway.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.gateway.GatewayApp;
import com.gateway.R;
import com.gateway.event.ListingsLoadedEvent;
import com.gateway.model.Listing;
import com.gateway.network.ListingService;
import com.gateway.view.adapter.ListingsAdapter;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListingsFragment extends BaseFragment {

    private static final String OPTION_LISTING_TYPE = "listing_type_option";

    private enum State { Loading, Done };
    public enum ListingType {
        All, User;

        public static ListingType getByIndex(int index) {
            for (ListingType type : values()) {
                if (type.ordinal() == index) {
                    return type;
                }
            }

            return All;
        }
    };

    @InjectView(R.id.listings_loading_spinner) ProgressBar mLoadingSpinner;
    //@InjectView(R.id.listings_header) TextView mHeader;
    @InjectView(R.id.listings) ListView mListings;

    @Inject ListingService mListingService;

    private ArrayAdapter<Listing> mAdapter;
    private ListingType type;

    public static ListingsFragment newInstance(ListingType type) {
        Bundle options = new Bundle();
        options.putInt(OPTION_LISTING_TYPE, type.ordinal());

        ListingsFragment fragment = new ListingsFragment();
        fragment.setArguments(options);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle options = getArguments();

        if (options != null) {
            this.type = ListingType.getByIndex(options.getInt(OPTION_LISTING_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listings, container, false);

        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadListings();
    }

    public void loadListings() {
        // show loading spinner and start loading
        showLoading();
        mListingService.loadListings();
    }


    @Subscribe
    public void onListingsLoaded(ListingsLoadedEvent event) {

        //TODO: set up empty state for 0 listings

        // set up the adapter
        mAdapter = new ListingsAdapter(
            getActivity().getApplicationContext(),
            R.layout.listing_item,
            event.getListings(),
            this.type,
            ((GatewayApp)getActivity().getApplication()).getCurrentUser()
        );

        mListings.setAdapter(mAdapter);

        // hide the spinner & show list
        hideLoading();
    }


    private void showLoading() {
        updateViewState(State.Loading);
    }

    private void hideLoading() {
        updateViewState(State.Done);
    }

    private void updateViewState(State state) {
        if (mLoadingSpinner != null) {
            mLoadingSpinner.setVisibility(
                state.equals(State.Loading)
                    ? View.VISIBLE
                    : View.GONE
            );
        }

        if (mListings != null) {
            mListings.setVisibility(
                state.equals(State.Loading)
                    ? View.GONE
                    : View.VISIBLE
            );
        }

//        if (mHeader != null) {
//            mHeader.setVisibility(
//                state.equals(State.Loading)
//                    ? View.GONE
//                    : View.VISIBLE
//            );
//        }
    }
}