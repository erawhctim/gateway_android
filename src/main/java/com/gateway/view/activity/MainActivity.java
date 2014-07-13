package com.gateway.view.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.gateway.event.NavDrawerSelectionEvent;
import com.gateway.event.NowLoggedInEvent;
import com.gateway.view.fragment.DrawerFragment;
import com.gateway.R;
import com.gateway.view.fragment.ListingsRootFragment;
import com.gateway.view.fragment.LoginFragment;
import com.gateway.view.fragment.ListingsFragment;
import com.squareup.otto.Subscribe;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.fragment_container) FrameLayout mFragmentContainer;

    private DrawerFragment mDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // we HAVE to call 'setLayout()' before calling super.onCreate(), so
        // our BaseActivity class can set up the layout/view injection properly
        super.setLayout(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mDrawerFragment.setUp(
            R.id.navigation_drawer,
            (DrawerLayout) findViewById(R.id.drawer_layout)
        );

        // initially show the login page
        showLogin();
    }

    @Subscribe
    public void onNavDrawerEvent(NavDrawerSelectionEvent event) {

        switch (event.getNavItem()) {

            case login:
                showLogin();
                break;

            case listings:
                showListings();
                break;
        }
    }

    /**
     * Show the main listings fragment after the login screen sends a success event
     * @param event
     */
    @Subscribe
    public void afterLogin(NowLoggedInEvent event) {
        showListings();
    }

    private void showLogin() {
        showFragment(LoginFragment.newInstance());
    }

    private void showListings() {
        showFragment(ListingsRootFragment.newInstance());
    }

    private void showFragment(Fragment newFragment) {
        // check to see if we already have a fragment with this class name
        Fragment currentFragment = getFragmentManager().findFragmentByTag(
            newFragment.getClass().getName()
        );

        // if the result is null, then the current fragment is not this
        // one's class, so we can switch it out without problems
        if (currentFragment == null) {
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, newFragment, newFragment.getClass().getName())
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);

//            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
//
//            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
//            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    mBus.post(new SearchEvent(query));
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return true;
//                }
//            });

            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
