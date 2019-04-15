package com.leaderpower.baechelin_owner_android.Retrofit.Response;

public class ResponseKakao {
    public final int response_code;
    public final String response_message;

    public ResponseKakao(int response_code, String response_message) {
        this.response_code = response_code;
        this.response_message = response_message;
    }
}
