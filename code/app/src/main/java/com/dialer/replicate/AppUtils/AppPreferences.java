package com.dialer.replicate.AppUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {


    SharedPreferences pref;
    Editor editor;
    public static Context mContext;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Dailler_prefrences";

    @SuppressLint("CommitPrefEdits")
    public AppPreferences(Context context) {
        this.mContext = context;
        pref = AppPreferences.mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean getPrefrenceBoolean(String keyName) {
        return pref.getBoolean(keyName, false);
    }

    public void setPrefrenceBoolean(String keyName, Boolean booleanValue) {
        editor.putBoolean(keyName, booleanValue);
        editor.commit();
    }

    public String getPrefrenceString(String keyName) {
        return pref.getString(keyName, "");
    }

    public void setPrefrenceString(String keyName, String stringValue) {
        editor.putString(keyName, stringValue);
        editor.commit();
    }



}
