package sample;

import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ReadMessage implements Runnable{
    Message msg;
    Socket socket;
    ObjectInputStream is;
    volatile boolean quit = false;

    ReadMessage(Socket s){
        try {
            this.socket = s;
            is = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRunning()
    {
        quit = true;
    }


    @Override
    public void run() {
        while(!quit){   //quit sie nie zmienia wiec on probuje zczytac zamknietego strumienia
            System.out.println("Quit: " + quit);
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
                //e.printStackTrace();
            }
        }
    }
}