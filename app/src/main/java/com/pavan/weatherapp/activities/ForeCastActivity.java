package com.pavan.weatherapp.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pavan.weatherapp.AppPreferenceUtils;
import com.pavan.weatherapp.R;
import com.pavan.weatherapp.Utils;
import com.pavan.weatherapp.models.List;
import com.pavan.weatherapp.models.Main;
import com.pavan.weatherapp.models.WeatherForecast;
import com.pavan.weatherapp.models.WeatherResponse;
import com.pavan.weatherapp.network.WeatherRepo;


import static com.pavan.weatherapp.activities.SplashActivity.EXTRA_RESPONSE;

public class ForeCastActivity extends AppCompatActivity {
    private TextView tempMin1,tempMax1,tempMin2,tempMax2,tempMin3,tempMax3,
            tempMin4,tempMax4,tempMin5,tempMax5;

    private ImageView tmpIcon1,tmpIcon2,tmpIcon3,tmpIcon4,tmpIcon5;

    private TextView time1,time2,time3,time4,time5;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherResponse weatherResponse = (WeatherResponse) getIntent().getSerializableExtra(EXTRA_RESPONSE);
        setContentView(R.layout.forecast_view);
        mProgressBar = findViewById(R.id.progress_bar);

        TextView title = findViewById(R.id.tvTitle);
        TextView description = findViewById(R.id.tvDescription);
        ImageView icon = findViewById(R.id.ivWeather);
        Utils.setWeatherIcon(this,icon,weatherResponse.getWeather().get(0).getIcon());
        String titleString = getString(R.string.temparature_format,weatherResponse.getMain().getTemp());
        titleString = titleString + " in " + weatherResponse.getName();
        title.setText(titleString);
        description.setText(weatherResponse.getWeather().get(0).getDescription());



        tempMax1 = findViewById(R.id.tvMaxTemp1);
        tempMin1 = findViewById(R.id.tvMinTemp1);
        tempMax2 = findViewById(R.id.tvMaxTemp2);
        tempMin2 = findViewById(R.id.tvMinTemp2);
        tempMax3 = findViewById(R.id.tvMaxTemp3);
        tempMin3 = findViewById(R.id.tvMinTemp3);
        tempMax4 = findViewById(R.id.tvMaxTemp4);
        tempMin4 = findViewById(R.id.tvMinTemp4);
        tempMax5 = findViewById(R.id.tvMaxTemp5);
        tempMin5 = findViewById(R.id.tvMinTemp5);

        tmpIcon1 = findViewById(R.id.ivWeatherIcon1);
        tmpIcon2 = findViewById(R.id.ivWeatherIcon2);
        tmpIcon3 = findViewById(R.id.ivWeatherIcon3);
        tmpIcon4 = findViewById(R.id.ivWeatherIcon4);
        tmpIcon5 = findViewById(R.id.ivWeatherIcon5);

        time1 = findViewById(R.id.tvTime1);
        time2 = findViewById(R.id.tvTime2);
        time3 = findViewById(R.id.tvTime3);
        time4 = findViewById(R.id.tvTime4);
        time5 = findViewById(R.id.tvTime5);


        loadForeCastData();
    }

    private void loadForeCastData(){
        mProgressBar.setVisibility(View.VISIBLE);
        WeatherRepo.getRepo().getWeatherForecastForCity(AppPreferenceUtils.getStringValue(this,AppPreferenceUtils.KEY_CITY)).
                observe(this, new Observer<WeatherForecast>() {
                    @Override
                    public void onChanged(@Nullable WeatherForecast weatherForecast) {
                        mProgressBar.setVisibility(View.GONE);
                        if(weatherForecast != null) {
                            renderForecastData(weatherForecast);
                        }else {
                            //Toast.makeText(ForeCastActivity.this,R.string,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void renderForecastData(WeatherForecast weatherForecast){
        java.util.List<List> list = weatherForecast.getList();
        List listItem = list.get(0);
        Main main = listItem.getMain();
        tempMax1.setText(getString(R.string.temparature_format,main.getTempMax()));
        tempMin1.setText(getString(R.string.temparature_format,main.getTempMin()));
        time1.setText(Utils.getFormatedData("HH:mm a",listItem.getDt()));
        Utils.setWeatherIcon(this,tmpIcon1,listItem.getWeather().get(0).getIcon());

        listItem = list.get(1);
        main = listItem.getMain();
        tempMax2.setText(getString(R.string.temparature_format,main.getTempMax()));
        tempMin2.setText(getString(R.string.temparature_format,main.getTempMin()));
        time2.setText(Utils.getFormatedData("HH:mm a",listItem.getDt()));
        Utils.setWeatherIcon(this,tmpIcon2,listItem.getWeather().get(0).getIcon());

        listItem = list.get(2);
        main = listItem.getMain();
        tempMax3.setText(getString(R.string.temparature_format,main.getTempMax()));
        tempMin3.setText(getString(R.string.temparature_format,main.getTempMin()));
        time3.setText(Utils.getFormatedData("HH:mm a",listItem.getDt()));
        Utils.setWeatherIcon(this,tmpIcon3,listItem.getWeather().get(0).getIcon());

        listItem = list.get(3);
        main = listItem.getMain();
        tempMax4.setText(getString(R.string.temparature_format,main.getTempMax()));
        tempMin4.setText(getString(R.string.temparature_format,main.getTempMin()));
        time4.setText(Utils.getFormatedData("HH:mm a",listItem.getDt()));
        Utils.setWeatherIcon(this,tmpIcon4,listItem.getWeather().get(0).getIcon());

        listItem = list.get(4);
        main = listItem.getMain();
        tempMax5.setText(getString(R.string.temparature_format,main.getTempMax()));
        tempMin5.setText(getString(R.string.temparature_format,main.getTempMin()));
        time5.setText(Utils.getFormatedData("HH:mm a",listItem.getDt()));
        Utils.setWeatherIcon(this,tmpIcon5,listItem.getWeather().get(0).getIcon());

    }




}
