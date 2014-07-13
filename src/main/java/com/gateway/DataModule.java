package com.gateway;

import android.app.Application;
import android.content.SharedPreferences;

import com.gateway.network.ListingApi;
import com.gateway.network.ListingService;
import com.gateway.network.LoginApi;
import com.gateway.network.LoginService;
import com.gateway.view.activity.MainActivity;
import com.gateway.view.fragment.DrawerFragment;
import com.gateway.view.fragment.ListingsFragment;
import com.gateway.view.fragment.ListingsRootFragment;
import com.gateway.view.fragment.LoginFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;
import static com.gateway.util.SettingsStrings.PREFERENCES_BASE;
import static com.gateway.util.ApiStrings.API_URL_BASE;

@Module(
    injects = {
        MainActivity.class,
        DrawerFragment.class,
        ListingsFragment.class,
        ListingsRootFragment.class,
        LoginFragment.class
    },
    complete = false,
    library = true
)
public class DataModule {

    @Provides @Singleton
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences(PREFERENCES_BASE, MODE_PRIVATE);
    }

    private RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
            .setEndpoint(API_URL_BASE)
            .setConverter(new GsonConverter(getGsonConfig()))
            .build();
    }

    private Gson getGsonConfig() {
        return new GsonBuilder()
            //.registerTypeAdapter(Listing.class, new ListingAdapter())
            .create();
    }

    @Provides @Singleton
    ListingApi provideListingApi() {
        return provideRestAdapter().create(ListingApi.class);
    }

    @Provides @Singleton
    LoginApi provideLoginApi() { return provideRestAdapter().create(LoginApi.class); }

    @Provides @Singleton
    ListingService provideListingService(ListingApi api, Bus bus) {
        return new ListingService(api, bus);
    }

    @Provides @Singleton
    LoginService provideLoginService(LoginApi api, Bus bus) {
        return new LoginService(api, bus);
    }
}