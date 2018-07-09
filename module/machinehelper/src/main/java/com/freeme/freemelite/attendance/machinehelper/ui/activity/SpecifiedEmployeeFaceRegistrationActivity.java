package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.SpecifiedEmployeeFaceRegistrationViewModel;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.SpecifiedEmployeeFaceRegistrationViewModel.LifecycleObserverImpl;
import com.freeme.freemelite.attendance.router.util.ToastUtil;
import com.pingan.ai.face.result.PaFrameFaceDetectResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecifiedEmployeeFaceRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R2.id.photo_container)
    FrameLayout mCircleFrameLayout;


    private SpecifiedEmployeeFaceRegistrationViewModel mViewModel;
    private String mEmployeeName;
    private LifecycleObserverImpl mLifecycleObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specified_employee_face_registration);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(SpecifiedEmployeeFaceRegistrationViewModel.class);
        mLifecycleObserver = mViewModel.bindLifecycleObserver(this, getApplication());
        mEmployeeName = mViewModel.handleIntent(getIntent());
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.mFaceDetectResultLiveData.observe(this, new Observer<PaFrameFaceDetectResult>() {
            @Override
            public void onChanged(@Nullable PaFrameFaceDetectResult paFrameFaceDetectResult) {
                mViewModel.updateSpecifiedEmployeeFaceInfo(SpecifiedEmployeeFaceRegistrationActivity.this, paFrameFaceDetectResult);
                mViewModel.pushFaceInfoToCheckInDevice(SpecifiedEmployeeFaceRegistrationActivity.this, mEmployeeName, paFrameFaceDetectResult.feature);
                finish();
            }
        });

        mViewModel.mFaceStateLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == Config.FaceState.REGISTERED) {
                    Intent intent = new Intent(SpecifiedEmployeeFaceRegistrationActivity.this, FaceManagementActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Config.IntentConfig.INTENT_SPECIFIED_EMPLOYEE_FACE_STATE_KEY, Config.FaceState.REGISTERED);
                    startActivity(intent);
                }
            }
        });

        mViewModel.mHasCametaPermissionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    ActivityCompat.requestPermissions(SpecifiedEmployeeFaceRegistrationActivity.this, new String[]{Manifest.permission.CAMERA}, Config.PermissionRequest.CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }

    private void initView() {
        mViewModel.bindCameraSurfaceHelper(this).initPreview(mCircleFrameLayout, Camera.CameraInfo.CAMERA_FACING_FRONT, (int) getResources().getDimension(R.dimen.specified_employee_face_registration_layout_size), (int) getResources().getDimension(R.dimen.specified_employee_face_registration_layout_size));

        TextView pageTitle = (TextView) findViewById(R.id.tv_title);
        pageTitle.setText(mEmployeeName);
        findViewById(R.id.iv_back).setOnClickListener(this);
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
                mLifecycleObserver.surfaceHelperOnResume();
            } else {
                ToastUtil.show(this, "权限请求被拒绝,当前页面功能无法正常使用");
                finish();
            }
        }
    }
}
