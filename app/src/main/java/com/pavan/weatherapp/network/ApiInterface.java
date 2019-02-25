package com.pavan.weatherapp.network;

import com.pavan.weatherapp.models.WeatherForecast;
import com.pavan.weatherapp.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather/")
    Call<WeatherResponse> getWeatherDetailsForCity(@Query("q") String city);

    @GET("forecast/")
    Call<WeatherForecast> getWeatherForecastForCity(@Query("q") String city);

}
