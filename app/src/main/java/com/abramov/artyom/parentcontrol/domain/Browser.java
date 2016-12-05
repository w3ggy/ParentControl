package com.abramov.artyom.parentcontrol.domain;

import java.util.ArrayList;
import java.util.List;

public class Browser {
    private String mPackage;
    private String mActivity;

    public static List<Browser> browsers;

    static {
        browsers = new ArrayList<>();
        browsers.add(new Browser("com.chrome.beta", "com.google.android.apps.chrome.Main"));
        browsers.add(new Browser("com.android.chrome", "com.google.android.apps.chrome.Main"));
    }


    /*public static Browser checkBrowser(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
                && context.checkCallingOrSelfPermission(Manifest.permission.GET_TASKS)
                == PackageManager.PERMISSION_GRANTED) {
            ComponentName componentName = ((ActivityManager.RunningTaskInfo)
                    ((ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE))
                            .getRunningTasks(1).get(0)).topActivity;
            for (Browser browser : browsers) {
                if (componentName.getPackageName().contains(browser.getPackage())) {
                    return browser;
                }
            }
        }
        return null;
    }*/

    public static Browser checkBrowser(String mPackage) {
        for (Browser browser : browsers) {
            if (mPackage.contains(browser.getPackage())) {
                return browser;
            }
        }

        return null;
    }

        public Browser(String sPackage, String activity) {
        mPackage = sPackage;
        mActivity = activity;
    }

    public String getPackage() {
        return mPackage;
    }

    public void setPackage(String sPackage) {
        this.mPackage = sPackage;
    }

    public String getActivity() {
        return mActivity;
    }

    public void setActivity(String activity) {
        this.mActivity = activity;
    }
}
