package com.freeme.freemelite.attendance.machinehelper.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.model.EmployeeModel;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.EmployeeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {
    private static final String TAG = "EmployeeListAdapter";
    private List<EmployeeModel> mEmployeeList;
    private EmployeeViewModel mEmployeeViewModel;

    public EmployeeListAdapter(EmployeeViewModel employeeViewModel, LifecycleOwner lifecycleOwner) {
        mEmployeeViewModel = employeeViewModel;
        employeeViewModel.mEmployeeModelLiveData.observe(lifecycleOwner, new Observer<List<EmployeeModel>>() {
            @Override
            public void onChanged(@Nullable List<EmployeeModel> employeeModels) {
                mEmployeeList = employeeModels;
                EmployeeListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_layout, null);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EmployeeViewHolder holder, final int position) {
        if (mEmployeeList != null) {
            holder.mEmployeeNameView.setText(mEmployeeList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return mEmployeeList == null ? 0 : mEmployeeList.size();
    }


    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_employee_name)
        TextView mEmployeeNameView;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R2.id.tv_delete_employee)
        void deleteEmployee() {
            EmployeeModel employeeModel = EmployeeListAdapter.this.mEmployeeList.get(getLayoutPosition());
            EmployeeListAdapter.this.mEmployeeViewModel.deleteSpecifiedEmployee(mEmployeeNameView.getContext(), getLayoutPosition(), employeeModel.getId());
        }
    }
}
