package service;

import dao.AnimalDAO;
import dao.SolicitacaoDAO;
import dao.AdocaoDAO;
import model.Animal;
import model.Solicitacao;
import model.Adocao;
import model.Cliente;
import model.Usuario;
import conexao.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import java.sql.PreparedStatement;
import java.sql.Date;

public class AdocaoService {

    public boolean solicitarAdocao(Usuario usuario, int idAnimal) {

        if (usuario == null
                || !"CLIENTE".equals(usuario.getPerfil())) {
            return false;
        }

        AnimalDAO animalDAO = new AnimalDAO();
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

        Animal animal = animalDAO.consultarPorId(idAnimal);

        if (animal == null) {
            return false;
        }

        if (!"Disponivel".equals(animal.getStatus())) {
            return false;
        }

        int idCliente = usuario.getIdPessoa();

        for (Solicitacao s : solicitacaoDAO.consultarPorCliente(idCliente)) {

            if (s.getIdAnimal() == idAnimal
                    && "Pendente".equals(s.getStatus())) {
                return false;
            }
        }

        Solicitacao solicitacao = new Solicitacao(idCliente, idAnimal);

        return solicitacaoDAO.inserir(solicitacao);
    }

    public boolean recusarSolicitacao(Usuario usuario, int idSolicitacao) {

        if (usuario == null
                || !"FUNCIONARIO".equals(usuario.getPerfil())) {
            return false;
        }

        new Conexao();

        try (Connection conexao = Conexao.getConexao()) {

            if (conexao == null) {
                return false;
            }

            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

            return solicitacaoDAO.atualizarStatus(
                    idSolicitacao, "Recusada", conexao
            );

        } catch (SQLException e) {
            System.out.println("Erro ao recusar solicitacao: " + e.getMessage());
        }

        return false;
    }

    public boolean aprovarSolicitacao(Usuario usuario, int idSolicitacao) {

        if (usuario == null
                || !"FUNCIONARIO".equals(usuario.getPerfil())) {
            return false;
        }

        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
        AnimalDAO animalDAO = new AnimalDAO();

        new Conexao();

        try (Connection conexao = Conexao.getConexao()) {

            if (conexao == null) {
                return false;
            }

            conexao.setAutoCommit(false);

            try {

                // 1. Buscar a solicitacao
                Solicitacao solicitacao = solicitacaoDAO.consultarPorId(
                        idSolicitacao, conexao
                );

                if (solicitacao == null
                        || !"Pendente".equals(solicitacao.getStatus())) {
                    throw new SQLException("Solicitacao invalida.");
                }

                // 2. Atualizar o status do animal
                boolean animalAtualizado = animalDAO.atualizarStatus(
                        solicitacao.getIdAnimal(), "Adotado", conexao
                );

                if (!animalAtualizado) {
                    throw new SQLException("Animal indisponivel.");
                }

                // 3. Registrar a adocao
                String sql = "INSERT INTO Adocao "
                        + "(idAnimal, idCliente, dataAdocao) "
                        + "VALUES (?, ?, ?)";

                try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

                    stmt.setInt(1, solicitacao.getIdAnimal());
                    stmt.setInt(2, solicitacao.getIdCliente());
                    stmt.setDate(3, Date.valueOf(LocalDate.now()));

                    if (stmt.executeUpdate() == 0) {
                        throw new SQLException("Erro ao registrar adocao.");
                    }
                }

                // 4. Aprovar a solicitacao
                boolean aprovada = solicitacaoDAO.atualizarStatus(
                        idSolicitacao, "Aprovada", conexao
                );

                if (!aprovada) {
                    throw new SQLException("Erro ao aprovar solicitacao.");
                }

                conexao.commit();

                return true;

            } catch (SQLException | RuntimeException e) {

                conexao.rollback();

                System.out.println("Erro na adocao: " + e.getMessage());

                return false;
            }

        } catch (SQLException e) {
            System.out.println("Erro na conexao: " + e.getMessage());
        }

        return false;
    }
}
