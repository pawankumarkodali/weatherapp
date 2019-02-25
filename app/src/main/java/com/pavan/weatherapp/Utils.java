package com.pavan.weatherapp;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final String IMAGE_URL = "http://openweathermap.org/img/w/";

    public static String getImageUrl(String imageId){
        return IMAGE_URL + imageId + ".png";
    }

    public static void setWeatherIcon(Context context, ImageView imageView,String iconId){
        Glide.with(context)
                .load(Utils.getImageUrl(iconId))
                .into(imageView);

    }

    public static String getFormatedData(String format,long timestamp){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(new Date(timestamp * 1000));
        return dateString;
    }
}
