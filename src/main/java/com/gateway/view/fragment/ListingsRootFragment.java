package com.gateway.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gateway.R;
import com.gateway.view.PagerSlidingTabStrip;
import com.gateway.view.adapter.ListingsPagerAdapter;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.gateway.view.fragment.ListingsFragment.ListingType.*;

public class ListingsRootFragment extends BaseFragment {

    @InjectView(R.id.listings_root_viewpager) ViewPager mViewPager;
    @InjectView(R.id.listings_root_tab_strip) PagerSlidingTabStrip mTabStrip;

    public static ListingsRootFragment newInstance() {
        return new ListingsRootFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listings_root, container, false);

        ButterKnife.inject(this, rootView);

        // set up the viewpager and tabs
        ArrayList<ListingsFragment> fragments = Lists.newArrayList(
            ListingsFragment.newInstance(All),
            ListingsFragment.newInstance(User)
        );
        mViewPager.setAdapter(new ListingsPagerAdapter(getFragmentManager(), fragments, getResources()));
        mTabStrip.setViewPager(mViewPager);

        // clicking a tab will reload the results
        mTabStrip.setOnTabClickListener(new PagerSlidingTabStrip.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                ((ListingsPagerAdapter)mViewPager.getAdapter()).reloadListings(position);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //loadListings();
    }
}
