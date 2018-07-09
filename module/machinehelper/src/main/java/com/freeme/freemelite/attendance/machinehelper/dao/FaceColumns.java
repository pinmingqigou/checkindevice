package com.freeme.freemelite.attendance.machinehelper.dao;

import android.net.Uri;
import android.provider.BaseColumns;

public class FaceColumns implements BaseColumns {
    public static final Uri FACE_INFO_TABLE_URL = Uri.parse("content://com.freeme.freemelite.attendance/" + FaceProvider.FACE_INFO_TABLE);

    /*
    * 以下为人脸注册表
    * */
    //照片信息
    public static final String COLUMN_PHOTO = "photo";

    //姓名信息
    public static final String COLUMN_USER_NAME = "name";

    //人脸特征
    public static final String COLUMN_FACE_FEATURE = "feature";

    //此条信息是否最近更新，０代表否，１代表是
    public static final String COLUMN_RECENT_FLAG = "recent";

}
