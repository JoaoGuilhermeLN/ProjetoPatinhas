
package dao;

import conexao.Conexao;
import model.Cliente;
import model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClienteDAO {

    Connection conexao;

    public ClienteDAO() {
        conexao = new Conexao().getConexao();
    }

    // CADASTRAR CLIENTE
    public void inserirCliente(Cliente cliente) {

        String sql = "INSERT INTO Cliente (idPessoa, dataNascimento, tipoMoradia) VALUES (?, ?, ?)";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            // 1. Cadastrar endereço
            EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);
            int idEndereco = enderecoDAO.inserirEndereco(cliente.getEndereco());

            if (idEndereco <= 0) {
                throw new SQLException("Erro ao cadastrar endereço.");
            }

            // 2. Cadastrar pessoa
            PessoaDAO pessoaDAO = new PessoaDAO(conexao);
            int idPessoa = pessoaDAO.inserirPessoa(cliente);

            if (idPessoa <= 0) {
                throw new SQLException("Erro ao cadastrar pessoa.");
            }

            // 3. Cadastrar cliente
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {

                ps.setInt(1, idPessoa);
                ps.setDate(2, java.sql.Date.valueOf(cliente.getDataNascimento()));
                ps.setString(3, cliente.getTipoMoradia());

                int linhas = ps.executeUpdate();

                if (linhas == 0) {
                    throw new SQLException("Cliente não foi cadastrado!");
                }
            }

            conexao.commit();

            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");

        } catch (SQLException | IllegalArgumentException erro) {

            try {
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro no rollback: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente: " + erro.getMessage());

        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    // sobrecarga de métodos
    public int inserirCliente(Cliente cliente, Connection conexao)
        throws SQLException {

    String sql = "INSERT INTO Cliente (idPessoa, dataNascimento, tipoMoradia) VALUES (?, ?, ?)";

    // Cadastrar endereço
    EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

    int idEndereco = enderecoDAO.inserirEndereco(cliente.getEndereco());

    if (idEndereco <= 0) {
        throw new SQLException("Erro ao cadastrar endereço.");
    }

    // Cadastrar pessoa
    PessoaDAO pessoaDAO = new PessoaDAO(conexao);

    int idPessoa = pessoaDAO.inserirPessoa(cliente);

    if (idPessoa <= 0) {
        throw new SQLException("Erro ao cadastrar pessoa.");
    }

    // Cadastrar cliente
    try (PreparedStatement ps = conexao.prepareStatement(sql)) {

        ps.setInt(1, idPessoa);
        ps.setDate(2, java.sql.Date.valueOf(cliente.getDataNascimento()));
        ps.setString(3, cliente.getTipoMoradia());

        if (ps.executeUpdate() == 0) {
            throw new SQLException("Erro ao cadastrar cliente.");
        }
    }

    return idPessoa;
}

    // LISTAR TODOS OS CLIENTES
    public ArrayList<Cliente> listarClientes() {

        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT p.*, c.dataNascimento, c.tipoMoradia, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep FROM Cliente c INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa INNER JOIN Endereco e ON p.idEndereco = e.idEndereco";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco!");
            return clientes;
        }

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
            JOptionPane.showMessageDialog(null, "Erro ao listar clientes: " + erro.getMessage());
        }

        return clientes;
    }

    // BUSCAR CLIENTE POR CPF
    public Cliente buscarClientePorCpf(String cpf) {

        Cliente cliente = null;

        String sql = "SELECT p.*, c.dataNascimento, c.tipoMoradia, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep FROM Cliente c INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa INNER JOIN Endereco e ON p.idEndereco = e.idEndereco WHERE p.cpf = ?";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco!");
            return null;
        }

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {

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
            }

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar cliente: " + erro.getMessage());
        }

        return cliente;
    }

    // ATUALIZAR CLIENTE
    public void atualizar(Cliente cliente) {

        String sqlVerificar = "SELECT p.idEndereco FROM Cliente c INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa WHERE c.idPessoa = ?";

        String sqlEndereco = "UPDATE Endereco SET rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE idEndereco = ? AND idEndereco = (SELECT idEndereco FROM Pessoa WHERE idPessoa = ?)";

        String sqlPessoa = "UPDATE Pessoa SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE idPessoa = ?";

        String sqlCliente = "UPDATE Cliente SET dataNascimento = ?, tipoMoradia = ? WHERE idPessoa = ?";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            // 1. Verificar cliente e endereço
            try (PreparedStatement ps = conexao.prepareStatement(sqlVerificar)) {

                ps.setInt(1, cliente.getIdPessoa());

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException("Cliente não encontrado!");
                    }

                    if (rs.getInt("idEndereco") != cliente.getEndereco().getIdEndereco()) {
                        throw new SQLException("Endereço não pertence ao cliente!");
                    }
                }
            }

            // 2. Atualizar endereço
            try (PreparedStatement ps = conexao.prepareStatement(sqlEndereco)) {

                ps.setString(1, cliente.getEndereco().getRua());
                ps.setString(2, cliente.getEndereco().getNumero());
                ps.setString(3, cliente.getEndereco().getBairro());
                ps.setString(4, cliente.getEndereco().getCidade());
                ps.setString(5, cliente.getEndereco().getEstado());
                ps.setString(6, cliente.getEndereco().getCep());
                ps.setInt(7, cliente.getEndereco().getIdEndereco());
                ps.setInt(8, cliente.getIdPessoa());

                ps.executeUpdate();
            }

            // 3. Atualizar pessoa
            try (PreparedStatement ps = conexao.prepareStatement(sqlPessoa)) {

                ps.setString(1, cliente.getNome());
                ps.setString(2, cliente.getCpf());
                ps.setString(3, cliente.getTelefone());
                ps.setString(4, cliente.getEmail());
                ps.setInt(5, cliente.getIdPessoa());

                ps.executeUpdate();
            }

            // 4. Atualizar cliente
            try (PreparedStatement ps = conexao.prepareStatement(sqlCliente)) {

                ps.setDate(1, java.sql.Date.valueOf(cliente.getDataNascimento()));
                ps.setString(2, cliente.getTipoMoradia());
                ps.setInt(3, cliente.getIdPessoa());

                ps.executeUpdate();
            }

            conexao.commit();

            JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!");

        } catch (SQLException | IllegalArgumentException erro) {

            try {
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro no rollback: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Erro ao atualizar cliente: " + erro.getMessage());

        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // REMOVER CLIENTE
    public void remover(int idPessoa) {

        String sqlBuscar = "SELECT p.idEndereco FROM Cliente c INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa WHERE c.idPessoa = ?";

        String sqlSolicitacao = "SELECT 1 FROM Solicitacao WHERE idCliente = ? LIMIT 1";

        String sqlAdocao = "SELECT 1 FROM Adocao WHERE idCliente = ? LIMIT 1";

        String sqlCliente = "DELETE FROM Cliente WHERE idPessoa = ?";

        String sqlPessoa = "DELETE FROM Pessoa WHERE idPessoa = ?";

        String sqlEndereco = "DELETE FROM Endereco WHERE idEndereco = ? AND NOT EXISTS (SELECT 1 FROM Pessoa WHERE idEndereco = ?)";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão com o banco!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            int idEndereco;

            // 1. Buscar endereço do cliente
            try (PreparedStatement ps = conexao.prepareStatement(sqlBuscar)) {

                ps.setInt(1, idPessoa);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException("Cliente não encontrado!");
                    }

                    idEndereco = rs.getInt("idEndereco");
                }
            }

            // 2. Verificar solicitações
            try (PreparedStatement ps = conexao.prepareStatement(sqlSolicitacao)) {

                ps.setInt(1, idPessoa);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        throw new SQLException("Não é possível remover um cliente que possui solicitações de adoção!");
                    }
                }
            }

            // 3. Verificar adoções
            try (PreparedStatement ps = conexao.prepareStatement(sqlAdocao)) {

                ps.setInt(1, idPessoa);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        throw new SQLException("Não é possível remover um cliente que possui adoções registradas!");
                    }
                }
            }

            // 4. Remover cliente
            try (PreparedStatement ps = conexao.prepareStatement(sqlCliente)) {

                ps.setInt(1, idPessoa);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Erro ao remover cliente!");
                }
            }

            // 5. Remover pessoa
            try (PreparedStatement ps = conexao.prepareStatement(sqlPessoa)) {

                ps.setInt(1, idPessoa);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Erro ao remover pessoa!");
                }
            }

            // 6. Remover endereço se não estiver em uso
            try (PreparedStatement ps = conexao.prepareStatement(sqlEndereco)) {

                ps.setInt(1, idEndereco);
                ps.setInt(2, idEndereco);

                ps.executeUpdate();
            }

            conexao.commit();

            JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");

        } catch (SQLException erro) {

            try {
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro no rollback: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Erro ao remover cliente: " + erro.getMessage());

        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
