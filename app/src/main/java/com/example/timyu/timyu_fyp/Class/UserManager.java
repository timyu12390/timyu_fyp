package com.example.timyu.timyu_fyp.Class;

import android.content.Context;

/**
 * Created by timyu on 3/3/2017.
 */

public class UserManager {
    private static UserManager instance;
    private User user;

    public static UserManager getInstance() {
        if (instance == null) return new UserManager();
        else return instance;
    }

    public UserManager() {
        instance = this;
    }

    public void setUser(Context context, User user) {
        this.user = user;
        SharedPreferencesEditor editor = new SharedPreferencesEditor(context);
        editor.setUser(this.user);
        editor.setLoggedin(true);
    }

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return (user != null);
    }

    public void logout(Context context) {
        user = null;
        SharedPreferencesEditor editor = new SharedPreferencesEditor(context);
        editor.setLoggedin(false);
    }


}
