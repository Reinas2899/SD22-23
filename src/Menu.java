
import java.io.*;
import java.util.Scanner;

public class Menu {


    public static boolean existsUser(String username, String password){
        File ficheiro = new File("registos.txt");
        String search = username+","+password;
        try {
            Scanner scanner = new Scanner(ficheiro);

            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.equals(search) ) {
                    return true;
                }
            }
            return false;
        } catch(FileNotFoundException e) {
            //handle this
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        Scanner ler = new Scanner(System.in);
        System.out.println("------------------SD-TP-GRUPO-21--------------");
        System.out.println("|Insira a operação que pretende realizar :   |");
        System.out.println("|1->Registar utilizador                      |");
        System.out.println("|2->Autenticar                               |");
        System.out.println("|3->                                         |");
        System.out.println("----------------------------------------------");

         int opcao=ler.nextInt();
         ler.nextLine();
         if (opcao==1) {
             System.out.println("Insira o username :");
             String user= ler.nextLine();
             System.out.println("Insira a password :");
             String pass = ler.nextLine();
             if (existsUser(user,pass)) {
                 System.out.println("Utilizador já existe!");
             }
             else {

                 File file = new File("registos.txt");
                 FileWriter fr = new FileWriter(file, true);
                 BufferedWriter br = new BufferedWriter(fr);
                 PrintWriter pr = new PrintWriter(br);
                 pr.println(user + "," + pass);
                 pr.close();
                 br.close();
                 fr.close();

                 }
             }
         if (opcao ==2){
             System.out.println("Insira o username :");
             String user= ler.nextLine();
             System.out.println("Insira a password :");
             String pass = ler.nextLine();
             if (existsUser(user,pass)) {
                 System.out.println("Login realizado com sucesso");
             }
             else System.out.println("Utilizador inválido!");
         }
             }


}




