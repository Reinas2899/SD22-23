package Entidades;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Localizacao {
    int x;
    int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localizacao that = (Localizacao) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Localizacao(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void serialize (DataOutputStream out) throws IOException {


        out.writeInt(x);
        out.writeInt(y);


        out.flush();
    }

    // @TODO
    public static Localizacao deserialize (DataInputStream in) throws IOException {
        int x = in.readInt();
        int y = in.readInt();

        Localizacao localizacao = new Localizacao(x,y);
        return localizacao;


    }



    @Override
    public String toString() {
        return "(" + x + "," + y +")";
    }
}
