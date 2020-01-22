package sample;

import java.io.*;
import java.net.Socket;

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
                s.append(MainServer.clientList.get(i).name + ",");
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
        boolean quit = false;

        try {
            while (!quit){
                msg = (Message) is.readObject();
                if(msg.msg.equals("init")) {
                    name = msg.sender;
                    sendListOfUsers(findUser(msg));
                }
                else if(msg.msg.equals("quit")){
                    for(int i = 0; i < MainServer.clientCount; i++){
                        if(MainServer.clientList.get(i).name.equals(msg.sender)){
                            MainServer.clientList.remove(i);
                            MainServer.clientCount--;
                            os.close();
                            is.close();
                            quit = true;
                            break;
                        }
                    }
                }
                else {
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
