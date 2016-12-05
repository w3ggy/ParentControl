package com.abramov.artyom.parentcontrol.services.sockets.client;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.abramov.artyom.parentcontrol.domain.Call;
import com.abramov.artyom.parentcontrol.domain.DataObject;
import com.abramov.artyom.parentcontrol.domain.Loc;
import com.abramov.artyom.parentcontrol.domain.Sms;
import com.abramov.artyom.parentcontrol.model.BaseModel;
import com.abramov.artyom.parentcontrol.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClientThread extends AsyncTask<String, Void, Void> {
    private static final String TAG = SocketClientThread.class.getSimpleName();
    private Socket mSocket;
    private StringBuilder mResponse = new StringBuilder("");
    private Gson mGson;
    private BaseModel mBaseModel;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mGson = new Gson();
        mBaseModel = new BaseModel();
    }

    @Override
    protected Void doInBackground(String... parametres) {
        if (parametres == null || parametres.length != 2) {
            return null;
        }
        try {
            mSocket = new Socket(parametres[0], Integer.parseInt(parametres[1]));

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            String tmp;
            InputStream inputStream = mSocket.getInputStream();
            /*
             * notice:
             * inputStream.read() will block if no data return
             */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                /*tmp = byteArrayOutputStream.toString("UTF-8");
                byteArrayOutputStream.reset();*/

                mResponse.append(byteArrayOutputStream.toString());

                byteArrayOutputStream.reset();

                /*if (!TextUtils.isEmpty(tmp)) {
                    mResponse.append(tmp.trim());
                }*/
            }

            Logger.d(TAG, "Reply was received");

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

        if (TextUtils.isEmpty(mResponse)) {
            return null;
        }

        DataObject object = mGson.fromJson(mResponse.toString(), DataObject.class);

        Logger.d(TAG, "Response is : " + object);

        saveData(object);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void saveData(DataObject object) {
        mBaseModel.saveItems(object.getSmsList());
        mBaseModel.saveItems(object.getCallList());
        mBaseModel.saveItem(object.getLocList().get(0));

    }
}
