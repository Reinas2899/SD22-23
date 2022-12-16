package Servidor;

import Entidades.*;
import Servidor.Message.ListObject;
import Servidor.Message.Message;
import Servidor.Message.SuccessResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Servidor.Message.MessageType.*;


public class Server {
   //Porque não ter um map das trotinetes?



    private Map<String, Reservation> activeReservations;
    private static Map<String, Utilizador> contasAtivas = new HashMap<>();
    private static Lock l = new ReentrantLock();
    private static Map<Localizacao,Integer> trotinetes = new HashMap<>();
    private static Map<Localizacao,Integer> recompensas = new HashMap<>();
    private static final int tamanhoMapa = 20;
    private static final int numeroTroti = 100;
    private static final int distanciaUser = 5;


    //Lista de threads ativas, aka, clientes/users ativos


    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        preencheMapaTroti(numeroTroti);
        geraRecompensa(10,10);

        while (true) {
            Socket s = ss.accept();
            System.out.println("Client Connected");
            new Thread(() -> {
                while (true) {
                    try {
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());

                        Message packet = Message.deserialize(in);
                        Object message = packet.getMessage();
                        long startTime = 0;

                        long endTime = 0;
                        System.out.println(packet.toString());

                        switch (packet.getType()) {
                            case REGISTER -> {
                                if (message instanceof Utilizador registaUser)
                                    registaUser(registaUser, out);
                            }
                            case CONNECTION -> {
                                if (message instanceof Utilizador loginUser)
                                    login(loginUser, out);
                            }
                            case DESCONNECTION -> {
                                if (message instanceof Utilizador desconnect)
                                    logout(desconnect, out);
                            }
                            case NEARBY_SCOOTERS -> {
                                if (message instanceof Localizacao loc) {
                                    System.out.println("vou mandar scooters");
                                    List<Localizacao> lista = nearbyScooter(distanciaUser, loc);
                                    ListObject lo = new ListObject(lista.size(), lista);
                                    Message m = new Message(LIST_SCOOTERS, lo);
                                    m.serialize(out);
                                }
                            }
                            case SCOOTER_RESERVATION_REQUEST -> {
                                if (message instanceof Localizacao loc) {
                                    System.out.println(loc);
                                    l.lock();
                                    try {
                                        if (trotinetes.get(loc) > 0) {
                                            trotinetes.put(loc, trotinetes.get(loc) - 1);
                                            Message m = new Message(SCOOTER_RESERVATION_RESPONSE, loc);
                                            m.serialize(out);
                                            System.out.println(m.toString());
                                        } else {
                                            Message m = new Message(SCOOTER_RESERVATION_RESPONSE, loc);
                                            m.serialize(out);
                                            out.writeUTF("Nenhuma Trotinete nessa Localizaçao!");
                                            System.out.println(m.toString());
                                        }
                                    } finally {
                                        l.unlock();
                                    }
                                }
                            }
                            case NEARBY_REWARDS -> {
                                if (message instanceof Localizacao userLocation) {
                                    List<Localizacao> lista1 = nearbyRecompensa(distanciaUser, userLocation);
                                    if (lista1.isEmpty()) {
                                        out.writeUTF("Nenhuma recompensa nas proximidades.");
                                    }
                                    out.writeInt(lista1.size());
                                    for (Localizacao l : lista1) {
                                        l.serialize(out);
                                    }
                                }
                            }
                            case START_TRIP -> {
                                if (message instanceof Localizacao userLocation) {
                                    l.lock();
                                    try {
                                        trotinetes.put(userLocation, trotinetes.get(userLocation) - 1);
                                        startTime = System.currentTimeMillis();
                                    } finally {
                                        l.unlock();
                                    }
                                }
                            }
                            case END_TRIP -> {
                                if (message instanceof Localizacao userLocation) {
                                    l.lock();
                                    try {
                                        trotinetes.put(userLocation, trotinetes.get(userLocation) + 1);
                                        endTime = System.currentTimeMillis();
                                    } finally {
                                        l.unlock();
                                    }
                                    Message m = new Message(COST_REWARD, calculaPreco(startTime, endTime));
                                    m.serialize(out);
                                }
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Erro");
                    }
                }
            }).start();

        }
    }

    public static void criamapaTroti(int n){
        trotinetes= new HashMap<>();
                for (int x = 0; x <= n; x++) {
                    for (int y = 0; y <= n; y++) {
                        Localizacao l =new Localizacao(x,y);
                        if(!trotinetes.containsKey(l)) trotinetes.put(l, 0);

                    }
                }
            }

    public static void login(Utilizador user, DataOutputStream out) throws IOException{

                if(existsUser(user.getUsername(), user.getPassword())){
                    l.lock();
                    try{
                        contasAtivas.put(user.getUsername(),user);
                        System.out.println("vou enviar");
                        Message m = new Message(SUCCESS_RESPONSE,new SuccessResponse(true,"Login Efetuado com Sucesso!"));
                        m.serialize(out);
                    }finally {
                        l.unlock();
                    }
                } else {
                    Message m = new Message(SUCCESS_RESPONSE,new SuccessResponse(false,"Login Incorreto!"));
                    m.serialize(out);

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
                l.lock();
                try {
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
                finally {
                    l.unlock();
                }

            }

    public static void setContasAtivas(Map<String, Utilizador> contasAtivas) {
        Server.contasAtivas = contasAtivas;
    }

    public static void geraRecompensa(int n, int creditos){
        criaMapaRecompensas();
        int i =1;

        Random random = new Random();
        while(i<=n) {
            Localizacao l = new Localizacao(random.nextInt(0,tamanhoMapa), random.nextInt(0,tamanhoMapa));
            if(recompensas.get(l)==0){
                recompensas.put(l,creditos);
            }
            else i--;
            i++;
        }
    }

    public static void criaMapaRecompensas(){
        recompensas= new HashMap<>();
        for (int x = 0; x <= tamanhoMapa; x++) {
            for (int y = 0; y <= tamanhoMapa; y++) {
                Localizacao l =new Localizacao(x,y);
                if(!recompensas.containsKey(l)) recompensas.put(l, 0);

            }


        }

    }

    public static void preencheMapaTroti(int n){
        int i = 1;
        criamapaTroti(tamanhoMapa);
        Random random = new Random();
        while(i<=n) {
            Localizacao l = new Localizacao(random.nextInt(0,tamanhoMapa), random.nextInt(0,tamanhoMapa));
            int t = trotinetes.get(l);
            trotinetes.put(l,t+1);
            i++;
        }
        //System.out.println(trotinetes);
    }

    public static List<Localizacao> nearbyScooter(int d, Localizacao l){
        List<Localizacao> lista = new ArrayList<>();
        double distance;

        for (Localizacao t: trotinetes.keySet()) {

            distance = distanciaLocalizacao(t,l);
            if(distance <=d && trotinetes.get(t)>0){
                lista.add(t);
            }
        }
        return lista;


    }

    public static List<Localizacao> nearbyRecompensa(int d, Localizacao l){
        List<Localizacao> lista =new ArrayList<>();

        double distance;

        for (Localizacao l1: recompensas.keySet()) {

            distance = distanciaLocalizacao(l1,l);
            if(distance <=d && recompensas.get(l1)>0){
                lista.add(l1);
            }
        }


        return lista;
    }

    public static double distanciaLocalizacao(Localizacao l1, Localizacao l2){
        return Math.hypot(l2.getX()- l1.getX(), l2.getY() - l1.getY());

    }

    public static float calculaPreco(long startTime, long endTime){

        float elapsTime = endTime-startTime;

        return (float) (elapsTime*0.2);
    }


}


