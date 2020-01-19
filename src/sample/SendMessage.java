package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SendMessage implements Runnable{
    String name;
    Socket socket;
    String receiver;
    ObjectOutputStream os = null;
    BufferedReader reader = null;

    /*public void setReceiver(String receiver){
        this.receiver = receiver;
    }*/

    SendMessage(Socket s, String n, String receiver){
        this.socket = s;
        this.name = n;

        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    volatile String line;
    void setLine(String s){
        line = s;
        System.out.println("setting line = " + line);
    }

    @Override
    public void run() {
        while(true){
            try {
                //line = reader.readLine();
                if(line != null){
                    if(line.equals("quit")) break;
                    if(!line.isEmpty()){
                        System.out.println("sending message!");
                        Message msg = new Message(name, receiver, line);
                        os.writeObject(msg);
                        os.flush();
                        line = null;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}