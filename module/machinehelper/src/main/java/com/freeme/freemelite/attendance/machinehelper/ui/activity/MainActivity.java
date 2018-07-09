package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R2.id.bt_share_wifi)
    Button mShareWifiButton;
    @BindView(R2.id.face_button_container)
    LinearLayout mFaceButtonContainer;
    @BindView(R2.id.bt_face_registration)
    Button mFaceRegistrationButton;
    @BindView(R2.id.bt_face_management)
    Button mFaceManagementButton;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.bindLifecyclerObserver(this, this);
        mViewModel.mCheckInDeviceNetworkConnectedLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null || !aBoolean) {
                    showWifiShareButton();
                } else {
                    showFaceButtonContainer();
                }
            }
        });
    }

    @OnClick(R2.id.bt_share_wifi)
    void shareWifiButtonClick() {
        Log.e(TAG, ">>>>>>>>>>>>shareWifiButtonClick");
        Intent intent = new Intent(this, WifiInfoShareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R2.id.bt_face_registration)
    void faceRegistrationButtonClick() {
        Log.e(TAG, ">>>>>>>>>>>>>faceRegistrationButtonClick");
        Intent intent = new Intent(this, FaceRegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R2.id.bt_face_management)
    void faceManagementButtonClick() {
        Log.e(TAG, ">>>>>>>>>>>>>faceManagementButtonClick");
        Intent intent = new Intent(this, FaceManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, ">>>>>>>>>>>>>>>>onNewIntent");
        if (intent.getBooleanExtra(Config.IntentConfig.INTENT_WIFI_CONNECTTED_KEY, false)) {
            showFaceButtonContainer();
        }
    }

    public void showWifiShareButton() {
        mShareWifiButton.setVisibility(View.VISIBLE);
        mFaceButtonContainer.setVisibility(View.GONE);
    }

    public void showFaceButtonContainer() {
        mShareWifiButton.setVisibility(View.GONE);
        mFaceButtonContainer.setVisibility(View.VISIBLE);
    }
}
