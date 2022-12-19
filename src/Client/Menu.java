package Client;



import Entidades.Localizacao;
import Entidades.Utilizador;
import Servidor.Message.ListObject;
import Servidor.Message.Message;
import Servidor.Message.ReservationMessage;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static Servidor.Message.MessageType.*;

public class Menu {
       static Scanner ler = new Scanner(System.in);

    public static void menuSemConta(DataOutputStream out) throws IOException {
        String user = "";
        String pass = "";
        String nome = "";

        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1->Registar utilizador                      |");
        System.out.println("|2->Autenticar                               |");
        System.out.println("----------------------------------------------");

        int opcao = ler.nextInt(); ler.nextLine();

        if (opcao == 1) {
            new Message(REGISTER, menuGetUtilizador()).serialize(out);
            System.out.println("[DEBUG] REGISTER message sent");
        }
        if (opcao == 2) {
            new Message(CONNECTION, menuGetUtilizador()).serialize(out);
            System.out.println("[DEBUG] CONNECTION message sent");
        }
    }

    public static void menuLogado(DataOutputStream out) throws IOException {
        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1-> Obter as trotinetes mais próximas       |");
        System.out.println("|2-> Obter as recompensas mais próximas      |");
        System.out.println("|3-> Iniciar viagem                          |");
        System.out.println("|4-> Logout                                  |");
        System.out.println("----------------------------------------------");
        int opt = ler.nextInt();
        ler.nextLine();
        if (opt== 1){
            new Message(NEARBY_SCOOTERS , menuGetLocalização()).serialize(out);
            System.out.println("[DEBUG] Sent NEARBY_SCOOTERS to server");
        }

        if (opt== 2){
            new Message(NEARBY_REWARDS , menuGetLocalização()).serialize(out);
            System.out.println("[DEBUG] Sent NEARBY_REWARDS to server");
        }

        if (opt==3){
            new Message(START_TRIP,null).serialize(out);
            System.out.println("[DEBUG] Sent START_TRIP to server");
        }
        if (opt==4){
            new Message(DESCONNECTION , null).serialize(out);
            System.out.println("[DEBUG] Sent DESCONNECTION to server");
        }
    }

    public static void menuViagem(DataOutputStream out) throws IOException {
        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1->Terminar viagem                          |");
        System.out.println("----------------------------------------------");

        // Ler input aqui
        int opt = ler.nextInt(); ler.nextLine();

        if (opt== 1){
            Localizacao loc = menuGetLocalização();
            System.out.println("Insira código de reserva :");
            String reservationCode = ler.nextLine();
            System.out.println("[DEBUG] Sent END_TRIP to server");
            new Message(END_TRIP, new ReservationMessage(reservationCode, loc)).serialize(out);
        }

    }

    public static void imprimeListas(ListObject list){
        for (int i = 0; i < list.getSize() ; i++) {
            System.out.println(list.getObjects().get(i) + "\n");
        }
    }

    private static Utilizador menuGetUtilizador(){
        System.out.println("Insira o username :");
        String username = ler.nextLine();
        System.out.println("Insira a password :");
        String pass = ler.nextLine();
        return new Utilizador(username, pass);
    }
    private static Localizacao menuGetLocalização(){
        System.out.println("Insira a coordenada X :");
        int x = ler.nextInt();
        ler.nextLine();
        System.out.println("Insira a coordenada Y :");
        int y = ler.nextInt();
        ler.nextLine();
        return new Localizacao(x,y,-1);
    }
}




