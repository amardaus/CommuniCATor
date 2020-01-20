package sample;

import com.sun.tools.javac.Main;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

    /*
    * Serwer oczekuje na przychodzące połączenia, wysyła listę użytkowników (identyfikatorów),
    * przyjmuje wiadomości i wysyła je do odpowiednich klientów.
    * Serwer powinien przechowywać listę wszystkich użytkowników,
    * w razie gdy któryś nie jest połączony może buforować wiadomości do niego.
    */

public class ServerThread extends Thread{
    Socket socket;
    ObjectInputStream is = null;
    ObjectOutputStream os = null;
    String name;

    public ServerThread(Socket s){
        this.socket = s;
        try {
            is = new ObjectInputStream(socket.getInputStream());
            os = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    ServerThread findUser(Message msg){
        for(int i = 0; i < MainServer.clientCount; i++){
            System.out.println("i"+i);
            System.out.println("count" + MainServer.clientCount);
            System.out.println(MainServer.clientList.get(i).name);
            System.out.println("sender:" + msg.sender);
            if(MainServer.clientList.get(i).name.equals(msg.sender)){
                return MainServer.clientList.get(i);
            }
        }
        return null;
    }

    void sendListOfUsers(ServerThread user){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < MainServer.clientCount; i++){
            if(MainServer.clientList.get(i) != user){
                s.append(MainServer.clientList.get(i).name);
            }
        }
        Message listOfUsers = new Message("server", name, s.toString());
        try {
            user.os.writeObject(listOfUsers);
            user.os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Message msg;

        try {
            /*
            for(int i = 0; i < MainServer.clientCount; i++){
                //System.out.println("$" + MainServer.clientList.get(i).name);
                if(MainServer.clientList.get(i).name.equals(initialMessage.sender)){
                    sendListOfUsers(i, MainServer.clientList.get(i).os);
                }
            }*/


            while (true){
                msg = (Message) is.readObject();

                if(msg.msg.equals("init")){
                    name = msg.sender;
                    sendListOfUsers(findUser(msg));
                }
                else{
                    for(int i = 0; i < MainServer.clientCount; i++){
                        if(MainServer.clientList.get(i).name.equals(msg.receiver)){
                            MainServer.clientList.get(i).os.writeObject(msg);
                            MainServer.clientList.get(i).os.flush();
                        }
                    }
                    System.out.println(msg.sender + ": " + msg.msg + "---->" + msg.receiver);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO error in server thread");
        }
        catch(ClassNotFoundException e){
            System.out.println("Class not found");
        }
        finally{
            try {
                System.out.println("Closing socket");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
