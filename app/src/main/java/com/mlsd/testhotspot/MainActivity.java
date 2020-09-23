package com.mlsd.testhotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/*
* Test Hotspot:
Lograste iniciar el hotspot, pero la impresion de la ip sale antes de mensaje del callbackproxy.
Además si cierras la aplicación el hotspot se apaga.
*/

public class MainActivity extends AppCompatActivity {
    private String TAG = "mainHotspot";
    private boolean isHotspotEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runThread();
    }

    public void mainToggleHotspot(View view) throws InterruptedException {
        Context mContext = this.getApplicationContext();
        Hotspot mHotspot = new Hotspot(mContext, this);
        isHotspotEnabled = mHotspot.toggleHotspot(isHotspotEnabled);

    };

    private void runThread() {
        new Thread() {
            public void run() {
                while(true){
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView ipView = findViewById(R.id.ip);
                                if(isHotspotEnabled)
                                    ipView.setText(getProxyAddress());
                                else
                                    ipView.setText("Disabled");

                                }
                            });
                        Thread.sleep(1500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public String getProxyAddress()
    {
        String mStrIPAddress = "Not Found";
        try {

            //if(en != null){
                //while (en.hasMoreElements())
                for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();)
                {
                    NetworkInterface intf = en.nextElement();
                    Log.d(TAG, "Name interface: " + intf.getName());
                    //Log.d(TAG, "Name interface: " + intf.getDisplayName());
                    //if(intf.getName().equals("ap0")){
                        //Log.d(TAG, "Entro wlan00");
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                        {
                            //Log.d(TAG, "Entro wlan00 for");
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            Log.d(TAG, "inetAddress2: " + inetAddress.getHostAddress());
                            if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)){
                                if(inetAddress.getHostAddress().startsWith("192.168.43"))
                                    mStrIPAddress = inetAddress.getHostAddress();
                                Log.d(TAG, "mStrIPAddress: " + mStrIPAddress);

                            }

                        }
                    //}

                }
            //}
        }
        catch (SocketException ex)
        {
            Log.e("ServerActivity", ex.toString());
        }
        return mStrIPAddress;
    }
}