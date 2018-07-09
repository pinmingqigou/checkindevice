package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.model.WifiScanResultWrapper;
import com.freeme.freemelite.attendance.machinehelper.ui.view.ItemDecoration;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.WifiInfoShareViewModel;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.WifiInfoShareViewModel.WifiReceiver;
import com.freeme.freemelite.attendance.router.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiInfoShareActivity extends AppCompatActivity {

    @BindView(R2.id.edit_text_network)
    TextInputLayout mNetworkTextInputLayout;
    @BindView(R2.id.edit_text_pw)
    TextInputLayout mPwTextInputLayout;
    @BindView(R2.id.network_info_bt_confirm)
    Button mConfirmButton;
    @BindView(R2.id.rv_ssid_list)
    RecyclerView mRecyclerView;
    @BindView(R2.id.wifi_name_list_container)
    LinearLayout mWifiNameListContainer;
    @BindView(R2.id.tv_cancel_select_wifi_name)
    TextView mCancleSelcet;

    private static final String TAG = "WifiInfoShareActivity";
    private WifiInfoShareViewModel mWifiInfoShareViewModel;
    private WifiReceiver mWifiReceiver;
    private EditText mPwEdittext;
    private EditText mNetworkEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info_share);
        ButterKnife.bind(this);
        mWifiInfoShareViewModel = ViewModelProviders.of(this).get(WifiInfoShareViewModel.class);
        initView();

        mWifiReceiver = mWifiInfoShareViewModel.bindWifiReceiver(this, getApplication());

        mWifiInfoShareViewModel.mWifiScanResultLiveData.observe(this, new Observer<WifiScanResultWrapper>() {
            @Override
            public void onChanged(@Nullable WifiScanResultWrapper wifiScanResultWrapper) {
                String ssidSelected = wifiScanResultWrapper.getSsidSelected();
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>WifiScanResultLiveData-onChanged:" + ssidSelected + "/" + wifiScanResultWrapper.pw);
                if (!TextUtils.isEmpty(ssidSelected)) {
                    mNetworkEdittext.setText(ssidSelected);
                }
                if (wifiScanResultWrapper.scanResults != null) {
                    mWifiInfoShareViewModel.dealWithScanResult(wifiScanResultWrapper);
                }
                if (!TextUtils.isEmpty(mNetworkEdittext.getText().toString().trim()) && !TextUtils.isEmpty(mPwEdittext.getText().toString().trim())) {
                    mConfirmButton.setEnabled(true);
                } else {
                    mConfirmButton.setEnabled(false);
                }
            }
        });

        mWifiInfoShareViewModel.mWifiListItemClickLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean itemClick) {
                if (itemClick != null && itemClick) {
                    mWifiNameListContainer.setVisibility(View.GONE);
                    mWifiInfoShareViewModel.mWifiListItemClickLiveData.setValue(false);
                }
            }
        });
    }

    private void initView() {
        mPwEdittext = mPwTextInputLayout.findViewById(R.id.text_input_edit_text);
        mNetworkEdittext = mNetworkTextInputLayout.findViewById(R.id.text_input_edit_text);
        mPwEdittext.addTextChangedListener(mWifiInfoShareViewModel.getTextWatcher(WifiInfoShareViewModel.EDIT_TEXT_PW));
        mPwEdittext.setCompoundDrawables(null, null, null, null);
        mNetworkTextInputLayout.setHint("网络");
        mPwTextInputLayout.setHint("密码");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new ItemDecoration(this, ItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mWifiInfoShareViewModel.bindViewAdapter(this));

        mNetworkEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mWifiReceiver.scanWifiResult(WifiInfoShareActivity.this);
                    mWifiNameListContainer.setVisibility(View.VISIBLE);
                    mNetworkEdittext.clearFocus();
                }
            }
        });

        mNetworkEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiReceiver.scanWifiResult(WifiInfoShareActivity.this);
                mWifiNameListContainer.setVisibility(View.VISIBLE);
                mNetworkEdittext.clearFocus();
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R2.id.network_info_bt_confirm)
    void networkConfirmButtonClick() {
        startQRCodeActivity();
    }

    private void startQRCodeActivity() {
        Intent intent = new Intent(this, QRCodeActivity.class);
        intent.putExtra(Config.IntentConfig.INTENT_SELECT_WIFI_KEY, mWifiInfoShareViewModel.mWifiScanResultLiveData.getValue());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R2.id.tv_cancel_select_wifi_name)
    void wifiNameSelectCancelClick() {
        mWifiNameListContainer.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.PermissionRequest.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mWifiReceiver.scanWifiResult(this);
            } else {
                ToastUtil.show(this, "权限请求被拒绝");
                mWifiNameListContainer.setVisibility(View.GONE);
            }
        }
    }
}
