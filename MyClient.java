/* 
Jumana Suleiman
CSCI 4311 
PA1 
*/

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;

public class MyClient {

    private Socket socket = null;
    private DataInputStream keyboardInput = null;
    private DataOutputStream serverOutputStream = null;

    public MyClient(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            new ClientListener(socket).start();
            
            keyboardInput = new DataInputStream(System.in);
            serverOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }

        String line = "";
        while (!line.equalsIgnoreCase("Bye")) {
            try {
                line = keyboardInput.readLine();
                serverOutputStream.writeUTF(line);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        try {
            keyboardInput.close();
            serverOutputStream.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        MyClient client = new MyClient("localhost", 3972);
    }
}