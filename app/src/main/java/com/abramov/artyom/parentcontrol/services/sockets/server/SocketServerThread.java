package com.abramov.artyom.parentcontrol.services.sockets.server;

import com.abramov.artyom.parentcontrol.utils.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.abramov.artyom.parentcontrol.interfaces.Constants.SOCKET_PORT;

public class SocketServerThread extends Thread {
    private static final String TAG = SocketServerThread.class.getSimpleName();
    private ServerSocket mServerSocket;

    public SocketServerThread() throws IOException {
        super();

        mServerSocket = new ServerSocket(SOCKET_PORT);
    }

    @Override
    public void run() {
        try {
            Socket socket = mServerSocket.accept();
            sendReply(socket, "test");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cannot starting reply to the client");
        }
    }

    private void sendReply(Socket socket, String message) {
        OutputStream outputStream;

        try {
            outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(message);
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG, "Cannot reply to the client");
        }
    }
}
