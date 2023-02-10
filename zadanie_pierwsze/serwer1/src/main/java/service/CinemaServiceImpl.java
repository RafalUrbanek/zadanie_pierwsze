package service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CinemaServiceImpl {

    private static ServerSocket server;

    public CinemaServiceImpl() throws Exception {
        server = new ServerSocket(8080);
        System.out.println("Server is online");
        generateNewUserConnection();
    }

    public void generateNewUserConnection() throws IOException {

        while (true) {
            try {
                Socket socket = server.accept();
                Thread thread = new Thread(()-> {
                    while(true){
                        try {
                            saveMovieToSchedule(socket);
                            break;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
                thread.start();
            } catch (Exception e){
                e.printStackTrace();

            }
        }
    }

    public void saveMovieToSchedule(Socket connection) throws IOException {
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        checkActiveThreads();
        while (true) {
            System.out.println("trying to read client msg");
            String clientInput = br.readLine();
            System.out.println("msg: " + clientInput);
            bw.write("server output");
            bw.newLine();
            bw.flush();
            if (clientInput.equals("0 - Logout")) {
                System.out.println("Connection to client closed");
                connection.close();
                break;
            }
        }
    }

    public void checkActiveThreads() {
        System.out.println("Active thread: " + Thread.activeCount());
    }
}
