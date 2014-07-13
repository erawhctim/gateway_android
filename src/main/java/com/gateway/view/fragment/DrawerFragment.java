package com.gateway.view.fragment;

import android.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gateway.GatewayApp;
import com.gateway.R;
import com.gateway.event.NavDrawerSelectionEvent;
import com.gateway.event.NowLoggedInEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;
import static com.gateway.event.NavDrawerSelectionEvent.NavItem;
import static com.gateway.event.NavDrawerSelectionEvent.NavItem.*;
import static android.widget.AbsListView.CHOICE_MODE_NONE;

public class DrawerFragment extends BaseFragment {

    private static final String STATE_SELECTED_POSITION = "selected_nav_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "nav_drawer_learned";

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    // the default position is
    private int mlastSelectedPosition = 1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public DrawerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mlastSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        //selectItem(mlastSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // set up drawer list
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        updateList();

        mDrawerListView.setItemChecked(mlastSelectedPosition, true);

        return mDrawerListView;
    }

    public void updateList() {
        if (mDrawerListView != null) {
            mDrawerListView.setAdapter(new ArrayAdapter<>(
                getActionBar().getThemedContext(),
                R.layout.nav_drawer_item,
                R.id.drawer_item,
                getListItems()
            ));
            //mDrawerListView.invalidate();
        }
    }

    @Subscribe
    public void onceLoggedIn(NowLoggedInEvent event) {
        // update the drawer list to show the username and listings options
        updateList();

        // the listings page is automatically loaded, so set it as the selected item
    }

    private String[] getListItems() {

        GatewayApp app = ((GatewayApp)getActivity().getApplication());

        return app.loggedIn()
            ? new String[] {
                app.getCurrentUser(),
                getString(R.string.listings_title)
            }
            : new String[] {
                getString(R.string.login_title)
            };
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
            getActivity(),                   /* host Activity */
            mDrawerLayout,                   /* DrawerLayout object */
            R.drawable.ic_drawer,            /* nav drawer image to replace 'Up' caret */
            R.string.navigation_drawer_open,        /* "open drawer" description for accessibility */
            R.string.navigation_drawer_close        /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {

        // don't set the selected state for the login menu item
        // and don't set any selection if the choice mode is NONE
        if (mDrawerListView != null) {
            if (mDrawerListView.getChoiceMode() != CHOICE_MODE_NONE) {

                mDrawerListView.setSelection(
                    position == indexOf(login)
                        ? mlastSelectedPosition
                        : position
                );
            }

            mDrawerListView.setChoiceMode(CHOICE_MODE_SINGLE);
        }

        mlastSelectedPosition = position;

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }

        sendNavDrawerEvent();
    }

    private void sendNavDrawerEvent() {
        mBus.post(new NavDrawerSelectionEvent(
            NavItem.findByIndex(mlastSelectedPosition)
        ));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mlastSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

}
