package Client;



import Entidades.Localizacao;
import Entidades.Trotinete;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {


    public static void main(String[] args) throws IOException {
        Localizacao l = new Localizacao(0,0);
        Trotinete t = new Trotinete("1",false,10,'N',l);

    Socket s = new Socket("localhost",4999);
    DataOutputStream out = new DataOutputStream(s.getOutputStream());
    t.serialize(out);

    PrintWriter pr = new PrintWriter(s.getOutputStream());
    pr.println("hello");
    out.flush();
    InputStreamReader in = new InputStreamReader(s.getInputStream());
    BufferedReader bf= new BufferedReader(in);

    String str = bf.readLine();
        System.out.println("Server:"+str);
    }
}
