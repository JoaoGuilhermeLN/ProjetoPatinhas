package service;

import conexao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

public class AutenticacaoService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String login, String senha) {

        if (login == null || senha == null ||
                login.trim().isEmpty() || senha.trim().isEmpty()) {

            return null;
        }

        new Conexao();

        Usuario usuario = usuarioDAO.buscarPorLogin(login);

        if (usuario == null) {
            return null;
        }

        if (!usuario.getSenha().equals(senha)) {
            return null;
        }

        return usuario;
    }
}