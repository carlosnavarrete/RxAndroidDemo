package com.example.carlosnavarrete.reactiveandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos.navarrete on 1/18/16.
 */
public class AppConnectivityManager {
    private static List<ConnectivityObserver> mObservers;

    private boolean mConnected;

    private final Context mContext;

    private static AppConnectivityManager mManager;

    public static AppConnectivityManager instance (Context context) {
        if (mManager == null) {
            mManager = new AppConnectivityManager(context);
        }
        return mManager;
    }

    private AppConnectivityManager(Context context){
        mObservers = new ArrayList<ConnectivityObserver>();
        this.mContext = context;
        mConnected = isConnectionEnabled();
    }

    private boolean isConnectionEnabled(){
        try {
            ConnectivityManager cm = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }catch (Exception e) {
            return false;
        }
    }

    public void addObserver(ConnectivityObserver observer) {
        mObservers.add(observer);
        observer.manageNotification(mConnected);
    }

    public void removeObserver(ConnectivityObserver observer) {
        mObservers.remove(observer);
    }

    void notifiyConnectionChange () {
        mConnected = isConnectionEnabled();
        for (ConnectivityObserver observer : mObservers) {
            observer.manageNotification(mConnected);
        }
    }
}
