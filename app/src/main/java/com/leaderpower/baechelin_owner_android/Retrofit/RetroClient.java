package com.leaderpower.baechelin_owner_android.Retrofit;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private RetroApiService apiService;
    public static String baseUrl = RetroApiService.KAKAO_BASE_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder{
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }

    public static RetroClient getInstance(Context context){
        if (context != null)
            mContext = context;

        return  SingletonHolder.INSTANCE;
    }

    private RetroClient(Context context){
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl).build();
    }

    public RetroClient createBaseApi(){
        apiService = create(RetroApiService.class);
        return this;
    }

    public <T> T create(final Class<T> service){
        if (service == null)
            throw new RuntimeException("API service null");
        return  retrofit.create(service);
    }
}
