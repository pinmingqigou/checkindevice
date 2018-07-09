package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.model.EmployeeModel;
import com.freeme.freemelite.attendance.machinehelper.ui.view.TitleItemDecoration;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.EmployeeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeManagementActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R2.id.rv_employee_name_list)
    RecyclerView mEmployeeNameListView;

    private EmployeeViewModel mEmployeeViewModel;
    private TitleItemDecoration mTitleItemDecoration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);
        mEmployeeViewModel = ViewModelProviders.of(this).get(EmployeeViewModel.class);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEmployeeViewModel.mEmployeeModelLiveData.observe(this, new Observer<List<EmployeeModel>>() {
            @Override
            public void onChanged(@Nullable List<EmployeeModel> employeeModels) {
                if (mTitleItemDecoration == null) {
                    mTitleItemDecoration = new TitleItemDecoration(EmployeeManagementActivity.this, employeeModels);
                }
                mEmployeeNameListView.removeItemDecoration(mTitleItemDecoration);
                mTitleItemDecoration.setData(employeeModels);
                mEmployeeNameListView.addItemDecoration(mTitleItemDecoration);
            }
        });
        mEmployeeViewModel.bindEmployeeObserver(getApplication(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        ImageView backView = (ImageView) findViewById(R.id.iv_back);
        TextView pageTitle = (TextView) findViewById(R.id.tv_title);
        TextView completedView = (TextView) findViewById(R.id.tv_completed);

        pageTitle.setText("参与考勤员工");
        completedView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        completedView.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEmployeeNameListView.setLayoutManager(linearLayoutManager);
        mEmployeeNameListView.setAdapter(mEmployeeViewModel.bindViewAdapter(this));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_completed) {
            startFaceManagementActivity();
        }
    }

    private void startFaceManagementActivity() {
        Intent intent = new Intent(this, FaceManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R2.id.add_employee_container)
    void addEmployeeClick() {
        startEmployeeAddActivity();
    }

    private void startEmployeeAddActivity() {
        Intent intent = new Intent(this, EmployeeAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
