package com.example.weatherforecast.retrofit;

import com.example.weatherforecast.Api;
import com.example.weatherforecast.URls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit retrofit;
    public static RetrofitClient retrofitClient;

    public static RetrofitClient getInstance() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(URls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            retrofitClient =  new RetrofitClient();

        }


        return retrofitClient;

    }

    public Api getApi () {

        return retrofit.create(Api.class);
    }


}
