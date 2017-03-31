package com.example.timyu.timyu_fyp.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferencesEditor {
    private final String PREF_USER_LOGGED_IN = "PREF_USER_LOGGED_IN";

    private final String PREF_USER_ID = "PREF_USER_ID";
    private final String PREF_USER_NAME = "PREF_USER_NAME";
    private final String PREF_USER_EMAIL = "PREF_USER_EMAIL";

    private Context context;
    private SharedPreferences sharedPreferences;

    public SharedPreferencesEditor(Context context) {
        this.context = context;
    }

    private void open() {
        //sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLoggedin(boolean isLoggedIn) {
        open();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_USER_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean getIsLoggedIn() {
        open();
        return sharedPreferences.getBoolean(PREF_USER_LOGGED_IN, false);
    }

    //get user data
    public void setUser(User user) {
        open();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(PREF_USER_ID, user.getId());
        editor.putString(PREF_USER_NAME, user.getName());
        editor.putString(PREF_USER_EMAIL, user.getEmail());
        editor.apply();
    }


    public User getUser() {
        open();
        long id = sharedPreferences.getLong(PREF_USER_ID, 0);
        String name = sharedPreferences.getString(PREF_USER_NAME, null);
        String email = sharedPreferences.getString(PREF_USER_EMAIL, null);
        return new User(id, name, email);
    }
}
