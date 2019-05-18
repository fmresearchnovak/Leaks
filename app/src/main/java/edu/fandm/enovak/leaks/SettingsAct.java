package edu.fandm.enovak.leaks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFrag())
                .commit();
    }
}
