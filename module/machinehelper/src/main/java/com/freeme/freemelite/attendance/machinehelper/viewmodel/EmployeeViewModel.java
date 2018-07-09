package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.freeme.freemelite.attendance.machinehelper.EmployeeComparator;
import com.freeme.freemelite.attendance.machinehelper.adapter.EmployeeListAdapter;
import com.freeme.freemelite.attendance.machinehelper.dao.BaseContentObserverPartner;
import com.freeme.freemelite.attendance.machinehelper.dao.FaceProviderPartner;
import com.freeme.freemelite.attendance.machinehelper.model.EmployeeModel;
import com.freeme.freemelite.attendance.machinehelper.model.RegistrationFaceBean;
import com.freeme.freemelite.attendance.machinehelper.utils.PinyinUtils;
import com.freeme.freemelite.attendance.router.AsyncHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeViewModel extends BaseViewModel {
    private static final String TAG = "EmployeeViewModel";
    public MutableLiveData<List<EmployeeModel>> mEmployeeModelLiveData = new MutableLiveData<>();
    private int mDeletePosition = -1;
    private Application mContext;

    public EmployeeListAdapter bindViewAdapter(LifecycleOwner lifecycleOwner) {
        return new EmployeeListAdapter(this, lifecycleOwner);
    }

    public void bindEmployeeObserver(Application application, LifecycleOwner lifecycleOwner) {
        mContext = application;
        new EmployeeObserver(this, lifecycleOwner, application);
    }

    private void queryEmployee(final Context context, final EmployeeComparator pinyinComparator) {
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                List<EmployeeModel> employeeList = new ArrayList<>();
                List<RegistrationFaceBean> registrationFaceBeans = FaceProviderPartner.queryAllRegistrationFaces(context);
                for (RegistrationFaceBean registrationFaceBean : registrationFaceBeans) {
                    String pinyin = PinyinUtils.getPingYin(registrationFaceBean.name);
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    EmployeeModel employeeModel;
                    if (sortString.matches("[A-Z]")) {
                        employeeModel = new EmployeeModel(registrationFaceBean.name, registrationFaceBean.feature, sortString.toUpperCase(), registrationFaceBean.id);
                    } else {
                        employeeModel = new EmployeeModel(registrationFaceBean.name, registrationFaceBean.feature, "#", registrationFaceBean.id);
                    }
                    employeeList.add(employeeModel);
                    Collections.sort(employeeList, pinyinComparator);
                }
                mEmployeeModelLiveData.postValue(employeeList);
            }
        });
    }

    public void deleteSpecifiedEmployee(Context context, int position, int id) {
        mDeletePosition = position;
        int delete = FaceProviderPartner.deleteSpecifiedFace(context, id);
    }

    @Override
    public void contentObserverOnchange() {
        List<EmployeeModel> value = mEmployeeModelLiveData.getValue();
        if (value != null) {
            if (mDeletePosition != -1) {
                value.remove(mDeletePosition);
                mEmployeeModelLiveData.postValue(value);
                mDeletePosition = -1;
            } else {
                EmployeeComparator pinyinComparator = new EmployeeComparator();
                queryEmployee(mContext, pinyinComparator);
            }
        }
    }

    class EmployeeObserver extends BaseContentObserverPartner {
        public EmployeeObserver(BaseViewModel baseViewModel, LifecycleOwner lifecycleOwner, Application context) {
            super(baseViewModel, lifecycleOwner, context);
            EmployeeComparator pinyinComparator = new EmployeeComparator();
            EmployeeViewModel.this.queryEmployee(context, pinyinComparator);
        }
    }
}
