package model;

import java.time.LocalDate;

public class Solicitacao {

    private int idSolicitacao;
    private int idCliente;
    private int idAnimal;
    private LocalDate dataSolicitacao;
    private String status;

    public Solicitacao(int idCliente, int idAnimal) {
        this.idCliente = idCliente;
        this.idAnimal = idAnimal;
        this.dataSolicitacao = LocalDate.now();
        this.status = "Pendente";
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
