package com.quandisa.nintyml.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * This class contains connection related APIs
 */
public class NetworkManager {
	public static boolean isNetworkAvailable(Context context) {
		// Check for data connection
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
			// Data connection available
			return true;
		}
 
		// Check for wifi connecion
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled() && wifi.getConnectionInfo().getSSID() != null) {
			// Wifi connected
			return true;
		}

		// No connection
		return false;
	}
}
