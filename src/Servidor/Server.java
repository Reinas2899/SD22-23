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
        System.out.println(message.toString());

        switch (packet.getType()) {
            case REGISTER:
                if(message instanceof Utilizador registerUser)
                    //Este user vem sem localização associada
                    //register
                    //send response
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
    }


        public static void handler (Message m, DataOutputStream out) throws IOException {


            if (m.getType() == MessageType.REGISTER) {

                String[] conteudo = m.getMessage().toString().split(",");
                System.out.println(m);

                    if (existsUser(conteudo[0], conteudo[1])) {

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
                        pr.println(conteudo[0] + "," + conteudo[1]);
                        pr.close();
                        br.close();
                        fr.close();

                    }

                } else if (m.getType() == MessageType.CONNECTION) {
                String[] conteudo = m.getMessage().toString().split(",");
                System.out.println(m);
                    if (existsUser(conteudo[0], conteudo[1])) {
                        out.writeUTF("Login Realizado Com Sucesso!");
                        out.flush();
                    } else {
                        out.writeUTF("Login Incorreto");
                        out.flush();
                    }
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

        }


