package com.abramov.artyom.parentcontrol.utils;

import android.util.Log;

import static com.abramov.artyom.parentcontrol.interfaces.Constants.isLogging;

/**
 * Created by Artyom on 11.09.2016.
 */

public class Logger {
    public static void d(String tag, String msg) {
        if (isLogging) {
            Log.d(tag, msg);
        }
    }
}
