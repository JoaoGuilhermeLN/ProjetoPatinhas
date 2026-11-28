package dao;

import conexao.Conexao;
import model.Funcionario;
import model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class FuncionarioDAO {

    Connection conexao;

    public FuncionarioDAO() {
        conexao = new Conexao().getConexao();
    }

    // CADASTRAR FUNCIONÁRIO
    public void inserirFuncionario(Funcionario funcionario) {

        String sql = "INSERT INTO Funcionario (idPessoa, cargo, matricula) VALUES (?, ?, ?)";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);
            int idEndereco = enderecoDAO.inserirEndereco(funcionario.getEndereco());

            if (idEndereco <= 0) {
                throw new SQLException("Erro ao cadastrar endereço.");
            }

            PessoaDAO pessoaDAO = new PessoaDAO(conexao);
            int idPessoa = pessoaDAO.inserirPessoa(funcionario);

            if (idPessoa <= 0) {
                throw new SQLException("Erro ao cadastrar pessoa.");
            }

            try (PreparedStatement ps = conexao.prepareStatement(sql)) {

                ps.setInt(1, idPessoa);
                ps.setString(2, funcionario.getCargo());
                ps.setInt(3, funcionario.getMatricula());

                int linhas = ps.executeUpdate();

                if (linhas == 0) {
                    throw new SQLException("Funcionário não cadastrado!");
                }
            }

            conexao.commit();

            JOptionPane.showMessageDialog(null, "Funcionário cadastrado com sucesso!");

        } catch (SQLException | IllegalArgumentException erro) {

            try {
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro no rollback: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Erro ao cadastrar funcionário: " + erro.getMessage());

        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    //sobrecarga pra cadastroService
    public int inserirFuncionario(Funcionario funcionario, Connection conexao)
            throws SQLException {

        String sql = "INSERT INTO Funcionario "
                + "(idPessoa, cargo, matricula) VALUES (?, ?, ?)";

        EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

        int idEndereco = enderecoDAO.inserirEndereco(
                funcionario.getEndereco());

        if (idEndereco <= 0) {
            throw new SQLException("Erro ao cadastrar endereço.");
        }

        funcionario.getEndereco().setIdEndereco(idEndereco);

        PessoaDAO pessoaDAO = new PessoaDAO(conexao);

        int idPessoa = pessoaDAO.inserirPessoa(funcionario);

        if (idPessoa <= 0) {
            throw new SQLException("Erro ao cadastrar pessoa.");
        }

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idPessoa);
            ps.setString(2, funcionario.getCargo());
            ps.setInt(3, funcionario.getMatricula());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Funcionário não cadastrado!");
            }
        }

        return idPessoa;
    }

    // CONSULTAR TODOS OS FUNCIONÁRIOS
    public ArrayList<Funcionario> consultarTodos() {

        ArrayList<Funcionario> funcionarios = new ArrayList<>();

        String sql = "SELECT p.*, f.cargo, f.matricula, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep FROM Funcionario f INNER JOIN Pessoa p ON f.idPessoa = p.idPessoa INNER JOIN Endereco e ON p.idEndereco = e.idEndereco";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão!");
            return funcionarios;
        }

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

                Funcionario funcionario = new Funcionario(
                        rs.getString("cargo"),
                        rs.getInt("matricula"),
                        rs.getInt("idPessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        endereco
                );

                funcionarios.add(funcionario);
            }

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar funcionários: " + erro.getMessage());
        }

        return funcionarios;
    }

    // CONSULTAR FUNCIONÁRIO POR ID
    public Funcionario consultarPorId(int idPessoa) {

        Funcionario funcionario = null;

        String sql = "SELECT p.*, f.cargo, f.matricula, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep FROM Funcionario f INNER JOIN Pessoa p ON f.idPessoa = p.idPessoa INNER JOIN Endereco e ON p.idEndereco = e.idEndereco WHERE f.idPessoa = ?";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão!");
            return null;
        }

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idPessoa);

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

                    funcionario = new Funcionario(
                            rs.getString("cargo"),
                            rs.getInt("matricula"),
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
            JOptionPane.showMessageDialog(null, "Erro ao consultar funcionário: " + erro.getMessage());
        }

        return funcionario;
    }

    // ATUALIZAR FUNCIONÁRIO
    public void atualizar(Funcionario f) {

        String sqlEndereco = "UPDATE Endereco SET rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE idEndereco = ? AND idEndereco = (SELECT idEndereco FROM Pessoa WHERE idPessoa = ?)";

        String sqlPessoa = "UPDATE Pessoa SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE idPessoa = ?";

        String sqlFuncionario = "UPDATE Funcionario SET cargo = ?, matricula = ? WHERE idPessoa = ?";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            // Verificar se o funcionário existe e qual é seu endereço
            String sqlVerificar = "SELECT p.idEndereco FROM Funcionario f INNER JOIN Pessoa p ON f.idPessoa = p.idPessoa WHERE f.idPessoa = ?";

            try (PreparedStatement ps = conexao.prepareStatement(sqlVerificar)) {
                ps.setInt(1, f.getIdPessoa());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Funcionário não encontrado!");
                    }

                    if (rs.getInt("idEndereco") != f.getEndereco().getIdEndereco()) {
                        throw new SQLException("Endereço não pertence ao funcionário!");
                    }
                }
            }

            // Atualizar endereço
            try (PreparedStatement ps = conexao.prepareStatement(sqlEndereco)) {

                ps.setString(1, f.getEndereco().getRua());
                ps.setString(2, f.getEndereco().getNumero());
                ps.setString(3, f.getEndereco().getBairro());
                ps.setString(4, f.getEndereco().getCidade());
                ps.setString(5, f.getEndereco().getEstado());
                ps.setString(6, f.getEndereco().getCep());
                ps.setInt(7, f.getEndereco().getIdEndereco());
                ps.setInt(8, f.getIdPessoa());

                ps.executeUpdate();
            }

            // Atualizar pessoa
            try (PreparedStatement ps = conexao.prepareStatement(sqlPessoa)) {

                ps.setString(1, f.getNome());
                ps.setString(2, f.getCpf());
                ps.setString(3, f.getTelefone());
                ps.setString(4, f.getEmail());
                ps.setInt(5, f.getIdPessoa());

                ps.executeUpdate();
            }

            // Atualizar funcionário
            try (PreparedStatement ps = conexao.prepareStatement(sqlFuncionario)) {

                ps.setString(1, f.getCargo());
                ps.setInt(2, f.getMatricula());
                ps.setInt(3, f.getIdPessoa());

                ps.executeUpdate();
            }

            conexao.commit();

            JOptionPane.showMessageDialog(null, "Funcionário atualizado com sucesso!");

        } catch (SQLException | IllegalArgumentException erro) {

            try {
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro no rollback: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + erro.getMessage());

        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // REMOVER FUNCIONÁRIO
    public void remover(int idPessoa) {

        String sqlBuscar = "SELECT p.idEndereco FROM Funcionario f INNER JOIN Pessoa p ON f.idPessoa = p.idPessoa WHERE f.idPessoa = ?";

        String sqlFuncionario = "DELETE FROM Funcionario WHERE idPessoa = ?";

        String sqlPessoa = "DELETE FROM Pessoa WHERE idPessoa = ?";

        String sqlEndereco = "DELETE FROM Endereco WHERE idEndereco = ? AND NOT EXISTS (SELECT 1 FROM Pessoa WHERE idEndereco = ?)";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Erro na conexão!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            int idEndereco;

            // Buscar endereço
            try (PreparedStatement ps = conexao.prepareStatement(sqlBuscar)) {

                ps.setInt(1, idPessoa);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException("Funcionário não encontrado!");
                    }

                    idEndereco = rs.getInt("idEndereco");
                }
            }

            // Remover funcionário
            try (PreparedStatement ps = conexao.prepareStatement(sqlFuncionario)) {

                ps.setInt(1, idPessoa);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Erro ao remover funcionário!");
                }
            }

            // Remover pessoa
            try (PreparedStatement ps = conexao.prepareStatement(sqlPessoa)) {

                ps.setInt(1, idPessoa);

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Erro ao remover pessoa!");
                }
            }

            // Remover endereço se não estiver em uso
            try (PreparedStatement ps = conexao.prepareStatement(sqlEndereco)) {

                ps.setInt(1, idEndereco);
                ps.setInt(2, idEndereco);

                ps.executeUpdate();
            }

            conexao.commit();

            JOptionPane.showMessageDialog(null, "Funcionário removido com sucesso!");

        } catch (SQLException erro) {

            try {
                conexao.rollback();
            } catch (SQLException e) {
                System.out.println("Erro no rollback: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Erro ao remover: " + erro.getMessage());

        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
