package com.example.mydemos_len.utils;

import com.google.gson.Gson;

import kdt.okhttputils.callback.IGenericsSerializator;

public class JsonGenericsSerializator implements IGenericsSerializator {
    Gson mGson = new Gson();
    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return mGson.fromJson(response, classOfT);
    }
}