package com.gateway.view;


import com.gateway.view.activity.MainActivity;

import dagger.Module;

@Module(
    injects = {
        MainActivity.class
    },
    complete = false,
    library = true
)
public class UiModule {

    // UI-related @Provides methods here

}
