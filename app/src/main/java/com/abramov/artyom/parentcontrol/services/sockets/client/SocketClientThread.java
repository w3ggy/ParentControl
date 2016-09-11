package com.abramov.artyom.parentcontrol.services.sockets.client;

import android.os.AsyncTask;

import com.abramov.artyom.parentcontrol.domain.Location;
import com.abramov.artyom.parentcontrol.utils.Logger;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import io.realm.Realm;
import io.realm.RealmModel;

public class SocketClientThread extends AsyncTask<String, Void, Void> {
    private static final String TAG = SocketClientThread.class.getSimpleName();
    private Socket mSocket;
    private String mResponse;
    private Gson mGson;
    private Realm mRealm;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mGson = new Gson();
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected Void doInBackground(String... voids) {
        if (voids == null || voids.length != 2) {
            return null;
        }
        try {
            mSocket = new Socket(voids[0], Integer.parseInt(voids[1]));

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = mSocket.getInputStream();
            /*
             * notice:
             * inputStream.read() will block if no data return
             */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                mResponse += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            Logger.d(TAG, "UnknownHostException: " + e.toString());
        } catch (IOException e) {
            Logger.d(TAG, "IOException: " + e.toString());
        } finally {
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Object response = mGson.fromJson(mResponse, Location.class);

        saveData(mGson.fromJson(mResponse, Location.class));

        Logger.d(TAG, "Response is : " + response);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mRealm.close();
    }

    private <T extends RealmModel> void saveData(T object) {
        mRealm.copyToRealm(object);
    }
}
