package com.tuwalike.wedding.config;

import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

@Configuration
public class GsonConfig {

    private Gson gson;

    public GsonConfig() {
        this.gson = new Gson().newBuilder().setLenient().create();
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

}