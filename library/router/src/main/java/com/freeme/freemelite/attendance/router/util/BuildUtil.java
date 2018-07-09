package com.freeme.freemelite.attendance.router.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

/**
 * Get build platform inifo:
 * <p/>
 * 1. check  build platfrom os version;
 * 2. get build brand;
 */
public class BuildUtil {
    // Initialize in main project;
    public static boolean DEBUG = false;

    // Initialize in Application project;
    public static Mode BUILD_MODE = Mode.NONE;

    public enum Mode {
        NONE, PUBLIC_HW, PUBLIC_CN, CUSTOMER_CN, CUSTOMER_HW
    }

    /**
     * If客户版本
     */
    public static boolean isCustomerBuild() {
        return BUILD_MODE == Mode.CUSTOMER_CN || BUILD_MODE == Mode.CUSTOMER_HW;
    }

    /**
     * If客户版本
     */
    public static boolean isPublicBuild() {
        return BUILD_MODE == Mode.PUBLIC_CN || BUILD_MODE == Mode.PUBLIC_HW;
    }

    /**
     * If海外版本
     */
    public static boolean isHWBuild() {
        return BUILD_MODE == Mode.PUBLIC_HW || BUILD_MODE == Mode.CUSTOMER_HW;
    }

    /**
     * If国内版本
     */
    public static boolean isCNBuild() {
        return BUILD_MODE == Mode.PUBLIC_CN || BUILD_MODE == Mode.CUSTOMER_CN;
    }


    // TODO: use Build.VERSION_CODES when available
    public static final boolean ATLEAST_NOUGAT =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

    public static final boolean ATLEAST_MARSHMALLOW
            = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    public static final boolean ATLEAST_LOLLIPOP_MR1 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;

    public static final boolean ATLEAST_LOLLIPOP =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public static final boolean ATLEAST_KITKAT =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    public static final boolean ATLEAST_JB_MR1 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;

    public static final boolean ATLEAST_JB_MR2 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;


    private static int mTargetSdkVersion = -1;
    private static int mFreemeVersionCode;
    private static String mFreemeVersionName;

    /**
     * Get freemelite version code;
     */
    public static int getFreemeVersionCode(Context context) {
        if (mFreemeVersionCode == 0) {
            mFreemeVersionCode = PackageUtil.getPackageInfoForCfg(context).versionCode;
        }
        return mFreemeVersionCode;
    }

    /**
     * Get freemelite version name;
     */
    public static String getFreemeVersionName(Context context) {
        if (TextUtils.isEmpty(mFreemeVersionName)) {
            mFreemeVersionName = PackageUtil.getPackageInfoForCfg(context).versionName;
        }
        return mFreemeVersionName;
    }

    /**
     * The minimum SDK version this application targets.
     */
    public static int getTargetSdkVersion(Context context) {
        if (mTargetSdkVersion != -1) {
            return mTargetSdkVersion;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            mTargetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return mTargetSdkVersion;
    }

    public static String getCompileTime(Context context) {
        return CommonUtilities.getMetaString(context, "FREEME_BUILD_TIME");
    }

}
