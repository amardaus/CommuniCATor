package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SendMessage implements Runnable{
    String name;
    Socket socket;
    ObjectOutputStream os = null;
    volatile String receiver;
    volatile String line;

    SendMessage(Socket s, String n){
        this.socket = s;
        this.name = n;

        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            Message initialMsg = new Message(name, "server", "init");
            os.writeObject(initialMsg);
            os.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setLine(String s){
        line = s;
        System.out.println("setting line = " + line);
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    void send(){
        if(line != null){
            if(!line.isEmpty()){
                System.out.println("sending message!");
                Message msg = new Message(name, receiver, line);
                try {
                    os.writeObject(msg);
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                line = null;
            }
        }
    }

    @Override
    public void run() {
        
    }
}