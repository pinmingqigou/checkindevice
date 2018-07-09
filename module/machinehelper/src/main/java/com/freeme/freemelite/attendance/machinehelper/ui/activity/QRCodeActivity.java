package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.model.WifiScanResultWrapper;
import com.freeme.freemelite.attendance.machinehelper.model.datapush.ServerDataForWifiConnected;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.QRCodeViewModel;
import com.freeme.freemelite.attendance.router.datapush.DataPushSelector;
import com.freeme.freemelite.attendance.router.datapush.model.BaseDataPushRequestModel;
import com.freeme.freemelite.attendance.router.util.PreferencesUtil;
import com.freeme.freemelite.attendance.zxingwrapper.activity.CodeUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRCodeActivity extends AppCompatActivity {
    private static final String TAG = "QRCodeActivity";

    @BindView(R2.id.iv_qrcode)
    ImageView mQRCodeImage;
    private QRCodeViewModel mQRCodeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        mQRCodeViewModel = ViewModelProviders.of(this).get(QRCodeViewModel.class);
        mQRCodeViewModel.bindLifecyclerObserverImpl(this, this);
        WifiScanResultWrapper wifiScanResultWrapper = (WifiScanResultWrapper) getIntent().getSerializableExtra(Config.IntentConfig.INTENT_SELECT_WIFI_KEY);
        final String message = mQRCodeViewModel.transformPhoneModel(this, wifiScanResultWrapper.getSsidSelected(), wifiScanResultWrapper.pw);
        Bitmap bitmap = CodeUtils.createImage(message, 400, 400, /*BitmapFactory.decodeResource(getResources(), R.drawable.logo)*/null);
        mQRCodeImage.setImageBitmap(bitmap);


        /*
        * 模拟签到设备连接wifi成功后推送消息通知手机端
        * */
        mQRCodeImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                ServerDataForWifiConnected serverDataForWifiConnected = new ServerDataForWifiConnected();
                serverDataForWifiConnected.wifiConnected = true;
                serverDataForWifiConnected.serverClientId = PreferencesUtil.getString(QRCodeActivity.this, "client_id_key");

                BaseDataPushRequestModel dataPushRequestModel = new BaseDataPushRequestModel();
                dataPushRequestModel.clientId = PreferencesUtil.getString(QRCodeActivity.this, "client_id_key");
                dataPushRequestModel.message = new Gson().toJson(serverDataForWifiConnected);
                dataPushRequestModel.title = "签到机返回";
                DataPushSelector.getInstance().pushSingleData(dataPushRequestModel);
            }
        }, 5000);

        mQRCodeViewModel.mServerDataForWifiConnectedLiveData.observe(this, new Observer<ServerDataForWifiConnected>() {
            @Override
            public void onChanged(@Nullable ServerDataForWifiConnected serverDataForWifiConnected) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>mServerDataForWifiConnectedLiveData-onChanged:" + serverDataForWifiConnected.wifiConnected);
                if (serverDataForWifiConnected != null) {
                    if (serverDataForWifiConnected.wifiConnected) {
                        mQRCodeViewModel.changeCheckInDeviceNetworkState(QRCodeActivity.this, true);
                        startMainActivity();
                    }
                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Config.IntentConfig.INTENT_WIFI_CONNECTTED_KEY, true);
        startActivity(intent);
    }
}
