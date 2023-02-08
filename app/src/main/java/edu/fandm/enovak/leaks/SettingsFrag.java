package edu.fandm.enovak.leaks;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFrag extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}

