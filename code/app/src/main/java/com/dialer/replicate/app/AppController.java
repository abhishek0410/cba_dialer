package com.dialer.replicate.app;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.dialer.replicate.AppUtils.AppPreferences;
import com.dialer.replicate.R;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppController extends MultiDexApplication {


    public static AppPreferences mAppPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppPreferences = new AppPreferences(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/optus_heavy_bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        MultiDex.install(this);
    }

    public static AppPreferences getmAppPreferences() {
        return mAppPreferences;
    }

}
