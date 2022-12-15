package Client;



import Entidades.Localizacao;
import Entidades.Utilizador;
import Servidor.Message.ListObject;
import Servidor.Message.Message;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

import static Servidor.Message.MessageType.*;

public class Menu {


    public static void menu(DataOutputStream out) throws IOException {
        String user = "";
        String pass = "";
        Message m = null;
        String nome = "";
        Scanner ler = new Scanner(System.in);
        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1->Registar utilizador                      |");
        System.out.println("|2->Autenticar                               |");
        System.out.println("----------------------------------------------");

        int opcao = ler.nextInt();
        ler.nextLine();
        if (opcao == 1) {
            System.out.println("Insira o username :");
            user = ler.nextLine();
            System.out.println("Insira a password :");
            pass = ler.nextLine();
            m = new Message(REGISTER, new Utilizador(user, pass));
            System.out.println("aqui");
            System.out.println(m.toString());
            m.serialize(out);
            menu(out);
        }


            if (opcao == 2) {
                System.out.println("Insira o username :");
                user = ler.nextLine();
                System.out.println("Insira a password :");
                pass = ler.nextLine();
                m = new Message(CONNECTION, new Utilizador(user,pass));
                m.serialize(out);
                }


        }


    public static void menu2(DataOutputStream out) throws IOException {
        Scanner ler = new Scanner(System.in);
        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1->Obter as trotinetes mais próximas        |");
        System.out.println("|2->Iniciar viagem                           |");
        System.out.println("|3->Terminar viagem                          |");
        System.out.println("|4->Reservar trotinete                       |");
        System.out.println("----------------------------------------------");
        int opt = ler.nextInt();
        ler.nextLine();
        if (opt== 1){
            System.out.println("Insira a coordenada X :");
            int x = ler.nextInt();
            ler.nextLine();
            System.out.println("Insira a coordenad Y :");
            int y = ler.nextInt();
            ler.nextLine();
            Message m=new Message(NEARBY_SCOOTERS,new Localizacao(x,y));
            m.serialize(out);
        //enviar mensagem para  obter as trotinetes mais proximas
        }
        if (opt==2){
        //enviar mensagem a dizer que reservei a trotinete numa dada localizacao
        }
        if (opt==3){
            //enviar mensagem a dizer que terminei viagem na localizaçao
        }
        if (opt==4){
            //enviar mensagema dizer que quero reservar trotinete na localixzação x e y dadas
        }

    }

    public static void imprimeListas(ListObject list){
        for (int i = 0; i < list.getSize() ; i++) {
            System.out.println(list.getObjects().get(i) + "\n");
        }


    }
}




