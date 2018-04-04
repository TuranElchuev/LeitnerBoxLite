package com.gmail.at.telchuev.leitnerboxlite;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static Context context;

    public static final String TAG = "LeitnerBoxLite";

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}