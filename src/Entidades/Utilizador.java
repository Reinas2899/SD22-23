package Entidades;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Utilizador {

    private String username;
    private int creditos;
    private String password;


    public Utilizador(String username, int creditos, String password) {
        this.username = username;
        this.creditos = creditos;
        this.password = password;

    }

    public Utilizador(String username, String password) {
        this.username = username;
        this.creditos = 0;
        this.password = password;
    }


   public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

       @Override
    public String toString() {
        return "username=" + username +
                ",creditos=" + creditos +
                ",password=" + password;
    }

    public String toStringAccountInfo() {
        return username +"," + password;
    }



    public void serialize (DataOutputStream out) throws IOException {

        out.writeUTF(username);


        out.writeInt(creditos);
        out.writeUTF(password);


        out.flush();
    }

    public void serializeBasics (DataOutputStream out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(password);
        out.flush();
    }
    //
    public static Utilizador deserialize (DataInputStream in) throws IOException {
        String username = in.readUTF();
        int creditos = in.readInt();
        String password = in.readUTF();

        return new Utilizador(username , creditos,password);
    }

    public static Utilizador deserializeBasics (DataInputStream in) throws IOException {
        String username = in.readUTF();
        String password = in.readUTF();

        return new Utilizador(username, password);
    }

}