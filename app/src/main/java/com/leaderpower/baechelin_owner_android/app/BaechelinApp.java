package com.leaderpower.baechelin_owner_android.app;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class BaechelinApp extends Application {
    private static BaechelinApp instance;
    private static FirebaseUser currentUser;

    public static BaechelinApp getInstance() {return instance;}

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(FirebaseUser currentUser) {
        BaechelinApp.currentUser = currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
