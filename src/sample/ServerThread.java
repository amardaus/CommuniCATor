package sample;

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
            Message initialMessage = (Message)is.readObject();
            name = initialMessage.sender;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Message msg;

        try {
            while (true){
                msg = (Message) is.readObject();
                System.out.println(msg.sender + ": " + msg.msg);

                for(int i = 0; i < MainServer.clientCount; i++){
                    if(MainServer.clientList.get(i).name.equals(msg.receiver)){
                        MainServer.clientList.get(i).os.writeObject(msg);
                        MainServer.clientList.get(i).os.flush();
                    }
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
