package com.bak.libra26;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolBlockingServer {
    private static final int PORT = 12345;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws Exception {
        ThreadPoolBlockingServer threadPoolBlockingServer = new ThreadPoolBlockingServer();
        threadPoolBlockingServer.run();
    }

    private void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        /*
         * 블러킹되는 데이터 입출력 작업을 할당할 스레드 풀
         * 할당 가능한 스레드의 개수는 상수로 설정한 THREAD_POOL_SIZE에 따른다.
         */
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        while (true) {
            Socket socket = serverSocket.accept(); // 서버와 연결된 클라이언트의 소켓을 가져온다.
            executorService.submit(new ThreadPoolServerHandler(socket));
        }
    }

    /*
     * 기존의 BlockingServer가 담당하던 데이터 입출력 작업을 ThreadPoolServerHandler 라는 정적 클래스에게 위임하였다.
     *
     * >> 클라이언트가 해당 ThreadPoolBlockingServer에 연결되어 생성된 Socket 타입의 소켓 객체를ㄴ
     *    ThreadPoolServerHandler 의 생성자에 인자로 넣어 해당 소켓 객체를 통해 데이터 입출력을 진행한다.
     */
    private static class ThreadPoolServerHandler implements Runnable {
        private final Socket socket;

        public ThreadPoolServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                OutputStream os = socket.getOutputStream(); // server -> client socket stream
                InputStream is = socket.getInputStream();   // client -> server socket stream

                OutputStreamWriter osw = new OutputStreamWriter(os);
                InputStreamReader isr = new InputStreamReader(is);

                // Client로 부터 받은 데이터를 다시 클라이언트에게 전송한다.
                while (true) {
                    try {
                        int request = isr.read();
                        if (request == -1) break; // 클라이언트가 연결을 끊으면 종료

                        osw.append((char) request);
                        osw.flush();
                    } catch (IOException e) {
                        break;
                    }
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
