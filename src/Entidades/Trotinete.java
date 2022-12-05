package Entidades;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Trotinete{

    private String idTrotinete;
    //O estar reservada é um atributo da trotinete ou da reserva?
    private boolean reservada;
    private int velocidade;
    private char direcao;
    private Localizacao localizacao;

    public Trotinete(String idTrotinete, boolean reservada, int velocidade, char direcao, Localizacao localizacao) {
        this.idTrotinete = idTrotinete;
        this.reservada = reservada;
        this.velocidade = velocidade;
        this.direcao = direcao;
        this.localizacao = localizacao;
    }

    public String getIdTrotinete() {
        return idTrotinete;
    }

    public void setIdTrotinete(String idTrotinete) {
        this.idTrotinete = idTrotinete;
    }

    public boolean isReservada() {
        return reservada;
    }

    public void setReservada(boolean reservada) {
        this.reservada = reservada;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public char getDirecao() {
        return direcao;
    }

    public void setDirecao(char direcao) {
        this.direcao = direcao;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return "Entidades.Trotinete{" +
                "idTrotinete='" + idTrotinete + '\'' +
                ", reservada=" + reservada +
                ", velocidade=" + velocidade +
                ", direcao=" + direcao +
                ", localizacao=" + localizacao +
                '}';
    }


    // @TODO
    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF(idTrotinete);
        out.writeBoolean(reservada);
        out.writeInt(velocidade);
        out.writeChar(direcao);
        localizacao.serialize(out);

        out.flush();
    }

    // @TODO
    public static Trotinete deserialize (DataInputStream in) throws IOException {
        String idTrotinete = in.readUTF();
        boolean reservada = in.readBoolean();
        int velocidade = in.readInt();
        char direcao = in.readChar();
        Localizacao localizacao = new Localizacao(in.readInt(), in.readInt());

        return new Trotinete(idTrotinete,reservada , velocidade, direcao, localizacao);


    }



}