/* 
Jumana Suleiman
CSCI 4311 
PA1 
*/

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class ServerThread extends Thread {

    private ArrayList<ServerThread> clients = null;
    private Socket socket = null;
    private DataInputStream in = null;
    private boolean isLoggedinClient  = false;
    private String clientName = null;

    public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
        this.socket = socket;
        this.clients = clients;
        
    }

    public void run() {
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream thisClientWriter = new DataOutputStream(this.socket.getOutputStream());
            thisClientWriter.writeUTF("Enter Username: ");
            
            String line = "";
            
            while (true) {
                try {
                    line = in.readUTF();
                    if (line.equalsIgnoreCase("Bye") && this.isLoggedinClient){
                        broadcast_to_all_clients("Server: Goodbye "+ this.clientName);
                        break;
                    }
                    
                    if (line.equalsIgnoreCase("AllUsers") && this.isLoggedinClient){
                        String all_users = "";
                        for (ServerThread client :this.clients){
                            all_users += client.clientName +" ,";
                        }

                        broadcast_to_all_clients("Server: All Users "+ all_users);
                    }


                    if (!this.isLoggedinClient){
                        String username = get_username(line);
                        if (username == ""){
                            thisClientWriter.writeUTF("Username input must be in this format 'username = yourname'");
                            continue;
                        }

                        this.isLoggedinClient = true;
                        this.clientName = username;
                        broadcast_to_all_clients("Server: Hello " + this.clientName);
                    }
                    
                    System.out.println(this.clientName + ": " +line);
                } catch (IOException i) {
                    this.clients.remove(this.socket);
                    break;
                }
            }

            socket.close();
            in.close();
            this.clients.remove(this.socket);
        } catch (Exception e) {
            System.out.println("Error here " + e.getMessage());
            this.clients.remove(this.socket);
        }
    }

    private String get_username(String userInput){
        String nameSeparater = "username = ";
        int nameStartsAt = userInput.indexOf(nameSeparater);
        if (nameStartsAt==-1){
            return "";
        }

        return userInput.substring(nameStartsAt+nameSeparater.length());

    }

    private void broadcast_to_all_clients(String value){

        // goes over each client and says hi to the client
        for (ServerThread client : this.clients) {
            if (client.isLoggedinClient){
                try{
                    DataOutputStream clientWriter = new DataOutputStream(client.socket.getOutputStream());
                    clientWriter.writeUTF(value);
                }catch (IOException e ){
                    System.out.printf("failed broad casting to client: %s\n",e);
                    this.clients.remove(client);
                }
            }
        }
    } 
}