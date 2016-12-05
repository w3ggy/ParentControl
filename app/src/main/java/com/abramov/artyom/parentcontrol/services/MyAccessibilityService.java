package com.abramov.artyom.parentcontrol.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.accessibilityservice.AccessibilityServiceInfoCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.EditText;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.domain.Browser;
import com.abramov.artyom.parentcontrol.ui.main_screen.MainActivity;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = MyAccessibilityService.class.getSimpleName();
    private boolean isWindowChanged;
    private Bundle mBundle;
    private View myView;
    private WindowManager wm;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.d(TAG, "Accessibility service created!");

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfoCompat.FEEDBACK_ALL_MASK;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        setServiceInfo(info);


//        startCheckingBrowser();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /*AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
        AccessibilityNodeInfoCompat source = record.getSource();*/
        int eventType = event.getEventType();
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

        AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
        AccessibilityNodeInfoCompat source2 = record.getSource();

        Log.d(TAG, String.format("current package is %s. Current activity is %s",
                event.getPackageName().toString(),
                event.getClassName().toString()));
        if (event.getSource() != null && event.getSource().getText() != null) {
            Log.d(TAG, "current text is : " + event.getSource().getText().toString());
            if (event.getSource().getText().toString().contains("Short description of the accessibility service purpose or behavior")) {
                onAlertClicked();
            }
        }

        switch (eventType) {
            /*case AccessibilityEventCompat.TYPE_VIEW_CLICKED:
                makeToast("Clicked for", source.getText());
                break;
            case AccessibilityEventCompat.TYPE_VIEW_FOCUSED: // check webview
                makeToast("FOCUSED FOR", source.getText());
                if (isWebView(source)) {
                    Log.d("TAG", "IT IS WEBVIEW!");
                }
                break;*/
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                makeToast("TEXT CHANGED FOR", source.getText());
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString());

                Browser browser = Browser.checkBrowser(componentName.getPackageName());
                if (browser != null) {
                    Log.d("CurrentActivity", "current browser is " + browser.getPackage());
                }

                /*if (!event.getPackageName().toString().equals(getPackageName())) {
                    makeSystemAlert();
                }*/ // TODO: DON'T TURN THIS ON

                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if (event.getClassName().equals("com.android.inputmethod.keyboard.Key")
                        && getString(R.string.enter).equals(event.getContentDescription())) {
                    List<AccessibilityNodeInfoCompat> list1 = source2.findAccessibilityNodeInfosByText(getString(R.string.test));
                    for (AccessibilityNodeInfoCompat node : list1) {
                        node.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
                    }
                }

//                makeToast("CONTENT_CHANGED", source.getText());
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED: // get url.
                if (isEditText(source) && source.getText() != null && source.getText().toString().contains("porno")) {

                    mBundle = new Bundle();
                    mBundle.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "ponies images");

                    source2.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT, mBundle);
                    source2.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://127.0.0.1"));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(browserIntent);*/

                    startBrowser();
                }

                makeToast("TYPE_VIEW_TEXT_SELECTION_CHANGED", event.getSource().getText());
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void makeToast(String tag, CharSequence msg) {
        if (msg == null) {
            msg = "";
        }
        Log.d(TAG, tag + msg);
//        Toast.makeText(this, tag + msg, Toast.LENGTH_SHORT).show();
        isWindowChanged = false;
    }

    private boolean isEditText(AccessibilityNodeInfo event) {
        if (event.getClassName().equals(EditText.class.getName())) {
            return true;
        }
        return false;
    }

    private boolean isWebView(AccessibilityNodeInfo event) {
        if (event.getClassName().equals(WebView.class.getName())) {
            return true;
        }
        return false;
    }

    private void startCheckingBrowser() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                /*while (true) {
                    Browser browser = Browser.checkBrowser(MyAccessibilityService.this);
                    *//*Toast.makeText(MyAccessibilityService.this, "Current browser is " +
                            (browser == null ? "null" : browser.getPackage()), Toast.LENGTH_SHORT)
                            .show();*//*
                    Log.d(TAG, "Current browser is " +
                            (browser == null ? "null" : browser.getPackage()));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
        thread.start();
    }

    private void makeSystemAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            makeAlert();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            makeAlert();
        }
    }

    private void makeAlert() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myView = inflater.inflate(R.layout.dialog_simple_system_alert, null);
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onAlertClicked();
                wm.removeView(myView);
                return true;
            }
        });

        wm.addView(myView, params);
    }

    private void onAlertClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startBrowser() {
        Browser browser = Browser.browsers.get(1);

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(1082130436);
        intent.addFlags(-4194305);
        intent.setDataAndType(Uri.parse("http://127.0.0.1"), "text/html");
        intent.setComponent(new ComponentName(browser.getPackage(), browser.getActivity()));
        intent.putExtra("com.android.browser.application_id", browser.getPackage());

        startActivity(intent);
    }
}
