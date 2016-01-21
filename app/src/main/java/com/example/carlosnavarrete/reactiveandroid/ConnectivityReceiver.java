package com.example.carlosnavarrete.reactiveandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by carlos.navarrete on 1/18/16.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppConnectivityManager.instance(context).notifiyConnectionChange();
    }
}
