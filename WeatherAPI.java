package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// API 요청 메소드를 정의하는 인터페이스
public interface WeatherAPI {

    @GET("weather")
    Call<WeatherResponse> getCurrentWeatherByCoord(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String langCode
    );
}
