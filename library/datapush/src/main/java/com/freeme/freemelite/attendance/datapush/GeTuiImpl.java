package com.freeme.freemelite.attendance.datapush;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.freeme.freemelite.attendance.datapush.push.request.AuthTokenRequestBody;
import com.freeme.freemelite.attendance.datapush.push.request.PushRequest;
import com.freeme.freemelite.attendance.datapush.push.request.SinglePushRequestBody;
import com.freeme.freemelite.attendance.datapush.push.request.SinglePushRequestBody.MessageBean;
import com.freeme.freemelite.attendance.datapush.push.request.SinglePushRequestBody.NotificationBean;
import com.freeme.freemelite.attendance.datapush.push.request.SinglePushRequestBody.NotificationBean.StyleBean;
import com.freeme.freemelite.attendance.datapush.push.response.AuthTokenResponse;
import com.freeme.freemelite.attendance.datapush.push.response.PushResponse;
import com.freeme.freemelite.attendance.datapush.receive.service.GtPushServiceWrapper;
import com.freeme.freemelite.attendance.datapush.receive.service.PushDataReceiver;
import com.freeme.freemelite.attendance.router.datapush.GlobalConfig;
import com.freeme.freemelite.attendance.router.datapush.interfaces.IDataPush;
import com.freeme.freemelite.attendance.router.datapush.model.BaseDataPushRequestModel;
import com.freeme.freemelite.attendance.router.datapush.subject.DataPushSubject;
import com.freeme.freemelite.attendance.router.util.PreferencesUtil;
import com.freeme.freemelite.attendance.router.util.encrypt.ShaUtil;
import com.igexin.sdk.PushManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeTuiImpl implements IDataPush {
    private static final String TAG = "GeTuiImpl";
    private Context mContext;
    private String mClientId;

    @Override
    public void init(Application context) {
        mContext = context.getApplicationContext();
        PushManager.getInstance().initialize(context, GtPushServiceWrapper.class);
        PushManager.getInstance().registerPushIntentService(context, PushDataReceiver.class);
        mClientId = PushManager.getInstance().getClientid(context);
        PreferencesUtil.putString(context, GlobalConfig.SharepreFerenceConfig.PHONE_CLIENT_ID_KEY, mClientId);
        Log.e(TAG, ">>>>>>>>>>>>>>>init-ClientId:" + mClientId);

        getAuthToken();
    }

    @Override
    public void pushSingleData(BaseDataPushRequestModel basePushData) {
        DataPushSubject.getInstance().beginDataPush();
        SinglePushRequestBody singlePushRequestBody = new SinglePushRequestBody();
        singlePushRequestBody.setCid(basePushData.clientId);
        singlePushRequestBody.setRequestid(String.valueOf(System.currentTimeMillis()));
        MessageBean messageBean = new MessageBean();
        messageBean.setAppkey(DataPushConfig.Config.APP_KEY);
        messageBean.setIs_offline(true);
        messageBean.setMsgtype("notification");
        messageBean.setOffline_expire_time(10000000);
        singlePushRequestBody.setMessage(messageBean);
        NotificationBean notificationBean = new NotificationBean();
        StyleBean styleBean = new StyleBean();
        styleBean.setText(basePushData.message);
        styleBean.setTitle(basePushData.title);
        styleBean.setType(0);
        notificationBean.setStyle(styleBean);
        notificationBean.setTransmission_content("啥叫透传");
        notificationBean.setTransmission_type(true);
        singlePushRequestBody.setNotification(notificationBean);

        new Builder()
                .baseUrl(DataPushConfig.Url.PUSH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PushRequest.class)
                .singlePushRequest(DataPushConfig.Config.APP_ID, PreferencesUtil.getString(mContext, DataPushConfig.SharepreFerenceConfig.AUTH_TOKEN_KEY), singlePushRequestBody)
                .enqueue(new Callback<PushResponse>() {
                    @Override
                    public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {
                        if (response.isSuccessful()) {
                            PushResponse body = response.body();
                            DataPushSubject.getInstance().dataPushSuccessful();
                        } else {
                            DataPushSubject.getInstance().dataPushFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<PushResponse> call, Throwable t) {
                        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>singlePushRequest-onFailure:" + t.toString());
                        DataPushSubject.getInstance().dataPushFailed();
                    }
                });

    }

    @Override
    public void pushGroupData() {

    }

    @Override
    public void release() {
        Log.e(TAG, ">>>>>>>>>>>>release");
    }

    public void getAuthToken() {
        Builder builder = new Builder();
        Retrofit retrofit = builder.baseUrl(DataPushConfig.Url.PUSH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final PushRequest request = retrofit.create(PushRequest.class);
        AuthTokenRequestBody authTokenRequestBody = new AuthTokenRequestBody();
        authTokenRequestBody.appkey = DataPushConfig.Config.APP_KEY;
        authTokenRequestBody.timestamp = String.valueOf(System.currentTimeMillis());
        authTokenRequestBody.sign = ShaUtil.shaEncrypt(authTokenRequestBody.appkey + authTokenRequestBody.timestamp + DataPushConfig.Config.MASTER_SECRET, ShaUtil.SHA_256);
        request.authTokenRequest(DataPushConfig.Config.APP_ID, authTokenRequestBody).enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>authTokenRequest-onResponse:" + response.body().toString());
                if (response.isSuccessful()) {
                    AuthTokenResponse tokenResponse = response.body();
                    if (tokenResponse != null) {
                        PreferencesUtil.putString(mContext, DataPushConfig.SharepreFerenceConfig.AUTH_TOKEN_KEY, tokenResponse.getAuth_token());
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>authTokenRequest-onFailure:" + t.toString());
            }
        });
    }
}
