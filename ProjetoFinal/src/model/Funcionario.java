
package model;

public class Funcionario extends Pessoa {
    
    private String cargo;
    private int matricula;

    public Funcionario(String cargo, int matricula, int idPessoa, String nome, String cpf, String telefone, String email, Endereco endereco) {
        super(idPessoa, nome, cpf, telefone, email, endereco);
        this.cargo = cargo;
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    @Override
    public String getTipoPessoa() {
        return "Funcionário";
    }
    
}
