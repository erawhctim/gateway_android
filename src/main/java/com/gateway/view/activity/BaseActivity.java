package com.gateway.view.activity;


import android.app.Activity;
import android.os.Bundle;

import com.gateway.GatewayApp;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {

    @Inject Bus mBus;

    private int mLayoutId;

    public void setLayout(int layoutId) {
        this.mLayoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mLayoutId);
        ButterKnife.inject(this);

        // We perform injection here so every Activity subclass doesn't have to inject itself
        ((GatewayApp) getApplication()).inject(this);

        // hide the action bar title always
        if (getActionBar() != null) {
            getActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        mBus.unregister(this);
    }
}
