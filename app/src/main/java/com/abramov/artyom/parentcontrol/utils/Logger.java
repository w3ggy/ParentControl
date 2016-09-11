package com.abramov.artyom.parentcontrol.utils;

import android.util.Log;

import static com.abramov.artyom.parentcontrol.interfaces.Constants.isLogging;


public class Logger {
    public static void d(String tag, String msg) {
        if (isLogging) {
            Log.d(tag, msg);
        }
    }
}
