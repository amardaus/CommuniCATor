package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ReadMessage implements Runnable{
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
                Message msg = (Message)is.readObject();
                if(msg != null){
                    System.out.println(msg.sender + ": " + msg.msg);
                }
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}