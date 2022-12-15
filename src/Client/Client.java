package Client;



import Entidades.Localizacao;
import Entidades.Trotinete;
import Servidor.Message.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;

import static Client.Menu.*;


public class Client {


    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 4999);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        menu(out);

        //COISAS QUE VÊM DO SERVER
        new Thread(() -> {
        while (true) {
            try {
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    Message packet = Message.deserialize(in);
                    System.out.println(packet);
                    Object message = packet.getMessage();
            switch (packet.getType()) {
                case SUCCESS_RESPONSE:
                    System.out.println(message);
                    menu2(out);


                    break;
                case LIST_SCOOTERS:
                    System.out.println("Selecione uma Localizaçao: \n");
                    imprimeListas((ListObject) message);
                    menu2(out);

                    break;
                case LIST_REWARDS:
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
            }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
