package com.leaderpower.baechelin_owner_android.Retrofit;

import com.leaderpower.baechelin_owner_android.Retrofit.Response.ResponseKakao;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetroApiService {
    String KAKAO_BASE_URL = "https://www.apiorange.com/api/";

    @Headers("Authorization: tUsWZZ+I983DVr5AZnuezWsQjz1iIR3e+KoJfPLSs7o=")
    @FormUrlEncoded
    @POST("send/notice.do")
    Call<ResponseKakao> postSendKakao(@FieldMap HashMap<String, Object> parameters);
}
