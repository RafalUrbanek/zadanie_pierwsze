package impl;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static utils.Messages.*;

public class CommunicationImpl {

    private Socket socketClient;

    private BufferedWriter bw = null;

    public CommunicationImpl() throws IOException {
        informationMenu();
    }

    public void sendMessage(String message) {
        try {
            if (socketClient == null) {
                bw = connectionStart();
            }
            System.out.println("trying to send msg to server");
            bw.write(message);
            bw.newLine();
            bw.flush();
            System.out.println("msg sent");

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void receiveMessage() throws IOException {
        InputStream is = socketClient.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String serverMessage;

        try {
            if (socketClient.isConnected()) {
                System.out.println("try to read server msg");
                serverMessage = br.readLine();
                System.out.println(serverMessage);
            } else {
                System.out.println("no socket Client connected");
            }
        } catch (Exception e) {
            socketClient.close();
        }
    }

    private BufferedWriter connectionStart() throws IOException {
        OutputStream os;
        OutputStreamWriter osw;

        socketClient = new Socket(IP, 8080);
        os = socketClient.getOutputStream();
        osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        return bw;
    }

    private void informationMenu() throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(selectMode);

            String input = sc.next();
            Thread thread = new Thread(() -> {
                try {

                    switch (input) {
                        case "1":
                            sendMessage(addMovie);
                            receiveMessage();
                            break;
                        case "0":
                            sendMessage(logout);
                            receiveMessage();
                            socketClient.close();
                            System.exit(0);
                            break;
                        default:
                            System.out.println(wrongDigit);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            });
            thread.start();

        }
    }
}