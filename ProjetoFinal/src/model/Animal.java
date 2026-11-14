package model;


public class Animal {
    private int idAnimal;
    private String nome;
    private String especie;
    private String raca;
    private int idadeAproximada;
    private String sexo;
    private String porte;
    private String status = "Disponivel";

    public Animal(int idAnimal, String nome, String especie, String raca, int idadeAproximada, String sexo, String porte) {
        this.idAnimal = idAnimal;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idadeAproximada = idadeAproximada;
        this.sexo = sexo;
        this.porte = porte;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdadeAproximada() {
        return idadeAproximada;
    }

    public void setIdadeAproximada(int idadeAproximada) {
        this.idadeAproximada = idadeAproximada;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
