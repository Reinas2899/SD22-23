package Entidades;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Recompensa {
    int creditos;
    Localizacao l;


    public Recompensa(int creditos,Localizacao l) {
        this.creditos = creditos;
        this.l = l;

    }

    public Localizacao getL() {
        return l;
    }

    public void setL(Localizacao l) {
        this.l = l;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public void serialize(DataOutputStream out) throws IOException {

        out.writeInt(creditos);
        l.serialize(out);

        out.flush();
    }

    public static Recompensa deserialize(DataInputStream in) throws IOException{
        int creditos = in.readInt();
        Localizacao localizacao = new Localizacao(in.readInt(), in.readInt());

        return new Recompensa(creditos,localizacao);
    }


}
