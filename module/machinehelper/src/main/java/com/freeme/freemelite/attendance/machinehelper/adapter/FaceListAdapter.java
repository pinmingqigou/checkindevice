package com.freeme.freemelite.attendance.machinehelper.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.R;
import com.freeme.freemelite.attendance.machinehelper.R2;
import com.freeme.freemelite.attendance.machinehelper.model.RegistrationFaceBean;
import com.freeme.freemelite.attendance.machinehelper.ui.activity.SpecifiedEmployeeFaceRegistrationActivity;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.FaceManagementViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaceListAdapter extends RecyclerView.Adapter<FaceListAdapter.FaceListViewHolder> {
    private FaceManagementViewModel mFaceManagementViewModel;
    private LifecycleOwner mLifecycleOwner;
    private List<RegistrationFaceBean> mFaceData;

    public FaceListAdapter(FaceManagementViewModel viewModel, LifecycleOwner lifecycleOwner) {
        mFaceManagementViewModel = viewModel;
        mLifecycleOwner = lifecycleOwner;
        mFaceManagementViewModel.mFaceBeanListLiveData.observe(mLifecycleOwner, new Observer<List<RegistrationFaceBean>>() {
            @Override
            public void onChanged(@Nullable List<RegistrationFaceBean> registrationFaceBeans) {
                mFaceData = registrationFaceBeans;
                FaceListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public FaceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout itemView = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_face_layout, null);
        return new FaceListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FaceListViewHolder holder, final int position) {
        if (mFaceData != null) {
            RegistrationFaceBean faceBean = mFaceData.get(position);
            if (faceBean.photo == null) {
                holder.mUserPhotoView.setImageResource(R.drawable.face_photo_default);
            } else {
                holder.mUserPhotoView.setImageBitmap(faceBean.photo);
            }
            holder.mUserNameView.setText(faceBean.name);
        }
    }

    @Override
    public int getItemCount() {
        return mFaceData == null ? 0 : mFaceData.size();
    }

    class FaceListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_user_photo)
        ImageView mUserPhotoView;
        @BindView(R2.id.tv_user_name)
        TextView mUserNameView;

        public FaceListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R2.id.iv_user_photo)
        void photoViewClick() {
            RegistrationFaceBean registrationFaceBean = mFaceData.get(getLayoutPosition());
            if (registrationFaceBean.photo == null) {
                Intent intent = new Intent(mUserNameView.getContext(), SpecifiedEmployeeFaceRegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Config.IntentConfig.INTENT_SPECIFIED_EMPLOYEE_NAME_KEY, registrationFaceBean.name);
                intent.putExtra(Config.IntentConfig.INTENT_SPECIFIED_EMPLOYEE_ID_KEY, registrationFaceBean.id);
                mUserNameView.getContext().startActivity(intent);
            }
        }
    }
}
