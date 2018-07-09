package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.freeme.freemelite.attendance.machinehelper.dao.FaceProviderPartner;

public class EmployeeAddViewModel extends ViewModel {
    public MutableLiveData<String> mEmployeeNameLiveData = new MutableLiveData<>();


    public InputEmployeeNameObserver bindTextWatcher() {
        return new InputEmployeeNameObserver();
    }

    public void saveEmployee(Context context, String employeeName) {
        FaceProviderPartner.insertRegistrationFace(context, employeeName, null, null);
    }


    class InputEmployeeNameObserver implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String employeeName = s.toString().trim();
            mEmployeeNameLiveData.setValue(employeeName);
        }
    }
}
