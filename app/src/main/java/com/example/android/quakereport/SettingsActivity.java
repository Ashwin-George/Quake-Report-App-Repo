package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

    }
    public static class EarthQuakePrefrenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference minMagnitude=findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            Preference orderBy= findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        private void bindPreferenceSummaryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences=
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString=sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference,preferenceString);

        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String strvalue=newValue.toString();
            if(preference instanceof ListPreference){
                ListPreference listPreference=(ListPreference) preference;
                int preIndex=listPreference.findIndexOfValue(strvalue);
                if(preIndex>=0){
                    CharSequence[] labels= listPreference.getEntries();
                    preference.setSummary(labels[preIndex]);
                }
                else{
                    preference.setSummary(strvalue);
                }
            }
            return true;
        }
    }
}
