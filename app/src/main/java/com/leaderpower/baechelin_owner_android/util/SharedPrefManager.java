package com.leaderpower.baechelin_owner_android.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String APP_SHARED_PREFS = "BECHELIN_OWNER_PREF";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context){
        this.sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setEmailAndPassword(String email, String password){
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString("email", "");
    }

    public String getPassword(){
        return sharedPreferences.getString("password", "");
    }

    public void setSaveAccount(boolean value){
        editor.putBoolean("save", value);
        editor.commit();
    }

    public boolean isSaved(){
        return sharedPreferences.getBoolean("save", false);
    }
}
