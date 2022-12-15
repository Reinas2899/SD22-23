package Entidades;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Recompensa {
    int creditos;
    Localizacao l;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recompensa that = (Recompensa) o;
        return creditos == that.creditos && l.equals(that.l);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditos, l);
    }

    @Override
    public String toString() {
        return "Recompensa{" +
                "creditos=" + creditos +
                ", l=" + l +
                '}';
    }

    public Recompensa(int creditos, Localizacao l) {
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
