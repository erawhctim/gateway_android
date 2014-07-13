package com.gateway;

import android.app.Application;
import android.content.Context;

import com.gateway.network.ListingService;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class GatewayApp extends Application {

    private ObjectGraph objectGraph;

    private boolean loggedIn;
    private String currentUser;

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Crashlytics.start(this);
            // Timber.plant(new CrashlyticsTree());
        }

        buildObjectGraphAndInject();
        logout();
    }

    @DebugLog
    public void buildObjectGraphAndInject() {
        if (objectGraph == null) {
            objectGraph = ObjectGraph.create(
                new GatewayModule(this)
            );
        }
        inject(this);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static GatewayApp get(Context context) {
        return (GatewayApp) context.getApplicationContext();
    }

    public void login(String user) {
        loggedIn = true;
        currentUser = user;
    }

    public void logout() {
        loggedIn = false;
        currentUser = null;
    }

    public boolean loggedIn() {
        return loggedIn;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
