package Entidades;

public class Recompensa {

    Localizacao l;
    int creditos;

    public Recompensa(Localizacao l, int creditos) {
        this.l = l;
        this.creditos = creditos;
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


}
