package Client;



import Entidades.Localizacao;
import Entidades.Trotinete;
import Servidor.Message.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;


public class Client {


    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 4999);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        Menu.menu(out);

       /* //COISAS QUE VÊM DO SERVER
        DataInputStream in = new DataInputStream(s.getInputStream());
        Message packet = Message.deserialize(in);
        Object message = packet.getMessage();

        switch (packet.getType()) {
            case SUCCESS_RESPONSE:
                if (message instanceof SuccessResponse response)
                    //print message and deal with the success flag
                    ;
                break;
            case LIST_SCOOTERS:
                if (message instanceof Localizacao userLocation)
                    //check scooters close by
                    //make a list of them
                    //send list to user
                    ;
                break;
            case LIST_REWARDS:
                if (message instanceof Localizacao userLocation)
                    //check rewards close by
                    //make a list of them
                    //send list to user
                    ;
                break;
            case SCOOTER_RESERVATION_RESPONSE:
                //TODO
                break;
            case COST_REWARD:
                //TODO;
                break;
        }*/
    }
}
