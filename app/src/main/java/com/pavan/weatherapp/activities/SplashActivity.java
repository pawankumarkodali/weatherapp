package com.pavan.weatherapp.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pavan.weatherapp.R;
import com.pavan.weatherapp.models.WeatherResponse;
import com.pavan.weatherapp.network.WeatherRepo;

public class SplashActivity extends AppCompatActivity {
    public static final String EXTRA_RESPONSE = "response";
    public static final String EXTRA_CITY = "city";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        final EditText editText = findViewById(R.id.city_editor);
        Button button = findViewById(R.id.get_weather);
        mProgressBar = findViewById(R.id.progress_bar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(editText.getText());
                if (!TextUtils.isEmpty(text)) {
                    loadWeatherDetails(text);
                } else {
                    Toast.makeText(SplashActivity.this, R.string.enter_valid_city, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadWeatherDetails(final String city){
        mProgressBar.setVisibility(View.VISIBLE);
        WeatherRepo.getRepo().getWeatherInfoForCity(city).observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(@Nullable WeatherResponse weatherResponse) {
                mProgressBar.setVisibility(View.GONE);
                if(weatherResponse != null) {
                    Intent intent = new Intent(SplashActivity.this, WeatherInfo.class);
                    intent.putExtra(EXTRA_RESPONSE,weatherResponse);
                    intent.putExtra(EXTRA_CITY,city);
                    startActivity(intent);
                    finishAffinity();
                }else {
                    Toast.makeText(SplashActivity.this,R.string.enter_valid_city,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
