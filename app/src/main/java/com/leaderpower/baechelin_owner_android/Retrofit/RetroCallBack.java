package com.leaderpower.baechelin_owner_android.Retrofit;

public interface RetroCallBack<T> {
    void onError(Throwable t);
    void onSuccess(int code, T receivedData);
    void onFailure(int code);
}
