package com.gateway.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gateway.R;
import com.gateway.model.Listing;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.gateway.view.fragment.ListingsFragment.ListingType;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ListingsAdapter extends ArrayAdapter<Listing> {

    private Context context;
    private List<Listing> listings;
    private LayoutInflater inflater;
    private ListingType listingType;

    public ListingsAdapter(final Context context, int layoutResId, List<Listing> listings, final ListingType type, final String currentUser) {
        super(context, layoutResId, listings);

        this.context = context;
        this.inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listingType = type;

        // filter out listings for the current user, if the ListingType requires it
        this.listings = type.equals(ListingType.User)
            ? filterListings(listings, currentUser)
            : listings;
    }

    private List<Listing> filterListings(List<Listing> listings, final String currentUser) {
        return FluentIterable.from(listings).filter(new Predicate<Listing>() {
            @Override
            public boolean apply(Listing listing) {
                String owner = listing.getOwner();

                return isNotEmpty(owner) && isNotEmpty(currentUser) && owner.equals(currentUser);
            }
        }).toList();
    }

    @Override
    public Listing getItem(int position) {
        return listings.get(position);
    }


    @Override
    public int getCount() {
        return listings.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListingViewHolder holder;
        Listing listing = getItem(position);

        if (view != null) {
            holder = (ListingViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.listing_item, parent, false);
            holder = new ListingViewHolder(view);
            view.setTag(holder);
        }

        holder.title.setText(listing.getTitle());
        holder.description.setText(listing.getDescription());

        // only set the data if its there
        if (listing.getDate() != null) {
            holder.date.setText(listing.getDate());
        } else {
            holder.date.setText("");
        }

        // only set the owner if we're looking at "all" listings
        holder.owner.setText(" - " + listing.getOwner());
        holder.owner.setVisibility(
            listingType.equals(ListingType.All)
                ? View.VISIBLE
                : View.GONE
        );

        return view;
    }


    static class ListingViewHolder {
        @InjectView(R.id.listing_title) TextView title;
        @InjectView(R.id.listing_description) TextView description;
        @InjectView(R.id.listing_date) TextView date;
        @InjectView(R.id.listing_owner) TextView owner;

        public ListingViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
