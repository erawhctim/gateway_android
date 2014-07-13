package com.gateway.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.gateway.R;
import com.gateway.view.fragment.ListingsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Super simple, not-very-customizable adapter because our tab strip
 * will only ever have two tabs (regular listings and my listings)
 */
public class ListingsPagerAdapter extends FragmentStatePagerAdapter {

    private Resources resources;
    private List<ListingsFragment> fragments;

    public ListingsPagerAdapter(FragmentManager fm, ArrayList<ListingsFragment> fragments, Resources resources) {
        super(fm);
        this.resources = resources;
        this.fragments = fragments;
    }

    /**
     * Reloads the listings in the indicated {@code ListingsFragment}
     */
    public void reloadListings(int position) {
        ((ListingsFragment)getItem(position)).loadListings();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.all_listings_title);

            case 1:
                return resources.getString(R.string.my_listings_title);

            default:
                return resources.getString(R.string.listings_title);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
