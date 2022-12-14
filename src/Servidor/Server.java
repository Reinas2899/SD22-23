package Servidor;

import Entidades.*;
import Servidor.Message.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Server {
   //Porque não ter um map das trotinetes?



    private Map<String, Reservation> activeReservations;
    private static Map<String, Utilizador> contasAtivas;
    private static Lock l = new ReentrantLock();
    private static Map<String,Trotinete> trotinetes;
    private static List<Recompensa> recompensas;

    //Lista de threads ativas, aka, clientes/users ativos


    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        preencheMapaTroti(20);

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
                                registaUser(registaUser,out);
                            break;
                        case CONNECTION:
                            if (message instanceof Utilizador connectUser)

                                login(connectUser,out);
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
                            if (message instanceof Localizacao userLocation) {
                                List<Trotinete> lista = nearbyScooter(10,userLocation);
                                if (lista.isEmpty()){
                                    out.writeUTF("Nenhuma scooter nas proximidades.");
                                }
                                out.writeInt(lista.size());
                                for (Trotinete t: lista) {
                                    t.serialize(out);
                                }
                            }


                                //check scooters close by
                                //make a list of them
                                //send list to user
                                ;
                            break;
                        case NEARBY_REWARDS:
                            if (message instanceof Localizacao userLocation) {
                                List<Recompensa> lista = nearbyRecompensa(10,userLocation);
                                if (lista.isEmpty()){
                                    out.writeUTF("Nenhuma recompensa nas proximidades.");
                                }
                                out.writeInt(lista.size());
                                for (Recompensa r: lista) {
                                    r.serialize(out);
                                }
                            }
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



            public static void login(Utilizador user, DataOutputStream out) throws IOException{

                if(existsUser(user.getUsername(), user.getPassword())){
                    l.lock();
                    try{
                        contasAtivas.put(user.getUsername(),user);
                        out.writeUTF("Login Efetuado com Sucessso"); // colocar aqui uma tag para que o cliente saiba q pode avançar
                        out.flush();
                    }finally {
                        l.unlock();
                    }
                } else {
                        out.writeUTF("Username ou Password erradas");
                        out.flush();

                }

            }

    public static void logout(Object message, DataOutputStream out) throws IOException{


        l.lock();
        try {
            contasAtivas.remove(((Utilizador) message).getUsername());
            out.writeUTF("Logout Efetuado com Sucessso"); // colocar aqui uma tag para que o cliente saiba q tem de voltar para o menu principal
            out.flush();
        } finally {
            l.unlock();
        }


        }

        public static boolean existsUser (String username, String password) throws FileNotFoundException{
            File ficheiro = new File("registos.txt");
            String search = username + "," + password;
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
            }

            public static void registaUser(Utilizador user, DataOutputStream out) throws IOException{
                if (existsUser(user.getUsername(), user.getPassword())) {

                        out.writeUTF("Utilizador já existe!");
                        out.flush();
                } else {

                    File file = new File("registos.txt");
                    FileWriter fr = new FileWriter(file, true);
                    BufferedWriter br = new BufferedWriter(fr);
                    PrintWriter pr = new PrintWriter(br);
                    pr.println(user.toStringAccountInfo());
                    pr.close();
                    br.close();
                    fr.close();

                }

            }

    public static void setContasAtivas(Map<String, Utilizador> contasAtivas) {
        Server.contasAtivas = contasAtivas;
    }

    public void geraRecompensa(){


        Random random = new Random();
        Localizacao l = new Localizacao(random.nextInt(0,20), random.nextInt(0,20));
        Recompensa r = new Recompensa(random.nextInt(0,10), l);

        recompensas.add(r);


    }

    public static void preencheMapaTroti(int n){
        int i = 1;
        Random random = new Random();
        while(i<=n) {
            Localizacao l = new Localizacao(random.nextInt(0,20), random.nextInt(0,20));
            for (Trotinete t1: trotinetes.values()) {
                if(t1.getLocalizacao() == l){
                    l =  new Localizacao(random.nextInt(0,20), random.nextInt(0,20));
                }

            }
            Trotinete t = new Trotinete(Integer.toString(i),false,l);
            trotinetes.put(t.getIdTrotinete(),t);
                    i++;
        }
    }

    public static List<Trotinete> nearbyScooter(int d, Localizacao l){
        List<Trotinete> lista = new ArrayList<>();
        double distance;

        for (Trotinete t: trotinetes.values()) {

            distance = distanciaLocalizacao(t.getLocalizacao(),l);
            if(distance <=d){
                lista.add(t);
            }
        }
        return lista;


    }

    public static List<Recompensa> nearbyRecompensa(int d, Localizacao l){
        List<Recompensa> lista =new ArrayList<>();

        double distance;

        for (Recompensa r: recompensas) {

            distance = distanciaLocalizacao(r.getL(),l);
            if(distance <=d){
                lista.add(r);
            }
        }


        return lista;
    }

    public static double distanciaLocalizacao(Localizacao l1, Localizacao l2){
        return Math.hypot(l2.getX()- l1.getX(), l2.getY() - l1.getY());

    }


}


