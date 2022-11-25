package Servidor;

import Entidades.Trotinete;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    String logFileName;
    String userFileName;
    String TrotiFileName;
    public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(4999);
    Socket s = ss.accept();
        System.out.println("Client Connected");
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        Trotinete t = Trotinete.deserialize(in);
        System.out.println(t.toString());

        //InputStreamReader in = new InputStreamReader(s.getInputStream());
        //BufferedReader bf = new BufferedReader(in);
        //String str= bf.readLine();
        //System.out.println("Client: "+ str);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("yes ");
        pr.flush();
    }


    public void Handler(Message message){

        if (message.getType()==MessageType.REGISTER){
            try {
                FileWriter writer = new FileWriter(userFileName);
                writer.write(message.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


}

