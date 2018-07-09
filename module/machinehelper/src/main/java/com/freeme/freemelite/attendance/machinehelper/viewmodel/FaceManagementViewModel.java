package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.FaceComparator;
import com.freeme.freemelite.attendance.machinehelper.adapter.FaceListAdapter;
import com.freeme.freemelite.attendance.machinehelper.dao.BaseContentObserverPartner;
import com.freeme.freemelite.attendance.machinehelper.dao.FaceProviderPartner;
import com.freeme.freemelite.attendance.machinehelper.model.RegistrationFaceBean;
import com.freeme.freemelite.attendance.router.AsyncHandler;

import java.util.Collections;
import java.util.List;

public class FaceManagementViewModel extends BaseViewModel {
    private static final String TAG = "FaceManagementViewModel";
    private Application mContext;

    public MutableLiveData<List<RegistrationFaceBean>> mFaceBeanListLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> mEmployeeNumberLiveData = new MutableLiveData<>();
    private final FaceComparator mFaceComparator;

    public FaceManagementViewModel() {
        mFaceComparator = new FaceComparator();
    }

    public void bindContentObserverPartner(Application context, LifecycleOwner lifecycleOwner) {
        mContext = context;
        new ContentObserverPartner(this, lifecycleOwner, context);
    }

    public FaceListAdapter bindViewAdapter(LifecycleOwner lifecycleOwner) {
        return new FaceListAdapter(this, lifecycleOwner);
    }

    public int handleIntent(Intent intent){
        return intent.getIntExtra(Config.IntentConfig.INTENT_SPECIFIED_EMPLOYEE_FACE_STATE_KEY,-1);
    }

    private void getAllRegistrationFace(final Context context) {
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                List<RegistrationFaceBean> registrationFaceBeans = FaceProviderPartner.queryAllRegistrationFaces(context);
                int notEnteredEmployee = 0;
                for (RegistrationFaceBean registrationFaceBean : registrationFaceBeans) {
                    if (registrationFaceBean.photo == null) {
                        notEnteredEmployee += 1;
                    }
                }
                Collections.sort(registrationFaceBeans, mFaceComparator);
                mEmployeeNumberLiveData.postValue(notEnteredEmployee);
                mFaceBeanListLiveData.postValue(registrationFaceBeans);
            }
        });
    }

    @Override
    public void contentObserverOnchange() {
        Log.e(TAG, ">>>>>>>>>>>>>>>contentObserverOnchange:");
        getAllRegistrationFace(mContext);
    }

    class ContentObserverPartner extends BaseContentObserverPartner {
        private Application mContext;

        public ContentObserverPartner(BaseViewModel baseViewModel, LifecycleOwner lifecycleOwner, Application context) {
            super(baseViewModel, lifecycleOwner, context);
            mContext = context;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void getAllRegistrationFace() {
            FaceManagementViewModel.this.getAllRegistrationFace(mContext);
        }
    }
}
