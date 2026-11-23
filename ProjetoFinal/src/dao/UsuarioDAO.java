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

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

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
}