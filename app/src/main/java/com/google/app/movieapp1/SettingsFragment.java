package com.google.app.movieapp1;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.util.ArrayList;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer);

        final ArrayList<CheckBoxPreference> alViewMode = new ArrayList<>();

        Preference.OnPreferenceClickListener listener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                for (CheckBoxPreference cbp : alViewMode) {
                    if (!cbp.getKey().equals(preference.getKey()) && cbp.isChecked()) {
                        cbp.setChecked(false);
                    }
                    else if (cbp.getKey().equals(preference.getKey()) && !cbp.isChecked()) {
                        cbp.setChecked(true);
                    }
                }

                switch (preference.getKey()){

                    case "pop":
                        Log.i("Fragment", "" + 0);
                        MainActivity.sort_order = 0;
                        break;

                    case "rate":
                        Log.i("Fragment", "" + 1);
                        MainActivity.sort_order = 1;
                        break;
                    case "fav":
                        Log.i("Fragment", "" + 2);
                        MainActivity.sort_order = 2;
                        break;


                }

//        Bundle queryBundle = new Bundle();
//        queryBundle.putString(MainActivity.SEARCH_QUERY_URL_EXTRA, movie_url_details[sort_order]);

                return false;
            }
        };

        CheckBoxPreference pop = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.pref_sort_pop_value));
        pop.setOnPreferenceClickListener(listener);

        CheckBoxPreference rate = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.pref_sort_rate_value));
        rate.setOnPreferenceClickListener(listener);


        CheckBoxPreference fav = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.pref_sort_fav_value));
        fav.setOnPreferenceClickListener(listener);

        alViewMode.add(pop);
        alViewMode.add(rate);
        alViewMode.add(fav);

    }

}