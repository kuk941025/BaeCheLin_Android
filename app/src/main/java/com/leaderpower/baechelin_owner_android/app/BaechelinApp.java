package com.leaderpower.baechelin_owner_android.app;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;

public class BaechelinApp extends Application {
    private static BaechelinApp instance;
    private static FirebaseUser currentUser;
    private static BusinessInfo businessInfo;

    public static BaechelinApp getInstance() {return instance;}

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(FirebaseUser currentUser) {
        BaechelinApp.currentUser = currentUser;
    }

    public BusinessInfo getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(BusinessInfo businessInfo) {
        BaechelinApp.businessInfo = businessInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
