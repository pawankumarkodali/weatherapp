package com.pavan.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferenceUtils {
    private static final String PREF_NAME = "weatherapp_pref";
    public static final String KEY_CITY = "city";
    public static void storeStringValue(Context context,String key,String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit();
        editor.putString(key,value).apply();
    }

    public static String getStringValue(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return pref.getString(key,"");
    }
}
