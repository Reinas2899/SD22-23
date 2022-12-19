package Servidor;

import Entidades.*;
import Servidor.Message.ListObject;
import Servidor.Message.Message;
import Servidor.Message.ReservationMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static Servidor.Message.MessageType.*;


public class Server {
   //Porque não ter um map das trotinetes?
    private static Map<String, Reservation> reservasAtivas = new HashMap<>();

    private static Map<Integer, Utilizador> contasAtivas = new HashMap<>(); //<port, user>
    private List<Integer> notificationBros = new ArrayList<>();
    private static Lock l = new ReentrantLock();
    private static List<Localizacao> trotinetes = new ArrayList<>();
    private static Map<Localizacao,Integer> recompensas = new HashMap<>();
    private static final int tamanhoMapa = 20;
    private static final int numeroTroti = 100;

    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static Lock readLock = rwLock.readLock();
    private static Lock writeLock = rwLock.writeLock();

    private static final int distanciaUser = 5;
    //Lista de threads ativas, aka, clientes/users ativos
    static int numThreads = 5;
    static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());



    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() throws IOException {
        preencheMapaTroti(numeroTroti);
        geraRecompensa(10,10);
        receiveFromClient().start();
        sendNotifications().start();

    }

    private Thread sendNotifications() {
        return new Thread(() -> {
            try {
                while (true) {
                    for(Integer port : notificationBros){
                        Socket socket = new Socket("localhost", port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        Utilizador user = contasAtivas.get(port);
                        nearbyRecompensa(distanciaUser, new Localizacao(1, 1), out);
                        socket.close();
                    }

                    Thread.sleep(10000);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }


    private Thread receiveFromClient() throws IOException {

        ServerSocket socket_control = new ServerSocket(4999);
        return new Thread(() -> {
            try {

                while (true) {

                    Socket new_socket = socket_control.accept();
                    connectedClient(new_socket).start();
                }

            } catch (Exception e) {
                System.out.println("[ERROR] Thread that connects to clients crashed!");
                System.out.println(e);
                System.exit(-1);
            }
        });
    }

    private Thread connectedClient(Socket s) {
        return new Thread(() -> {
            try {
                boolean connected = true;
                while(connected) {
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());

                    Message packet = Message.deserialize(in);
                    Object message = packet.getMessage();

                    System.out.println("[DEBUG] Recieved packet of type " + packet.getType().toString() +
                            " from client " + s.getPort());

                    switch (packet.getType()) {
                        case REGISTER -> {
                            // Dado um username e uma password, guardamos este user na "base de dados" (um ficheiro)
                            if (message instanceof Utilizador registaUser)
                                registaUser(registaUser, out);
                        }
                        case CONNECTION -> {
                            // Dado um username e uma password, verificamos se existe e se sim, criamos uma conexão

                            if (message instanceof Utilizador loginUser)
                                login(s.getPort(), loginUser, out);
                        }
                        case DESCONNECTION -> {
                            logout(s.getPort(), out);
                        }
                        case NEARBY_SCOOTERS -> {
                            // Dado a localização do user, damos uma lista de trotinetes perto
                            if (message instanceof Localizacao loc) {
                                nearbyScooter(distanciaUser, loc, out);
                            }
                        }
                        case NEARBY_REWARDS -> {
                            // Dado a localização do user, damos uma lista de recompensas perto
                            if (message instanceof Localizacao userLocation) {
                                nearbyRecompensa(distanciaUser, userLocation, out);

                                //String notificationMessage = "This is a notification";
                                //String recipient = "recipient@example.com";
                                //NotificationThread notificationThread = new NotificationThread(notificationMessage, recipient);
                                //executor.execute(notificationThread);

                            }
                        }
                        case SCOOTER_RESERVATION_REQUEST -> {

                        }
                        case START_TRIP -> {
                            // Dado a localização do user, retiramos uma trotinete da localização e enviamos um
                            // código de reserva. Caso nao haja trotinetes, enviamos uma mensagem de insucesso.
                            if (message instanceof ReservationMessage newReserva) {
                                startTrip(s.getPort(), out);
                            }
                        }
                        case END_TRIP -> {
                            if (message instanceof ReservationMessage reservation) {
                                endTrip(reservation, out);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("[ERROR] process thread crashed!");
                System.out.println(e);
                System.exit(-1);
            }
        });
    }



    public static void criaMapaTroti(int n){

                for (int x = 0; x <= n; x++) {
                    for (int y = 0; y <= n; y++) {
                        Localizacao l =new Localizacao(x,y,0);
                        trotinetes.add(l);


                    }
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





    public static void setContasAtivas(Map<Integer, Utilizador> contasAtivas) {
        Server.contasAtivas = contasAtivas;
    }

    public static void geraRecompensa(int n, int creditos){

        criaMapaRecompensas();
        int i =1;
        while(i<=n) {
            for (Localizacao l: trotinetes) {
                if(recompensas.get(l)==0){
                    recompensas.put(l,creditos);
                }
                else i--;
                i++;
            }

        }
    }

    public static void criaMapaRecompensas(){
        recompensas= new HashMap<>();
        for (int x = 0; x <= tamanhoMapa; x++) {
            for (int y = 0; y <= tamanhoMapa; y++) {
                for (Localizacao l: trotinetes) {
                    if(!recompensas.containsKey(l)) recompensas.put(l, 0);
                }

            }
        }
    }

    public static void preencheMapaTroti(int n){
        criaMapaTroti(tamanhoMapa);
        Random random = new Random();
        int i = 0;

            while (i<n){
                int x = random.nextInt(0,400);
                Localizacao l = trotinetes.get(x);
                l.setNumTrotinetes(l.getNumTrotinetes()+1);
                i++;
            }

        //System.out.println(trotinetes);
    }

    public static double distanciaLocalizacao(Localizacao l1, Localizacao l2){
        return Math.hypot(l2.getX()- l1.getX(), l2.getY() - l1.getY());
    }

    public static float calculaPreco(Reservation reserva){
        float elapsTime = reserva.getEndTime() - reserva.getStartTime();
        return (float) (elapsTime*0.2);
    }

    /*****************************************************************
     * FUNCTION:     registaUser
     * INPUT:        user
     * DESCRIPTION:  1- Guarda o user no ficheiro que contem contas,
     *                  caso ainda não exista
     *               2- Envia mensagem de resposta
     *****************************************************************/
    public static void registaUser(Utilizador user, DataOutputStream out) throws IOException{
        String message = "Utilizador já existe!";

        l.lock();
        try {
            if (!existsUser(user.getUsername(), user.getPassword())) {
                File file = new File("registos.txt");
                FileWriter fr = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(fr);
                PrintWriter pr = new PrintWriter(br);
                pr.println(user.toStringAccountInfo());
                pr.close();
                br.close();
                fr.close();
                message = "Registo feito!";
            }
        }
        finally {
            l.unlock();
            System.out.println("[DEBUG] Sending a GENERIC message");
            new Message(GENERIC, message).serialize(out);
        }
    }

    /*****************************************************************
     * FUNCTION:     login
     * INPUT:        user
     * DESCRIPTION:  1- Se user existir, valida-o
     *               2- Se o user não estiver logado, então acrescenta-se
     *                  às contas ativas
     *               3- Envia mensagem de resposta
     *****************************************************************/
    public static void login(Integer port, Utilizador user, DataOutputStream out) throws IOException{
        String message = "Login incorreto!";

        //TODO não é preciso meter isto dentro do lock?
        if(existsUser(user.getUsername(), user.getPassword())){
            l.lock();
            try{
                if(contasAtivas.containsKey(port)){
                    message = "Este user já está logado noutro cliente.";
                }
                else{
                    contasAtivas.put(port, user);
                    message = "Login efetuado com sucesso!";
                }

            }finally {
                l.unlock();
                System.out.println("[DEBUG] Sending a CONNECTION_RESPONSE");
                new Message(CONNECTION_RESPONSE, message).serialize(out);
            }
        }

    }

    /*****************************************************************
     * FUNCTION:     logout
     * INPUT:        username
     * DESCRIPTION:  1- Remove-se user das contas ativas
     *               3- Envia mensagem de resposta
     *****************************************************************/
    public static void logout(Integer port, DataOutputStream out) throws IOException{
        //TODO Se houver erros, alterar mensagem

        writeLock.lock();
        try {
            contasAtivas.remove(port);
        } finally {
            writeLock.unlock();
            System.out.println("[DEBUG] Sending a DESCONNECTION_RESPONSE");
            new Message(DESCONNECTION_RESPONSE,"Logout Efetuado com Sucessso").serialize(out);
        }
    }

    /*****************************************************************
     * FUNCTION:     startTrip
     * INPUT:        newReserva (username e localização inicial)
     * DESCRIPTION:  1- Faz uma nova reserva com um código aleatório
     *               2- Verifica se há trotinetes livres na localização
     *               3- Retira uma trotinete dessa localização
     *               4- Envia mensagem de resposta
     *****************************************************************/
    private static void startTrip(ReservationMessage newReserva, DataOutputStream out) throws IOException {
        //start time is automatically put here
        //reservation code too
        Reservation reserva = new Reservation(newReserva.getInformation(), newReserva.getLocation());
        String message = "Nenhuma Trotinete nessa Localizaçao!";
        Localizacao aux = newReserva.getLocation();

        // Acquire the write lock before modifying the list
        writeLock.lock();
        try {
            for (Localizacao loc:trotinetes) {
                if (loc.getX()==aux.getX() && loc.getY()==aux.getY() && loc.getNumTrotinetes() > 0) {
                    //Enquanto houver uma reserva com código igual, queremos mudar ate ser diferente
                    while (reservasAtivas.containsKey(reserva.getReservationCode())) reserva.setReservationCode();
                    message = reserva.getReservationCode();
                    reservasAtivas.put(reserva.getReservationCode(), reserva);
                    loc.setNumTrotinetes(loc.getNumTrotinetes()-1);

                }
            }
        } finally {
            // Release the write lock after modifying the list
            writeLock.unlock();
        }
        new Message(SCOOTER_RESERVATION_RESPONSE, message).serialize(out);
        System.out.println("[DEBUG] Sending a SCOOTER_RESERVATION_RESPONSE");
    }

    /*****************************************************************
     * FUNCTION:     startTrip
     * INPUT:        newReserva (username e localização inicial)
     * DESCRIPTION:  1- Faz uma nova reserva com um código aleatório
     *               2- Verifica se há trotinetes livres na localização
     *               3- Retira uma trotinete dessa localização
     *               4- Envia mensagem de resposta
     *****************************************************************/
    private static void startTrip(String reservationCode, DataOutputStream out) throws IOException {

        // Acquire the write lock before modifying the list
        writeLock.lock();
        try {
            Reservation reserva = reservasAtivas.get(reservationCode);
            reserva.setStartTime();
            reservasAtivas.put(reservationCode, reserva);
        } finally {
            // Release the write lock after modifying the list
            writeLock.unlock();
        }
        new Message(SCOOTER_RESERVATION_RESPONSE, message).serialize(out);
        System.out.println("[DEBUG] Sending a SCOOTER_RESERVATION_RESPONSE");
    }

    /*****************************************************************
     * FUNCTION:     endTrip
     * INPUT:        reserva (código de reserva e localização final)
     * DESCRIPTION:  1- Verifica se código de reserva é válido
     *               2- Remover reserva das reservas ativas
     *               3- Adicionar trotinete à localização
     *               4- Calcular e enviar custo e recompensas ao cliente
     *****************************************************************/
    private static void endTrip(ReservationMessage reserva, DataOutputStream out) throws IOException {
        String message = "Nenhuma Trotinete nessa Localizaçao!";
        Reservation reservation = new Reservation();

        writeLock.lock();
        try
        {
            if(reservasAtivas.containsKey(reserva.getInformation())){
                //Tratar da reserva
                reservation = reservasAtivas.get(reserva.getInformation());
                reservation.endReservation(reserva.getLocation());
                reservasAtivas.remove(reserva.getInformation());

                //Tratar das trotinetes
                for (Localizacao l:trotinetes) {
                    if (l.getX() == reserva.getLocation().getX() && l.getY()== reserva.getLocation().getY()){
                        l.setNumTrotinetes(l.getNumTrotinetes()+1);
                    }
                }
            }
        } finally {
            writeLock.unlock();
        } //Responder ao cliente
        System.out.println("[DEBUG] Sending a COST_REWARD message");
        //TODO falta recompensa
        new Message(COST_REWARD, calculaPreco(reservation)).serialize(out);
    }

    /*****************************************************************
     * FUNCTION:     nearbyScooter
     * INPUT:        raio de proximidade, localização
     * DESCRIPTION:  1- Faz lista de trotinetes que estão a um raio
     *                  de distancia do cliente
     *               2- Devolve lista ao cliente
     *****************************************************************/
    public static void nearbyScooter(int d, Localizacao l, DataOutputStream out) throws IOException {
        List<Localizacao> lista = new ArrayList<>();
        double distance;

        for (Localizacao t: trotinetes) {

            distance = distanciaLocalizacao(t,l);
            if(distance <=d && t.getNumTrotinetes() > 0){
                lista.add(t);
            }
        }
        if(lista.isEmpty()) {
            System.out.println("[DEBUG] Sending a GENERIC message");
            new Message(GENERIC, "Nenhuma trotinete nas proximidades").serialize(out);
        }
        else{
            System.out.println("[DEBUG] Sending a LIST_SCOOTERS message");
            new Message(LIST_SCOOTERS, new ListObject(lista.size(), lista)).serialize(out);
        }
    }

    /*****************************************************************
     * FUNCTION:     nearbyRecompensa
     * INPUT:        raio de proximidade, localização
     * DESCRIPTION:  1- Faz lista de recompensas que estão a um raio
     *                  de distancia do cliente
     *               2- Devolve lista ao cliente
     *****************************************************************/
    public static void nearbyRecompensa(int d, Localizacao l, DataOutputStream out) throws IOException {
        List<Localizacao> lista =new ArrayList<>();
        double distance;
        for (Localizacao l1: recompensas.keySet()) {

            distance = distanciaLocalizacao(l1,l);
            if(distance <=d && recompensas.get(l1)>0){
                lista.add(l1);
            }
        }
        if(lista.isEmpty()) {
            System.out.println("[DEBUG] Sending a GENERIC message");
            new Message(LIST_SCOOTERS, "Nenhuma recompensa nas proximidades").serialize(out);
        }
        else{
            System.out.println("[DEBUG] Sending a LIST_REWARDS message");
            new Message(LIST_REWARDS, new ListObject(lista.size(), lista)).serialize(out);
        }


    }
}


