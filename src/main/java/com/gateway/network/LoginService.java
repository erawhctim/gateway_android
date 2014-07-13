package com.gateway.network;

import com.gateway.GatewayApp;
import com.gateway.event.ApiError;
import com.gateway.event.LoginEvent;
import com.gateway.model.BoolResult;
import com.gateway.model.User;
import com.squareup.otto.Bus;


import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginService {

    private LoginApi mApi;
    private Bus mBus;

    @Inject
    public LoginService(LoginApi api, Bus bus) {
        mApi = api;
        mBus = bus;
        mBus.register(this);
    }

    public void login(String username, String password) {

        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        mApi.login(loginUser, new Callback<BoolResult>() {
            @Override
            public void success(BoolResult result, Response response) {
                if (response.getStatus() == 200) {
                    mBus.post(new LoginEvent(result.asBoolean()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(new ApiError(error));
            }
        });
    }
}
