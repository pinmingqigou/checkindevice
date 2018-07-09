package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.model.RegistrationFaceBean;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.FaceManagementViewModel;
import com.freeme.freemelite.attendance.router.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaceManagementActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R2.id.tv_user_number_no_entered)
    TextView mNoEnteredUserNumberView;
    @BindView(R2.id.tv_user_number_total)
    TextView mTotalUserNumberView;
    @BindView(R2.id.rv_face_list)
    RecyclerView mFaceListView;
    @BindView(R2.id.tv_face_edit)
    TextView mFaceEditView;


    private static final String TAG = "FaceManagementActivity";
    private FaceManagementViewModel mFaceManagementViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_management);
        ButterKnife.bind(this);
        mFaceManagementViewModel = ViewModelProviders.of(this).get(FaceManagementViewModel.class);
        mFaceManagementViewModel.bindContentObserverPartner(getApplication(), this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFaceManagementViewModel.mEmployeeNumberLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                mNoEnteredUserNumberView.setText(String.valueOf(integer));
            }
        });

        mFaceManagementViewModel.mFaceBeanListLiveData.observe(this, new Observer<List<RegistrationFaceBean>>() {
            @Override
            public void onChanged(@Nullable List<RegistrationFaceBean> registrationFaceBeans) {
                mTotalUserNumberView.setText(String.valueOf(registrationFaceBeans == null ? 0 : registrationFaceBeans.size()));
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int faceState = mFaceManagementViewModel.handleIntent(intent);
        if (faceState== Config.FaceState.REGISTERED){
            ToastUtil.show(this,"该脸部信息已经注册");
        }
    }

    private void initView() {
        ImageView backView = (ImageView) findViewById(R.id.iv_back);
        TextView pageTitleView = (TextView) findViewById(R.id.tv_title);
        ImageView addFaceView = (ImageView) findViewById(R.id.iv_add);
        pageTitleView.setText("人脸管理");
        addFaceView.setVisibility(View.VISIBLE);
        addFaceView.setOnClickListener(this);
        backView.setOnClickListener(this);

        mFaceListView.setLayoutManager(new GridLayoutManager(this, 4));
        mFaceListView.setAdapter(mFaceManagementViewModel.bindViewAdapter(this));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_add) {
            startFaceRegistrationActivity();
        }
    }

    private void startFaceRegistrationActivity() {
        Intent intent = new Intent(this, FaceRegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R2.id.tv_face_edit)
    void faceEditClick() {
        startEmployeeActivity();
    }



    private void startEmployeeActivity() {
        Intent intent = new Intent(this, EmployeeManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
