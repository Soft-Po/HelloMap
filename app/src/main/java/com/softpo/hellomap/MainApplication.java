package com.softpo.hellomap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by softpo on 2016/4/16.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        SDKInitializer.initialize(this);
        super.onCreate();

    }
}
