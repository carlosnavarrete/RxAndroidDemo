package com.example.carlosnavarrete.reactiveandroid;

import android.app.Application;

/**
 * Created by carlos.navarrete on 1/18/16.
 */
public class BaseApp extends Application {

    Test mTest;

    @Override
    public void onCreate() {
        super.onCreate();

        mTest = new Test();
    }

    public Test getObserver() {
        return mTest;
    }

}
