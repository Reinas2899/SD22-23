package Servidor;

import Entidades.Localizacao;
import Entidades.Reservation;
import Entidades.Trotinete;
import Entidades.Utilizador;
import Servidor.Message.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;


public class Server {
    String logFileName;
    String userFileName;
    String TrotiFileName; //Porque não ter um map das trotinetes?

    private Map<String, Reservation> activeReservations = null;

    //Lista de threads ativas, aka, clientes/users ativos


    public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(4999);
    Socket s = ss.accept();
        System.out.println("Client Connected");
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        Message packet = Message.deserialize(in);
        Object message = packet.getMessage();

        switch (packet.getType()){
            case REGISTER:
                if(message instanceof Utilizador registerUser)
                    //Este user vem sem localização associada
                    //register
                    //send response
                    ;
                break;
            case CONNECTION:
                if(message instanceof Utilizador connectUser)
                    //Atenção, este user só tem msm o id e a pass
                    //é preciso ir à lista de users válidos e guardar
                    //Verify if id exists
                    //Verify is password is correct
                    //log user in
                    //send response
                    ;
                break;
            case DESCONNECTION:
                //log user out
                ;
                break;
            case NEARBY_SCOOTERS:
                if(message instanceof Localizacao userLocation)
                    //check scooters close by
                    //make a list of them
                    //send list to user
                    ;
                break;
            case NEARBY_REWARDS:
                if(message instanceof Localizacao userLocation)
                    //check rewards close by
                    //make a list of them
                    //send list to user
                    ;
                break;
            case START_TRIP:
                if(message instanceof Localizacao userLocation)
                    //check timestamp and save in reservation
                    //save starting location
                    ;
                break;
            case END_TRIP:
                //TODO
                break;
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

        if (message.getType()== MessageType.REGISTER){
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

