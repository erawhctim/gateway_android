package com.gateway.network;

import com.gateway.model.BoolResult;
import com.gateway.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface LoginApi {

    @POST("/login-user")
    void login(@Body User user, Callback<BoolResult> response);
}
