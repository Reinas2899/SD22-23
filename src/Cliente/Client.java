package TP;



import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class Client {

    public static void main(String[] args) throws IOException {
    Socket s = new Socket("localhost",4999);
    PrintWriter pr = new PrintWriter(s.getOutputStream());
    pr.println("hello");
    pr.flush();
    InputStreamReader in = new InputStreamReader(s.getInputStream());
    BufferedReader bf= new BufferedReader(in);

    String str = bf.readLine();
        System.out.println("Server:"+str);
    }
}
