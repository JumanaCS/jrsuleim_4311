/* 
Jumana Suleiman
CSCI 4311 
PA1 

Refrences:
https://gyawaliamit.medium.com/multi-client-chat-server-using-sockets-and-threads-in-java-2d0b64cad4a7
https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
*/


import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class MyServer {

    private ServerSocket server = null;

    public MyServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started");
            System.out.println("Waiting client");

            ArrayList<ServerThread> clients =  new ArrayList<ServerThread>();

            while (true) {
                Socket socket = server.accept();
                
                ServerThread serverThread = new ServerThread(socket ,clients);
                clients.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error here " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MyServer server = new MyServer(3972);
    }
}