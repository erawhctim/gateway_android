package com.gateway;

import android.app.Application;

import com.gateway.view.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    includes = {
        DataModule.class,
        UiModule.class
    }, injects = {
        GatewayApp.class
    }
)
public class GatewayModule {

    private final GatewayApp app;

    public GatewayModule(GatewayApp app) {
        this.app = app;
    }

    @Provides @Singleton
    Application provideApplication() {
        return app;
    }

}
