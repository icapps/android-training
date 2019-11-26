package com.worldline.nicolaldi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author Nicola Verbeeck
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    public static NetworkChangeReceiver startListeningForNetworkChanges(Context context) {
        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        context.registerReceiver(receiver, filter);
        return receiver;
    }

    public static void stopListeningForNetworkChanges(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NetworkChangeReceiver", "Got network change event! -> " + intent);

        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo == null){
            Log.d("NetworkChangeReceiver", "No network");
        } else {
            Log.d("NetworkChangeReceiver", "We got network!");
        }
    }
}
