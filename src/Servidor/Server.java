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

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Server {
    String logFileName;
    String userFileName;
    String TrotiFileName; //Porque não ter um map das trotinetes?

    private Map<String, Reservation> activeReservations;
    private static Map<String, Utilizador> contasAtivas;
    private static Lock l = new ReentrantLock();
    private static Map<String,Trotinete> trotinetes;

    //Lista de threads ativas, aka, clientes/users ativos


    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        while (true) {
            Socket s = ss.accept();
            System.out.println("Client Connected");
            new Thread(() -> {
                try {
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());

                    Message packet = Message.deserialize(in);
                    Object message = packet.getMessage();
                    System.out.println(message.toString());

                    switch (packet.getType()) {
                        case REGISTER:
                            if (message instanceof Utilizador registaUser)
                                registaUser(message,out);
                            break;
                        case CONNECTION:
                            if (message instanceof Utilizador connectUser)

                                login(message,out);
                                //Atenção, este user só tem msm o id e a pass
                                //é preciso ir à lista de users válidos e guardar

                                //Verify if id exists
                                //Verify is password is correct
                                //log user in
                                //send response
                            break;
                        case DESCONNECTION:
                            logout(message,out);
                            //log user out
                            ;
                            break;
                        case NEARBY_SCOOTERS:
                            if (message instanceof Localizacao userLocation)
                                //check scooters close by
                                //make a list of them
                                //send list to user
                                ;
                            break;
                        case NEARBY_REWARDS:
                            if (message instanceof Localizacao userLocation)
                                //check rewards close by
                                //make a list of them
                                //send list to user
                                ;
                            break;
                        case START_TRIP:
                            if (message instanceof Localizacao userLocation)
                                //check timestamp and save in reservation
                                //save starting location
                                ;
                            break;
                        case END_TRIP:
                            //TODO
                            break;
                    }

                } catch (Exception e) {
                    System.out.println("Erro");
                }
            }).start();

        }
    }



            public static void login(Object message, DataOutputStream out){

                if(existsUser(((Utilizador) message).getUsername(), ((Utilizador) message).getPassword())){
                    l.lock();
                    try{
                    try {

                        contasAtivas.put(((Utilizador) message).getUsername(),(Utilizador) message);
                        out.writeUTF("Login Efetuado com Sucessso"); // colocar aqui uma tag para que o cliente saiba q pode avançar
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    }finally {
                        l.unlock();
                    }
                } else {
                    try {
                        out.writeUTF("Username ou Password erradas");
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

    public static void logout(Object message, DataOutputStream out){


        l.lock();
        try {
            try {
            contasAtivas.remove(((Utilizador) message).getUsername());
            out.writeUTF("Logout Efetuado com Sucessso"); // colocar aqui uma tag para que o cliente saiba q tem de voltar para o menu principal
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        } finally {
            l.unlock();
        }


        }

        public static boolean existsUser (String username, String password){
            File ficheiro = new File("registos.txt");
            String search = username + "," + password;
                try {
                    Scanner scanner = new Scanner(ficheiro);

                    int lineNum = 0;
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        lineNum++;
                        if (line.equals(search)) {
                            return true;
                        }
                    }
                    return false;
                } catch (FileNotFoundException e) {
                    //handle this
                }
            return false;
            }

            public static void registaUser(Object message, DataOutputStream out) throws IOException{
                if (existsUser(((Utilizador) message).getUsername(), ((Utilizador) message).getPassword())) {

                    try {
                        out.writeUTF("Utilizador já existe!");
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    File file = new File("registos.txt");
                    FileWriter fr = new FileWriter(file, true);
                    BufferedWriter br = new BufferedWriter(fr);
                    PrintWriter pr = new PrintWriter(br);
                    pr.println(((Utilizador) message).toStringAccountInfo());
                    pr.close();
                    br.close();
                    fr.close();

                }

            }

    public static void setContasAtivas(Map<String, Utilizador> contasAtivas) {
        Server.contasAtivas = contasAtivas;
    }

}


