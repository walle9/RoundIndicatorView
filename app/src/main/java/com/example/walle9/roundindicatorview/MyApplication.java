package com.example.walle9.roundindicatorview;

import android.app.Application;
import android.content.Context;

/**
 * Created by walle9 on 2018/5/26.
 * 描述:
 */
public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
