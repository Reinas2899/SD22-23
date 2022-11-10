public class Trotinete{

String idTrotinete;
boolean reservada;
int velocidade;
char direcao;
Localizacao localizacao;

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
        return "Trotinete{" +
                "idTrotinete='" + idTrotinete + '\'' +
                ", reservada=" + reservada +
                ", velocidade=" + velocidade +
                ", direcao=" + direcao +
                ", localizacao=" + localizacao +
                '}';
    }


}