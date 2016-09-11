package com.abramov.artyom.parentcontrol.services.sockets.client;

import com.abramov.artyom.parentcontrol.utils.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClientThread extends Thread {
    private static final String TAG = SocketClientThread.class.getSimpleName();
    private volatile boolean isRunning = true;
    private Socket mSocket;
    private String mResponse;
    private String mAddress;
    private int mPort;

    public SocketClientThread(String addr, int port) throws IOException {
        if (addr == null) {
            throw new IOException("Incorrect address");
        }

        mAddress = addr;
        mPort = port;
    }

    @Override
    public void run() {
        try {
            mSocket = new Socket(mAddress, mPort);

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

        Logger.d(TAG, "Response is : " + mResponse);
    }
}
