package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class MainServer {
    static int clientCount = 0;
    static ArrayList<ServerThread> clientList = new ArrayList<>();
    //static HashMap<String, ServerThread> clientMap = new HashMap<>();   //zamiast listy?
    BlockingQueue<Message> queue;

    public static void main(String[] args) {
        int port = 59090;
        Socket s;
        ServerSocket listener = null;

        try{
            listener = new ServerSocket(port);
            System.out.println("Server Listening...");
        }
        catch(IOException e){
            System.out.println("Server error");
        }

        while(true){
            try{
                s = listener.accept();
                System.out.println("Connection Established");
                ServerThread st = new ServerThread(s);
                clientList.add(st);
                clientCount++;
                st.start();
            }
            catch(Exception e){
                System.out.println("Connection Error");
                //  fuser -k 59090/tcp
            }
        }
    }
}