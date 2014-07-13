package com.gateway.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

public class BoolResult {

    @SerializedName("boolResult")
    private String result;

    public boolean asBoolean() {
        return StringUtils.equals("1", result);
    }
}
