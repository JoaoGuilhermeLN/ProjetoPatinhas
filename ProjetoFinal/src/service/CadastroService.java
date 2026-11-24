package service;

import conexao.Conexao;
import dao.ClienteDAO;
import dao.UsuarioDAO;
import model.Cliente;
import model.Usuario;
import java.sql.Connection;
import java.sql.SQLException;

public class CadastroService {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean cadastrarCliente(Cliente cliente, String login, String senha) {

        if (cliente == null || login == null || senha == null
                || login.trim().isEmpty() || senha.trim().isEmpty()) {
            return false;
        }

        new Conexao();

        try (Connection conexao = Conexao.getConexao()) {

            if (conexao == null) {
                return false;
            }

            conexao.setAutoCommit(false);

            try {

                int idPessoa = clienteDAO.inserirCliente(cliente, conexao);

                Usuario usuario = new Usuario(
                        0,
                        idPessoa,
                        login,
                        senha,
                        "CLIENTE"
                );

                usuarioDAO.inserirUsuario(usuario, conexao);

                conexao.commit();

                return true;

            } catch (SQLException | RuntimeException e) {

                conexao.rollback();

                System.out.println("Erro no cadastro: " + e.getMessage());

                return false;
            }

        } catch (SQLException e) {

            System.out.println("Erro na conexao: " + e.getMessage());

            return false;
        }
    }
}