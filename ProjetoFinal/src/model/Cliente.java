
package model;

public class Cliente extends Pessoa {

    private String dataNascimento;
    private String tipoMoradia;

    public Cliente(String dataNascimento, String tipoMoradia, int idPessoa, String nome, String cpf, String telefone, String email, Endereco endereco) {
        super(idPessoa, nome, cpf, telefone, email, endereco);
        this.dataNascimento = dataNascimento;
        this.tipoMoradia = tipoMoradia;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTipoMoradia() {
        return tipoMoradia;
    }

    public void setTipoMoradia(String tipoMoradia) {
        this.tipoMoradia = tipoMoradia;
    }   

    @Override
    public String getTipoPessoa() {
        return "Cliente";
    }
}
