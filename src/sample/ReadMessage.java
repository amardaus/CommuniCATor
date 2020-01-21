package sample;

import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ReadMessage implements Runnable{
    Message msg;
    Socket socket;
    ObjectInputStream is;

    ReadMessage(Socket s){
        try {
            this.socket = s;
            is = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try{
                msg = (Message)is.readObject();
                if(msg != null && !msg.sender.equals("server")){
                    System.out.println(msg.sender + ": " + msg.msg);
                    Platform.runLater(() -> {
                        Client.showMessage(msg);
                    });
                }
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}