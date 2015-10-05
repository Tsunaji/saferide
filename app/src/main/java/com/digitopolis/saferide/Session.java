package com.digitopolis.saferide;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bank on 8/25/2015.
 */
public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setFinish(String finish) {
        prefs.edit().putString("checkfinish", finish).commit();
       // prefs.
    }

    public String getFinish() {
        String usename = prefs.getString("checkfinish","");
        return usename;
    }
}
