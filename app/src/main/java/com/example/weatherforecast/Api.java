package com.example.weatherforecast;

import com.example.weatherforecast.Models.FWeather;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Api {


@GET("current")
    Call<JsonObject> weatherCall(@QueryMap Map<String , String> map);

@GET("current")
Call<JsonObject> authorizedzation(@Query("access_key") String access_key);

@GET("onecall")
    Call<JsonObject> fweatherCall(@Query("lat") String lat , @Query("lon") String lon , @Query("key") String key);
}
