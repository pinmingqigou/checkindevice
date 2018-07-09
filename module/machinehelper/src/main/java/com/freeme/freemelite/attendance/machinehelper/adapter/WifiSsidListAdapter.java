package com.freeme.freemelite.attendance.machinehelper.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.adapter.WifiSsidListAdapter.WifiSsidListViewHolder;
import com.freeme.freemelite.attendance.machinehelper.model.WifiScanResultWrapper;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.WifiInfoShareViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiSsidListAdapter extends Adapter<WifiSsidListViewHolder> {
    private static final String TAG = "WifiSsidListAdapter";
    private WifiInfoShareViewModel mViewModel;
    private List<String> mWifiItemWrappers;

    public WifiSsidListAdapter(WifiInfoShareViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.mViewModel = viewModel;
        viewModel.mSsidListLiveData.observe(lifecycleOwner, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> wifiItemWrappers) {
                mWifiItemWrappers = wifiItemWrappers;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public WifiSsidListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WifiSsidListViewHolder(View.inflate(parent.getContext(), R.layout.item_wifi_ssid_layout, null));
    }

    @Override
    public void onBindViewHolder(final WifiSsidListViewHolder holder, final int position) {
        if (mWifiItemWrappers != null) {
            holder.mWifiName.setText(mWifiItemWrappers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mWifiItemWrappers == null || mWifiItemWrappers.size() == 0 ? 5 : mWifiItemWrappers.size();
    }

    class WifiSsidListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_wifi_name)
        TextView mWifiName;
        @BindView(R2.id.iv_wifi_lock)
        ImageView mWifiLock;

        private WifiSsidListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R2.id.tv_wifi_name)
        void itemClick() {
            try {
                WifiScanResultWrapper wifiScanResultWrapper = new WifiScanResultWrapper();
                wifiScanResultWrapper.setSsidSelected(mWifiItemWrappers.get(getLayoutPosition()));
                mViewModel.mWifiScanResultLiveData.setValue(wifiScanResultWrapper);
                mViewModel.mWifiListItemClickLiveData.setValue(true);
            } catch (Exception e) {
                Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>item Click error:" + e);
            }

        }
    }
}
