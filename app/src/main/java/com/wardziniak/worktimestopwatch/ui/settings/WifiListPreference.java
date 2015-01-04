package com.wardziniak.worktimestopwatch.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wardziniak on 1/2/15.
 */
public class WifiListPreference extends ListPreference {

    private final static String ENTRY_SUFFIX = "_entry";
    private final static String NOT_SET = "Not set";

    private Context context;

    public WifiListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WifiListPreference(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onClick() {
        WifListLoader wifListLoader = new WifListLoader();
        wifListLoader.execute();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if (getEntry() != null) {
            final String entry = getEntry().toString();
            getPreferenceManager().getSharedPreferences().edit().putString(getKey() + ENTRY_SUFFIX, entry).commit();
            setSummary(entry);
        }
        else {
            setSummary(getPreferenceManager().getSharedPreferences().getString(getKey() + ENTRY_SUFFIX, NOT_SET));
        }
    }


    private void onLoadListEntries(ListPreferenceEntries listPreferenceEntries) {
        if (listPreferenceEntries == null) {
            // Nie udalo sie zaladowac danych
            Toast.makeText(context, "Sprawdź, czy masz wlączone WIFI", Toast.LENGTH_SHORT).show();
        }
        else
        {
            setEntries(listPreferenceEntries.getEntries().toArray(new String[0]));
            setEntryValues(listPreferenceEntries.getValues().toArray(new String[0]));
            super.onClick();
        }
    }

    private class WifListLoader extends AsyncTask<Void, Void, ListPreferenceEntries> {

        @Override
        protected ListPreferenceEntries doInBackground(Void... params) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
            if (wifiConfigurations == null)
                return null;
            ListPreferenceEntries listPreferenceEntries = new ListPreferenceEntries();
            for (WifiConfiguration wifiConfiguration: wifiConfigurations) {
                listPreferenceEntries.addEntry(wifiConfiguration);
            }
            return listPreferenceEntries;
        }

        @Override
        protected void onPostExecute(ListPreferenceEntries listPreferenceEntries) {
            onLoadListEntries(listPreferenceEntries);
        }
    }

    private static class ListPreferenceEntries {
        private List<String> values;
        private List<String> entries;

        public ListPreferenceEntries() {
            this.values = new ArrayList<String>();
            this.entries = new ArrayList<String>();
        }

        public void addEntry(WifiConfiguration wifiConfiguration) {
            values.add("" + wifiConfiguration.networkId);
            entries.add(wifiConfiguration.SSID.replace("\"", ""));
        }

        public List<String> getValues() {
            return values;
        }

        public List<String> getEntries() {
            return entries;
        }
    }


}
