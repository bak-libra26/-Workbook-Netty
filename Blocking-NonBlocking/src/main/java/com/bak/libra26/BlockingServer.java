package com.bak.libra26;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {
    public static void main(String[] args) throws Exception {
        BlockingServer blockingServer = new BlockingServer();
        blockingServer.run();
    }


    private void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket();

        while (true) {
            Socket socket = serverSocket.accept(); // 서버와 연결된 클라이언트의 소켓을 가져온다.

            OutputStream os = socket.getOutputStream(); // server -> client socket stream
            InputStream is = socket.getInputStream();   // client -> server socket stream

            OutputStreamWriter osw = new OutputStreamWriter(os);
            InputStreamReader isr = new InputStreamReader(is);

            /*
             * Client로 부터 받은 데이터를 다시 클라이언트에게 전송한다.
             */
            while (true) {
                try {
                    int request = isr.read();
                    osw.write((char) request);
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}
