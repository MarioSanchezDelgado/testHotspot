package com.mlsd.testhotspot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Handler;

import android.util.Log;

import androidx.core.app.ActivityCompat;

public class Hotspot {
    private final static String TAG = "hotspot";
    private static WifiManager.LocalOnlyHotspotReservation mReservation;
    private boolean isHotspotEnabled = false;
    private final int REQUEST_ENABLE_LOCATION_SYSTEM_SETTINGS = 101;
    private Context mContext;
    private Activity mActivity;
    private Handler mHandler;
    public Hotspot(Context oContext, Activity oActivity) {
        mContext = oContext;
        mActivity = oActivity;
    }

    private boolean isLocationPermissionEnable() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return false;
        }
        return true;
        /*if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[] {Manifest.permission.CHANGE_WIFI_STATE}, 2);
            return false;
        }
        return true;*/
    }

    private void turnOnHotspot() {
        if (!isLocationPermissionEnable()) {
            return;
        }
        WifiManager manager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        if (manager != null) {
            // Don't start when it started (existed)
           // mHandler = new Handler();
//            Runnable mRunnable = new Runnable() {
//                @Override
//                public void run() {

                    manager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                        @Override
                        public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                            super.onStarted(reservation);
                            Log.d(TAG, "Wifi Hotspot is on now");
                            mReservation = reservation;
                            Log.d(TAG, mReservation.toString());
                            Log.d(TAG, mReservation.getWifiConfiguration().preSharedKey);
                            Log.d(TAG, mReservation.getWifiConfiguration().SSID);
                            isHotspotEnabled = true;
                        }

                        @Override
                        public void onStopped() {
                            super.onStopped();
                            Log.d(TAG, "onStopped: ");
                            isHotspotEnabled = false;
                        }

                        @Override
                        public void onFailed(int reason) {
                            super.onFailed(reason);
                            Log.d(TAG, "onFailed: ");
                            isHotspotEnabled = false;
                        }
                    }, null);

                //}

            //};
            //mHandler.postDelayed(mRunnable, 100);
        }
    }

    private void turnOffHotspot() {
        if (!isLocationPermissionEnable()) {
            return;
        }
        if (mReservation != null) {
            Log.d(TAG, mReservation.toString());
            mReservation.close();
            mReservation = null;
            //mHandler.removeCallbacksAndMessages(null);
            isHotspotEnabled = false;
        }
    }

    public boolean toggleHotspot(boolean hotspotEnabled) {
        Log.d(TAG, "Entro a toggleHotspot");
        if (!hotspotEnabled) {
            Log.d(TAG, "Entro a turnOnHotspot");
            turnOnHotspot();
            return true;
        } else {
            Log.d(TAG, "Entro a turnOffHotspot");
            turnOffHotspot();
            return false;
        }
    }
}
