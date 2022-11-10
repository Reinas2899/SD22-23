public class Utilizador {

    String id;
    String username;
    String nome;
    int creditos;
    String password;
    Localizacao localizacao;


    public Utilizador(String id, String username, String nome, int creditos, String password, Localizacao localizacao) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.creditos = creditos;
        this.password = password;
        this.localizacao = localizacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", creditos=" + creditos +
                ", password='" + password + '\'' +
                ", localizacao=" + localizacao +
                '}';
    }
}