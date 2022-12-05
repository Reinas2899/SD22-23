package Client;



import Entidades.Utilizador;
import Servidor.Message.Message;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

import static Servidor.Message.MessageType.*;

public class Menu {


    public static void menu(DataOutputStream out) throws IOException {
        Scanner ler = new Scanner(System.in);
        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1->Registar utilizador                      |");
        System.out.println("|2->Autenticar                               |");
        System.out.println("|3->                                         |");
        System.out.println("----------------------------------------------");

        int opcao = ler.nextInt();
        ler.nextLine();
        if (opcao == 1) {
            System.out.println("Insira o username :");
            String user = ler.nextLine();
            System.out.println("Insira a password :");
            String pass = ler.nextLine();
            System.out.println("Insira o nome :");
            String nome = ler.nextLine();
            Random random = new Random();
            Integer id = random.nextInt();
            Message m = new Message(REGISTER, new Utilizador(id.toString(),user,nome,pass));
            System.out.println("aqui");
            System.out.println(m.toString());
            m.serialize(out);


            if (opcao == 2) {
                System.out.println("Insira o username :");
                user = ler.nextLine();
                System.out.println("Insira a password :");
                pass = ler.nextLine();
                m = new Message(REGISTER, "1" + "," + user + "," + pass + "," + LocalDateTime.now().toString());
                m.serialize(out);

            }


        }
    }
}




