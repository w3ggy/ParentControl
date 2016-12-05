package com.abramov.artyom.parentcontrol.services.sockets.server;

import android.content.Context;
import android.os.AsyncTask;

import com.abramov.artyom.parentcontrol.domain.Call;
import com.abramov.artyom.parentcontrol.domain.DataObject;
import com.abramov.artyom.parentcontrol.domain.Loc;
import com.abramov.artyom.parentcontrol.domain.Sms;
import com.abramov.artyom.parentcontrol.model.BaseModel;
import com.abramov.artyom.parentcontrol.utils.DeviceUtils;
import com.abramov.artyom.parentcontrol.utils.Logger;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.abramov.artyom.parentcontrol.interfaces.Constants.SOCKET_PORT;

public class SocketServerThread extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SocketServerThread.class.getSimpleName();
    private ServerSocket mServerSocket;
    private Gson mGson;
    private BaseModel mBaseModel;
    private Context mContext;

    public SocketServerThread(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mGson = new Gson();
        mBaseModel = new BaseModel();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            mServerSocket = new ServerSocket(Integer.parseInt(SOCKET_PORT));
            Logger.d(TAG, "Socket has been created!");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cant create Server socket on the " + SOCKET_PORT);
        }

        sendReply();

        return null;
    }

    private void sendReply() {

        OutputStream outputStream;
        Socket socket;

        try {
            socket = mServerSocket.accept();
            readDataFromInputSocket(socket);
            Logger.d(TAG, "Data accepted by server");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cannot accept socket");
            return;
        }

        DataObject object = new DataObject(
                mBaseModel.getItems(Sms.class),
                mBaseModel.getItems(Call.class),
                mBaseModel.getItems(Loc.class));

        try {
            outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(mGson.toJson(object));
            printStream.close();
            Logger.d(TAG, "Reply was sent");

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cannot reply to the client");
        }
    }

    private void readDataFromInputSocket(Socket socket) {
        try {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            /*String result = "";
            if (in.ready()) {
                result = in.readLine();
            }

            if (result.equals(Call.class.getName())) {
                mCurrentDataClass = Call.class.getClass();
            } else if (result.equals(Loc.class.getName())) {
                mCurrentDataClass = Loc.class.getClass();
            } else if (result.equals(Sms.class.getName())) {
                mCurrentDataClass = Sms.class.getClass();
            }*/

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Error was occurred while reading input data from socket");
        }
    }
}
