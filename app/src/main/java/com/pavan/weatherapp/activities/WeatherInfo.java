package com.pavan.weatherapp.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pavan.weatherapp.AppPreferenceUtils;
import com.pavan.weatherapp.R;
import com.pavan.weatherapp.Utils;
import com.pavan.weatherapp.models.Main;
import com.pavan.weatherapp.models.Sys;
import com.pavan.weatherapp.models.Weather;
import com.pavan.weatherapp.models.WeatherResponse;
import com.pavan.weatherapp.network.WeatherRepo;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static com.pavan.weatherapp.activities.SplashActivity.EXTRA_CITY;
import static com.pavan.weatherapp.activities.SplashActivity.EXTRA_RESPONSE;

public class WeatherInfo extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = WeatherInfo.class.getName();
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    ImageView weatherIcon;
    Button btForecast,btUpdateCity;
    EditText editText;
    Dialog dialog;
    private WeatherResponse weatherResponse;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        cityField = findViewById(R.id.city_field);
        updatedField = findViewById(R.id.updated_field);
        detailsField = findViewById(R.id.details_field);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        weatherIcon = findViewById(R.id.weather_icon);
        btForecast = findViewById(R.id.bt_forecast);
        btUpdateCity = findViewById(R.id.bt_city);
        btUpdateCity.setOnClickListener(this);
        btForecast.setOnClickListener(this);

        Intent intent = getIntent();
        WeatherResponse weatherResponse = (WeatherResponse) intent.getSerializableExtra(EXTRA_RESPONSE);
        String city = intent.getStringExtra(EXTRA_CITY);

        AppPreferenceUtils.storeStringValue(this,AppPreferenceUtils.KEY_CITY,city);

        renderWeather(weatherResponse);

    }

    private void loadWeatherDetails(final String city){
        mProgressBar.setVisibility(View.VISIBLE);
        WeatherRepo.getRepo().getWeatherInfoForCity(city).observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(@Nullable WeatherResponse weatherResponse) {
                mProgressBar.setVisibility(View.GONE);
                if(weatherResponse != null) {
                    AppPreferenceUtils.storeStringValue(WeatherInfo.this,AppPreferenceUtils.KEY_CITY,city);
                    renderWeather(weatherResponse);
                }
            }
        });
    }

    private void renderWeather(WeatherResponse weatherResponse){
        try {
            this.weatherResponse = weatherResponse;
            cityField.setText(weatherResponse.getName());
            List<Weather> weatherDetails = weatherResponse.getWeather();
            String details = "";
            if(weatherDetails != null && weatherDetails.size() > 0){
                details = weatherDetails.get(0).getDescription();
            }
            Main main = weatherResponse.getMain();
            if(main != null){
                details = details +
                        "\n" + "Humidity:" + main.getHumidity() + "%" +
                        "\n" + "Pressure:" + main.getPressure() + " hpa";
            }
            detailsField.setText(details);
            Sys sys = weatherResponse.getSys();

            String city = weatherResponse.getName() + "," + sys.getCountry();


            cityField.setText(city);


            currentTemperatureField.setText(getString(R.string.temparature_format,main.getTemp()));

            updatedField.setText(getString(R.string.last_update_time_string,Utils.getFormatedData("MMM d, yyyy HH:mm:ss a",weatherResponse.getDt())));


            Utils.setWeatherIcon(this,weatherIcon,weatherDetails.get(0).getIcon());

        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_city:
                showChangeCityDialog();
                break;
            case R.id.bt_forecast:
                Intent intent = new Intent(this,ForeCastActivity.class);
                intent.putExtra(EXTRA_RESPONSE,weatherResponse);
                startActivity(intent);
                break;
            case R.id.get_weather:
                if(editText != null) {
                    String city = String.valueOf(editText.getText());
                    if(!TextUtils.isEmpty(city)){
                        loadWeatherDetails(city);
                    }
                }
                if(dialog != null)
                    dialog.dismiss();
                break;
        }

    }

    private void showChangeCityDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.splash_main);
        Button button = dialog.findViewById(R.id.get_weather);
        editText = dialog.findViewById(R.id.city_editor);
        button.setOnClickListener(this);
        dialog.show();
    }
}
