package dao;

import conexao.Conexao;
import model.Cliente;
import java.sql.*;
import javax.swing.JOptionPane;

public class ClienteDAO {

    Connection conexao;

    public ClienteDAO() {
        conexao = new Conexao().getConexao();
    }

    
public void inserirCliente(Cliente cliente) {

    String sql = "INSERT INTO Cliente "
            + "(idPessoa, dataNascimento, tipoMoradia) "
            + "VALUES (?, ?, ?)";

    if (conexao == null) {
        JOptionPane.showMessageDialog(null,
                "Erro na conexão com o banco!");
        return;
    }

    try {
        conexao.setAutoCommit(false);

        // 1. CADASTRAR ENDEREÇO
        EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

        int idEndereco = enderecoDAO.inserirEndereco(
                cliente.getEndereco());

        if (idEndereco <= 0) {
            throw new SQLException("Erro ao cadastrar endereço.");
        }

        // 2. CADASTRAR PESSOA
        PessoaDAO pessoaDAO = new PessoaDAO(conexao);

        int idPessoa = pessoaDAO.inserirPessoa(cliente);

        if (idPessoa <= 0) {
            throw new SQLException("Erro ao cadastrar pessoa.");
        }

        // 3. CADASTRAR CLIENTE
        try (PreparedStatement ps =
                conexao.prepareStatement(sql)) {

            ps.setInt(1, idPessoa);

            ps.setDate(2, java.sql.Date.valueOf(
                    cliente.getDataNascimento()));

            ps.setString(3, cliente.getTipoMoradia());

            ps.executeUpdate();
        }

        conexao.commit();

        JOptionPane.showMessageDialog(null,
                "Cliente cadastrado com sucesso!");

    } catch (SQLException | IllegalArgumentException erro) {

        try {
            conexao.rollback();
        } catch (SQLException e) {
            System.out.println("Erro no rollback: "
                    + e.getMessage());
        }

        JOptionPane.showMessageDialog(null,
                "Erro ao cadastrar cliente: "
                + erro.getMessage());

    } finally {
        try {
            conexao.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

}
