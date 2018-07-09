package com.freeme.freemelite.attendance.datapush.push.request;

import com.freeme.freemelite.attendance.datapush.push.response.AuthTokenResponse;
import com.freeme.freemelite.attendance.datapush.push.response.PushResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface PushRequest {
    //获取auth token
    @Headers("Content-Type: application/json")
    @HTTP(method = "POST", path = "{appid}/auth_sign", hasBody = true)
    Call<AuthTokenResponse> authTokenRequest(@Path("appid") String appid, @Body AuthTokenRequestBody authTokenRequestBody);

    //单推
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "POST", path = "{appid}/push_single", hasBody = true)
    Call<PushResponse> singlePushRequest(@Path("appid") String appid, @Header("authtoken") String authtoken, @Body SinglePushRequestBody singlePushRequestBody);
}
