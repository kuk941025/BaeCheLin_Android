package com.leaderpower.baechelin_owner_android.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.leaderpower.baechelin_owner_android.model.BusinessInfo;

public class BaechelinApp extends Application {
    private static BaechelinApp instance;
    private static FirebaseUser currentUser;
    private static BusinessInfo businessInfo;
    private String Registeration_Token;
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

    public String getRegisteration_Token() {
        return Registeration_Token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) return;
                Registeration_Token = task.getResult().getToken();
                Log.d("REGISTERATION TOKEN: ", Registeration_Token);
            }
        });
    }
}
