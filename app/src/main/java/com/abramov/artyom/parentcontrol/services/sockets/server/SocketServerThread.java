package com.abramov.artyom.parentcontrol.services.sockets.server;

import android.content.Context;
import android.os.AsyncTask;

import com.abramov.artyom.parentcontrol.domain.Location;
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
    private Context mContext;

    public SocketServerThread(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mGson = new Gson();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            mServerSocket = new ServerSocket(Integer.parseInt(SOCKET_PORT));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cant create Server socket on the " + SOCKET_PORT);
        }

        sendReply();

        return null;
    }

    private void sendReply() {
        Location location = new Location("test marker",
                DeviceUtils.getDeviceId(mContext),
                54.357971,
                48.379740);

        OutputStream outputStream;
        Socket socket;

        try {
            socket = mServerSocket.accept();
            readDataFromInputSocket(socket);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cannot accept socket");
            return;
        }

        try {
            outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(mGson.toJson(location));
            printStream.close();
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


        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Error was occurred while reading input data from socket");
        }
    }
}
