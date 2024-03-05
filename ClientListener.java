/* 
Jumana Suleiman
CSCI 4311 
PA1 
*/

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientListener extends Thread {

    private DataInputStream in = null;

    public ClientListener(Socket socket) {
        try {
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("failed");
        }
    }

    public void run() {
        try {
            String line = "";
            while (true) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                } catch (IOException i) {
                    this.in.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error here " + e.getMessage());
        }
    }
}
