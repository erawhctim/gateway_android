package com.gateway.event;

import retrofit.RetrofitError;


public final class ApiError {

    private RetrofitError error;

    public ApiError(RetrofitError error) {
        this.error = error;
    }
}
