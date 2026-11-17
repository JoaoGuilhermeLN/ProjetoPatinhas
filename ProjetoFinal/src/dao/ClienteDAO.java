package dao;

import conexao.Conexao;
import model.Cliente;
import model.Endereco;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;

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
            try (PreparedStatement ps
                    = conexao.prepareStatement(sql)) {

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

    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        String sql = "SELECT p.*, c.dataNascimento, c.tipoMoradia, "
                + "e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep "
                + "FROM Cliente c "
                + "INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa "
                + "INNER JOIN Endereco e ON p.idEndereco = e.idEndereco";

        try (PreparedStatement ps = conexao.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Endereco endereco = new Endereco(
                        rs.getInt("idEndereco"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );

                Cliente cliente = new Cliente(
                        rs.getString("dataNascimento"),
                        rs.getString("tipoMoradia"),
                        rs.getInt("idPessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        endereco
                );

                clientes.add(cliente);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar clientes: " + erro.getMessage());
        }

        return clientes;
    }

    public Cliente buscarClientePorCpf(String cpf) {

        Cliente cliente = null;

        String sql = "SELECT p.*, c.dataNascimento, c.tipoMoradia, "
                + "e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep "
                + "FROM Cliente c "
                + "INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa "
                + "INNER JOIN Endereco e ON p.idEndereco = e.idEndereco "
                + "WHERE p.cpf = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Endereco endereco = new Endereco(
                        rs.getInt("idEndereco"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );

                cliente = new Cliente(
                        rs.getString("dataNascimento"),
                        rs.getString("tipoMoradia"),
                        rs.getInt("idPessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        endereco
                );
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar cliente: " + erro.getMessage());
        }

        return cliente;
    }

}
