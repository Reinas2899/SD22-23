package Client;



import Servidor.Message.*;

import java.io.*;
import java.net.Socket;

import static Client.Menu.*;


public class Client {


    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 4999);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        menuSemConta(out);

        //COISAS QUE VÊM DO SERVER
        new Thread(() -> {
        while (true) {
            try {
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    Message packet = Message.deserialize(in);
                    Object message = packet.getMessage();
                    System.out.println("[DEBUG] Recieved packet of type " + packet.getType().toString());

            switch (packet.getType()) {
                case GENERIC:
                    if (! (message instanceof String)) break;
                    System.out.println(message);
                    break;
                case SCOOTER_RESERVATION_RESPONSE: // We just need to print the reservation code for the user to user later
                    if (! (message instanceof String)) break;

                    if (message.equals("Nenhuma Trotinete nessa Localizaçao!")){
                        System.out.println(message);
                        menuLogado(out);
                    }
                    else{
                        System.out.println("Código: " + message);
                        System.out.println("Guarda este código para futuro uso");
                        menuViagem(out);
                    }
                    break;
                //
                case CONNECTION_RESPONSE:
                    if (! (message instanceof String)) break;

                    System.out.println(message);

                    if (message.equals("Login Incorreto!")) menuSemConta(out);
                    else menuLogado(out);

                    break;
                case DESCONNECTION_RESPONSE:
                    if (! (message instanceof String)) break;

                    System.out.println(message);
                    menuSemConta(out);

                    break;
                case LIST_SCOOTERS:
                case LIST_REWARDS:
                    // We just need to print the list so the user can choose
                    if (!(message instanceof ListObject)) break;

                    imprimeListas((ListObject) message);
                    menuLogado(out);
                    break;

                case COST_REWARD:
                    if (!(message instanceof Float)) break;
                    System.out.println("Tens de pagar " + message.toString() + "€");
                    menuLogado(out);
                    break;
            }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
