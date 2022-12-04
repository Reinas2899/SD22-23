package Servidor;

import Entidades.Localizacao;
import Entidades.Reservation;
import Entidades.Trotinete;

import java.io.*;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class Server {
    String logFileName;
    String userFileName;
    String TrotiFileName;

    private List<Reservation> activeReservations = null;

    public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(4999);
    Socket s = ss.accept();
        System.out.println("Client Connected");
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        Message packet = Message.deserialize(in);
        Object message = packet.getMessage();

        switch (packet.getType()){
            case SUCCESS_RESPONSE:
                if(message instanceof SuccessResponse response)
                    //print message
                    ;
            case REGISTER:
                if(message instanceof UserMessage registerUser)
                    //register
                    //send response
                    ;
            case CONNECTION:
                if(message instanceof UserMessage connectUser)
                    //Verify if id exists
                    //Verify is password is correct
                    //log user in
                    //send response
                    ;
            case DESCONNECTION:
                //log user out
                ;
            case NEARBY_SCOOTERS:
                if(message instanceof Localizacao userLocation)
                    //check scooters close by
                    //make a list of them
                    //send list to user
                    ;
            case NEARBY_REWARDS:
                if(message instanceof Localizacao userLocation)
                    //check rewards close by
                    //make a list of them
                    //send list to user
                    ;
            case START_TRIP:
                if(message instanceof Localizacao userLocation)
                    //check timestamp and save in reservation
                    //save starting location
                    ;
        }

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

