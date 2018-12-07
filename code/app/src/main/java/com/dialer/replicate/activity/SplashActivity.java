package com.dialer.replicate.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dialer.replicate.AppUtils.AppPreferences;
import com.dialer.replicate.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends Activity {
    RxPermissions rxPermissions;
    AppPreferences appPreferences;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appPreferences = new AppPreferences(this);
        rxPermissions = new RxPermissions(this);
        startMainActivity();
    }

    public void permission() {
        rxPermissions.requestEach(android.Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        startMainActivity();
                    } else {
//                        Toast.makeText(SplashActivity.this, "To use this app you need grant these permissions.", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                permission();
                            }
                        }, 300);
                    }
                });
    }


    private void startMainActivity() {
        if (appPreferences.getPrefrenceBoolean("firstTime")) {
            startActivity(new Intent(SplashActivity.this, DiallerViewActivity.class));
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    appPreferences.setPrefrenceBoolean("firstTime", true);
                    startActivity(new Intent(SplashActivity.this, DiallerViewActivity.class));
                    finish();
                }
            }, 2000);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            permission();
        }
    }
}
