package com.freeme.freemelite.attendance.machinehelper.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.EmployeeAddViewModel;
import com.freeme.freemelite.attendance.router.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EmployeeAddViewModel mEmployeeAddViewModel;
    private String mEmployeeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);
        mEmployeeAddViewModel = ViewModelProviders.of(this).get(EmployeeAddViewModel.class);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEmployeeAddViewModel.mEmployeeNameLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mEmployeeName = s;
            }
        });
    }

    private void initView() {
        TextInputEditText inputEditText = (TextInputEditText) findViewById(R.id.text_input_edit_text);
        inputEditText.addTextChangedListener(mEmployeeAddViewModel.bindTextWatcher());
        inputEditText.setCompoundDrawables(null, null, null, null);
        TextInputLayout inputEditLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        inputEditLayout.setHint("姓名");
        TextView pageTitle = (TextView) findViewById(R.id.tv_title);
        pageTitle.setText("添加员工");
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @OnClick(R2.id.bt_save_employee)
    void addEmployeeClick() {
        if (TextUtils.isEmpty(mEmployeeName)) {
            ToastUtil.show(this, "员工姓名不合法");
        } else {
            mEmployeeAddViewModel.saveEmployee(this, mEmployeeName);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        }
    }
}