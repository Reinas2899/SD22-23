package TP;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class implements java Socket server
 * @author pankaj
 *
 */
public class Server {
    public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(4999);
    Socket s = ss.accept();
        System.out.println("Client Connected");
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str= bf.readLine();
        System.out.println("Client: "+ str);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("yes ");
        pr.flush();
    }


}

