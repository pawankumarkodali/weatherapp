package com.pavan.weatherapp.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.pavan.weatherapp.BuildConfig;
import com.pavan.weatherapp.models.WeatherForecast;
import com.pavan.weatherapp.models.WeatherResponse;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepo {
    private static ApiInterface sApiClient;
    private static WeatherRepo sInstance;
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private WeatherRepo(){
        if(sApiClient == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request actualrequest = chain.request();
                    HttpUrl actualUrl = actualrequest.url();

                    HttpUrl url = actualUrl.newBuilder().
                            addQueryParameter("APPID", BuildConfig.AppId).
                            addQueryParameter("units","metric").
                            build();

                    Request request = actualrequest.newBuilder().
                            url(url).
                            build();

                    return chain.proceed(request);
                }
            });

            httpClient.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder().
                    baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(httpClient.build()).
                    build();

            sApiClient = retrofit.create(ApiInterface.class);
        }
    }

    public static WeatherRepo getRepo(){
        if(sInstance == null)
            sInstance = new WeatherRepo();
        return sInstance;
    }

    public LiveData<WeatherResponse> getWeatherInfoForCity(String city){
        final MutableLiveData<WeatherResponse> weatherResponse = new MutableLiveData<>();

        sApiClient.getWeatherDetailsForCity(city).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                weatherResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherResponse.setValue(null);

            }
        });

        return weatherResponse;
    }

    public LiveData<WeatherForecast> getWeatherForecastForCity(String city){
        final MutableLiveData<WeatherForecast> weatherResponse = new MutableLiveData<>();

        sApiClient.getWeatherForecastForCity(city).enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, retrofit2.Response<WeatherForecast> response) {
                weatherResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                weatherResponse.setValue(null);

            }
        });

        return weatherResponse;
    }

}
