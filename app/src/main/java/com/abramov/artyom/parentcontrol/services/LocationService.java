package com.abramov.artyom.parentcontrol.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.abramov.artyom.parentcontrol.R;
import com.abramov.artyom.parentcontrol.interfaces.Constants;
import com.abramov.artyom.parentcontrol.services.sockets.client.SocketClientThread;
import com.abramov.artyom.parentcontrol.services.sockets.server.SocketServerThread;
import com.abramov.artyom.parentcontrol.utils.Logger;
import com.abramov.artyom.parentcontrol.utils.NetworkUtils;

public class LocationService extends Service {
    private AsyncTask mCurrentSocket;
    private static final String TAG = LocationService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            mCurrentSocket = startSocketClient();
        } else {
            mCurrentSocket = startSocketServer();
        }

        if (mCurrentSocket == null) {
            Logger.d(TAG, "Loc service wasn't start");
            stopSelf(startId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.d(TAG, "Current device ip is " + NetworkUtils.getIpAddress());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCurrentSocket = null;

        /*try {
            mCurrentSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Was exception after closing location service!");
        }*/
    }

    private AsyncTask startSocketServer() {
//        Logger.d(TAG, "Was exception after start server socket");
        return new SocketServerThread(this).execute();
    }

    private AsyncTask startSocketClient() {
//        Logger.d(TAG, "Was exception after start client socket");
        return new SocketClientThread().execute(NetworkUtils.getIpAddress(this), Constants.SOCKET_PORT);
    }
}
