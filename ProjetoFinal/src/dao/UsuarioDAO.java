package dao;

import conexao.Conexao;
import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UsuarioDAO {

    public void inserirUsuario(Usuario usuario) {

        String sql = "INSERT INTO Usuario (idPessoa, login, senha, perfil) VALUES (?, ?, ?, ?)";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getIdPessoa());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Usuário cadastrado com sucesso!");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,
                    "Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
    
    //sobrecarga de metodos
    public void inserirUsuario(Usuario usuario, Connection conexao)
        throws SQLException {

    String sql = "INSERT INTO Usuario (idPessoa, login, senha, perfil) VALUES (?, ?, ?, ?)";

    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

        stmt.setInt(1, usuario.getIdPessoa());
        stmt.setString(2, usuario.getLogin());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getPerfil());

        if (stmt.executeUpdate() == 0) {
            throw new SQLException("Erro ao cadastrar usuario.");
        }
    }
}

    public Usuario buscarPorLogin(String login) {

        String sql = "SELECT * FROM Usuario WHERE login = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getInt("idPessoa"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );

                return usuario;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public Usuario buscarPorIdPessoa(int idPessoa) {

        String sql = "SELECT * FROM Usuario WHERE idPessoa = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPessoa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getInt("idPessoa"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );

                return usuario;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public void atualizarSenha(int idUsuario, String novaSenha) {

        String sql = "UPDATE Usuario SET senha = ? WHERE idUsuario = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novaSenha);
            stmt.setInt(2, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Senha atualizada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Usuário não encontrado!");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,
                    "Erro ao atualizar senha: " + e.getMessage());
        }
    }

    public void remover(int idUsuario) {

        String sql = "DELETE FROM Usuario WHERE idUsuario = ?";

        try (Connection conexao = Conexao.getConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {

                JOptionPane.showMessageDialog(null,
                        "Usuário removido com sucesso!");

            } else {

                JOptionPane.showMessageDialog(null,
                        "Usuário não encontrado!");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,
                    "Erro ao remover usuário: " + e.getMessage());
        }
    }
}
