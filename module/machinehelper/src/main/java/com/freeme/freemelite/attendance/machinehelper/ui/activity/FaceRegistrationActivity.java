package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.FaceRegistrationViewModel;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.FaceRegistrationViewModel.FaceRegistrationObserver;
import com.freeme.freemelite.attendance.router.util.ToastUtil;
import com.joooonho.SelectableRoundedImageView;
import com.pingan.ai.face.result.PaFrameFaceDetectResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaceRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R2.id.camera_view)
    FrameLayout mCameraView;
    @BindView(R2.id.face_registration_container)
    CardView mFaceRegistrationContainer;
    @BindView(R2.id.iv_avatar)
    SelectableRoundedImageView mAvatarView;
    @BindView(R2.id.bt_recheck)
    Button mRecheckButton;
    @BindView(R2.id.bt_confirm_register)
    Button mConfirmRegisterButton;
    @BindView(R2.id.edit_text_user_name)
    TextInputEditText mNameInputEt;
    @BindView(R2.id.title_bar)
    FrameLayout mTitleBar;
    @BindView(R2.id.tv_face_state)
    TextView mFaceStateView;

    private static final String TAG = "FaceRegistrationA";
    private FaceRegistrationViewModel mFaceRegistrationViewModel;
    private FaceRegistrationObserver mFaceRegistrationObserver;
    private TextView mTitle;
    private ImageView mIvBack;
    private TextWatcher mTextWatcher;
    private String mUserName;
    private Bitmap mPhotoBitmap;
    private float[] mFaceFeature;
    private int mFaceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_registration);
        ButterKnife.bind(this);
        mFaceRegistrationViewModel = ViewModelProviders.of(this).get(FaceRegistrationViewModel.class);
        mFaceRegistrationObserver = mFaceRegistrationViewModel.bindFaceRegistrationObserver(this, getApplication());
        mTextWatcher = mFaceRegistrationViewModel.bindTextWatcher();
        initView();
    }

    private void initView() {
        mFaceRegistrationViewModel.bindCameraSurfaceHelper(this).initPreview(mCameraView, Camera.CameraInfo.CAMERA_FACING_FRONT, 0, 0);

        mTitleBar.setBackgroundColor(Color.BLACK);
        mTitle = mTitleBar.findViewById(R.id.tv_title);
        mIvBack = mTitleBar.findViewById(R.id.iv_back);
        mTitle.setText("人脸检测");
        mTitle.setTextColor(Color.WHITE);
        mIvBack.setImageResource(R.drawable.left_arrow_icon_white);
        mIvBack.setOnClickListener(this);

        mNameInputEt.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFaceRegistrationViewModel.mFrameFaceLiveData.observe(this, new Observer<PaFrameFaceDetectResult>() {
            @Override
            public void onChanged(@Nullable PaFrameFaceDetectResult paFrameFaceDetectResult) {
                if (mFaceState == Config.FaceState.UNREGISTERED) {
                    mFaceFeature = paFrameFaceDetectResult.feature;
                    mPhotoBitmap = mFaceRegistrationViewModel.HandleFaceScanData(paFrameFaceDetectResult);
                    mAvatarView.setImageBitmap(mPhotoBitmap);
                    mFaceRegistrationContainer.setVisibility(View.VISIBLE);
                }

            }
        });

        mFaceRegistrationViewModel.mUserNameLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mUserName = s;
            }
        });

        mFaceRegistrationViewModel.mFaceStateLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.e(TAG, ">>>>>>>>>>>>mFaceStateLiveData-onChanged:" + integer);
                mFaceState = integer == null ? -1 : integer;
                if (mFaceState == Config.FaceState.REGISTERED) {
                    mFaceStateView.setText("该人脸信息已注册");
                }
            }
        });

        mFaceRegistrationViewModel.mHasCameraPermissionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    ActivityCompat.requestPermissions(FaceRegistrationActivity.this, new String[]{Manifest.permission.CAMERA}, Config.PermissionRequest.CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R2.id.bt_recheck)
    void recheckButtonClick() {
        mFaceRegistrationContainer.setVisibility(View.GONE);
    }

    @OnClick(R2.id.bt_confirm_register)
    void confirmRegisterButtonClick() {
        if (TextUtils.isEmpty(mUserName)) {
            ToastUtil.show(this, "用户名非法");
        } else {
            mFaceRegistrationContainer.setVisibility(View.GONE);
            mFaceRegistrationViewModel.saveFaceRegistrationInfo(getApplicationContext(), mUserName, mPhotoBitmap, mFaceFeature);
            mFaceRegistrationViewModel.pushFaceInfoToCheckInDevice(getApplicationContext(), mUserName, mFaceFeature);
            startFaceManagementActivity();
        }
    }

    private void startFaceManagementActivity() {
        Intent intent = new Intent(this, FaceManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.PermissionRequest.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFaceRegistrationObserver.surfaceHelperOnResume();
            } else {
                ToastUtil.show(this, "权限请求被拒绝,当前页面功能无法正常使用");
                finish();
            }
        }
    }
}
