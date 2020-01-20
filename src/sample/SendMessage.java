package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SendMessage implements Runnable{
    String name;
    Socket socket;
    ObjectOutputStream os = null;

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

    void send(String s, String r, String l){
        Message msg = new Message(s, r, l);
        try {
            os.writeObject(msg);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}