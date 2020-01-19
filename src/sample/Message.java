package sample;

import java.io.Serializable;

public class Message implements Serializable {
    String sender;
    String receiver;
    String msg;

    Message(String s, String r, String m){
        this.sender = s;
        this.receiver = r;
        this.msg = m;
    }
}
