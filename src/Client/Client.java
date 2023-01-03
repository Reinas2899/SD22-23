package Client;



import Servidor.Message.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static Client.Menu.*;


public class Client {

    final int localPort;

    Socket socket;
    public Socket notiSocket;
    Menu menu;


    public static void main(String[] args) throws IOException {
        new Client();
    }

    public Client() throws IOException {
        socket = new Socket("localhost", 4999);
        notiSocket = new Socket("localhost", 4999);
        menu = new Menu(this);
        this.localPort = socket.getLocalPort();
        System.out.println("identifier: " + localPort);
        System.out.println("Notification identifier: " + notiSocket.getLocalPort());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        menu.menuSemConta(out);
        receiveFromServer().start();
    }

    public Thread notificationThread(){
        return new Thread(() -> {
            try {
                while (true) {
                    DataInputStream in = new DataInputStream(notiSocket.getInputStream());
                    Message packet = Message.deserialize(in);
                    Object message = packet.getMessage();
                    System.out.println("[DEBUG] Recieved packet of type " + packet.getType().toString() + " in notification thread");

                    if (packet.getType() == MessageType.NOTIFICATION_MSG) {
                        if (!(message instanceof ListObject)) break;
                        imprimeRecompensas((ListObject) message);
                    }
                    if (packet.getType() == MessageType.DESCONNECTION_RESPONSE) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private Thread receiveFromServer() {
        return new Thread(() -> {
            while (true) {
                try {
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    Message packet = Message.deserialize(in);
                    Object message = packet.getMessage();
                    System.out.println("[DEBUG] Recieved packet of type " + packet.getType().toString());

                    switch (packet.getType()) {
                        case GENERIC:
                            if (! (message instanceof String)) break;
                            System.out.println("AQUI");
                            System.out.println(message);

                            if (message.equals("Registo feito!")|| message.equals("Utilizador já existe!"))
                                menu.menuSemConta(out);
                            break;
                        case SCOOTER_RESERVATION_RESPONSE: // We just need to print the reservation code for the user to user later
                            if (! (message instanceof String)) break;

                            if (message.equals("Nenhuma Trotinete nessa Localizaçao!")){
                                System.out.println(message);
                                menu.menuLogado(out);
                            }
                            else{
                                System.out.println("Código: " + message);
                                System.out.println("Guarda este código para futuro uso");
                                menu.menuReserve(out);
                            }
                            break;
                        //
                        case CONNECTION_RESPONSE:
                            if (! (message instanceof String)) break;

                            System.out.println(message);

                            if (message.equals("Login Incorreto!") || message.equals("Este user já está logado noutro cliente."))
                                menu.menuSemConta(out);
                            else menu.menuLogado(out);

                            break;
                        case DESCONNECTION_RESPONSE:
                            if (! (message instanceof String)) break;
                            System.out.println(message);
                            menu.menuSemConta(out);

                            break;
                        case LIST_SCOOTERS:
                            // We just need to print the list so the user can choose
                            if (!(message instanceof ListObject)) break;

                            imprimeTrotis((ListObject) message);
                            menu.menuLogado(out);
                            break;
                        case LIST_REWARDS:
                            // We just need to print the list so the user can choose
                            if (!(message instanceof ListObject)) break;

                            imprimeRecompensas((ListObject) message);
                            menu.menuLogado(out);
                            break;

                        case COST_REWARD:
                            if (!(message instanceof Float)) break;
                            System.out.println("Tens de pagar " + message.toString() + "€");
                            menu.menuLogado(out);
                            break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
