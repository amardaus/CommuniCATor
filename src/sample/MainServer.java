package sample;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static javax.net.ssl.HttpsURLConnection.getDefaultSSLSocketFactory;

public class MainServer {
    static int clientCount = 0;
    static ArrayList<ServerThread> clientList = new ArrayList<>();

    public static void main(String[] args) {
        int port = 59090;
        Socket s;
        ServerSocket listener = null;
        SSLServerSocketFactory sslServerSocketFactory;
        SSLServerSocket sslServerSocket = null;

        //https://www.exoscale.com/syslog/jdk-11-security-overview/ !!!!!!!!!!!!!!

        try{
            //System.setProperty("javax.net.ssl.keyStore", "/home/olcia/Documents/key.pfx");
            //System.setProperty("javax.net.ssl.keyStorePassword", "sample");
            sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(4444);
            sslServerSocket.setEnabledProtocols(new String[] {"TLSv1.3"});
            sslServerSocket.setEnabledCipherSuites(new String[] {"TLS_AES_128_GCM_SHA256"});
            //listener = new ServerSocket(port);
            System.out.println("Server Listening...");
        }
        catch(IOException e){
            System.out.println("Server error");
        }

        while (true) {
            try {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                ServerThread st = new ServerThread(sslSocket);
                clientList.add(st);
                clientCount++;
                st.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*while(true){
            try{
                s = listener.accept();
                System.out.println("Connection Established");
                ServerThread st = new ServerThread(s);
                clientList.add(st);
                clientCount++;
                st.start();
            }
            catch(Exception e){
                System.out.println("Connection Error");
                //  fuser -k 59090/tcp
            }
        }*/
    }
}