package model;

public class Usuario {

    private int idUsuario;
    private int idPessoa;
    private String login;
    private String senhaHash;
    private String perfil;

    public Usuario(int idUsuario, int idPessoa, String login, String senhaHash, String perfil) {

        this.idUsuario = idUsuario;
        this.idPessoa = idPessoa;
        this.login = login;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}