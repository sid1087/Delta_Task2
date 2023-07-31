package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Request {
    @SerializedName("type")
    private String type;

    public Request() {
        this.type = "player";
    }
}
