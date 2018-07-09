package com.freeme.freemelite.attendance.router;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import com.freeme.freemelite.attendance.router.util.DisplayUtil;

public class Partner {
    private static final String TAG = "Partner";
    public static final String CUSTOM_PACKAGE = "com.freeme.freemelite.attendance.custom";
    private static Resources sPartnerRes;
    private static String sPartnerPkgName;
    private static Context sPartnerContext;

    // 功能开关；


    /**
     * 客户版本 ： com.freeme.freemelite.odm.custom 存在则取其中配置，不存在则应用内配置生效
     * 公开版本 ： 只取应用内配置；
     */
    private static synchronized void initalize(Context ctx) {
        if (sPartnerRes == null) {
            Context context = ctx.getApplicationContext();
            // 如果是客户版本，判断配置apk是否存在；读取配置
            PackageManager pm = context.getPackageManager();

            if (sPartnerRes == null) {
                try {
                    sPartnerPkgName = CUSTOM_PACKAGE;
                    sPartnerRes = pm.getResourcesForApplication(CUSTOM_PACKAGE);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "Failed to find resources for " + CUSTOM_PACKAGE);
                }
            }

            // 客户配置apk不存在，读取自身配置；
            if (sPartnerRes == null) {
                sPartnerRes = context.getResources();
                sPartnerPkgName = context.getPackageName();
            }
        }
    }

    public static String getPackageName(Context context) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        return sPartnerPkgName;
    }

    public static Resources getResources(Context context) {
        if (sPartnerRes == null) {
            initalize(context);
        }

        return sPartnerRes;
    }

    public static Context getPartnerContext(Context context) {
        if (sPartnerContext == null) {
            if (sPartnerContext == null) {
                try {
                    sPartnerContext = context.createPackageContext(Partner.CUSTOM_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
                } catch (PackageManager.NameNotFoundException ex) {
                    Log.e(TAG, "==================getPartnerContext error:" + ex);
                }
            }
        }
        return sPartnerContext;
    }

    public static int getInteger(Context context, String key) {
        return getInteger(context, key, 0);
    }

    public static int getInteger(Context context, String key, int def) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int resId = sPartnerRes.getIdentifier(key, "integer",
                sPartnerPkgName);

        return (resId != 0) ? sPartnerRes.getInteger(resId) : def;
    }


    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean def) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int resId = sPartnerRes.getIdentifier(key, "bool",
                sPartnerPkgName);

        return (resId != 0) ? sPartnerRes.getBoolean(resId) : def;
    }

    public static String getString(Context context, String key) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int resId = sPartnerRes.getIdentifier(key, "string",
                sPartnerPkgName);

        return (resId != 0) ? sPartnerRes.getString(resId) : "";
    }

    public static String[] getStringArray(Context context, String key) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int resId = sPartnerRes.getIdentifier(key, "array",
                sPartnerPkgName);

        return (resId != 0) ? sPartnerRes.getStringArray(resId) : null;
    }

    public static int getColor(Context context, String key, int def) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int resId = sPartnerRes.getIdentifier(key, "color",
                sPartnerPkgName);

        return (resId != 0) ? sPartnerRes.getColor(resId) : def;
    }


    public static int getDimensionDpSize(Context context, String key) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int resId = sPartnerRes.getIdentifier(key, "dimen",
                sPartnerPkgName);

        int dimPx = (resId != 0) ? sPartnerRes.getDimensionPixelSize(resId) : 0;
        return DisplayUtil.px2dip(context, dimPx);
    }


    public static int getXmlResId(Context context, String key) {
        if (sPartnerRes == null) {
            initalize(context);
        }
        int defaultLayout = sPartnerRes.getIdentifier(key,
                "xml", sPartnerPkgName);
        return defaultLayout;
    }


}
