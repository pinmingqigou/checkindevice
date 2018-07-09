package com.freeme.freemelite.attendance.machinehelper;

import com.freeme.freemelite.attendance.machinehelper.model.RegistrationFaceBean;
import com.freeme.freemelite.attendance.machinehelper.utils.PinyinUtils;

import java.util.Comparator;

public class FaceComparator implements Comparator<RegistrationFaceBean> {
    @Override
    public int compare(RegistrationFaceBean o1, RegistrationFaceBean o2) {
        if (o1.photo==null){
            return -1;
        }else if (o2.photo==null){
            return 1;
        }else {
            String o1Py = PinyinUtils.getPingYin(o1.name);
            String o2Py = PinyinUtils.getPingYin(o2.name);
            String o1FirstSpell = o1Py.substring(0, 1).toUpperCase();
            String o2FirstSpell = o2Py.substring(0, 1).toUpperCase();
            return o1FirstSpell.compareTo(o2FirstSpell);
        }
    }
}
