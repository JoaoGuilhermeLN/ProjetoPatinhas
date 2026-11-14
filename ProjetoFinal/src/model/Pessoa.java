
package model;

/*
 Classe abstrata que representa uma pessoa
 cadastrada no sistema Projeto Patinhas.
 
 Serve como classe base para Cliente e Funcionario.
 */

public abstract class Pessoa {

    private int idPessoa;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Endereco endereco;

    public Pessoa(int idPessoa, String nome, String cpf, String telefone, String email, Endereco endereco) {
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public abstract String getTipoPessoa();
}

