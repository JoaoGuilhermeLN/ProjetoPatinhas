package dao;

import conexao.Conexao;
import model.Solicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import java.sql.ResultSet;
import java.util.ArrayList;

public class SolicitacaoDAO {

    private Connection conexao;

    public SolicitacaoDAO() {
        conexao = new Conexao().getConexao();
    }

    public boolean inserir(Solicitacao solicitacao) {

        String sql = "INSERT INTO Solicitacao (idCliente, idAnimal, dataSolicitacao, status) VALUES (?, ?, ?, ?)";

        if (conexao == null) {
            System.out.println("Erro: conexão não estabelecida.");
            return false;
        }

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, solicitacao.getIdCliente());
            stmt.setInt(2, solicitacao.getIdAnimal());
            stmt.setDate(3, Date.valueOf(solicitacao.getDataSolicitacao()));
            stmt.setString(4, solicitacao.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir solicitação: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Solicitacao> consultarTodos() {

        ArrayList<Solicitacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM Solicitacao";

        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("idCliente"),
                        rs.getInt("idAnimal")
                );

                solicitacao.setIdSolicitacao(rs.getInt("idSolicitacao"));

                solicitacao.setDataSolicitacao(
                        rs.getDate("dataSolicitacao").toLocalDate()
                );

                solicitacao.setStatus(rs.getString("status"));

                lista.add(solicitacao);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar solicitacoes: " + e.getMessage());
        }

        return lista;
    }

    public Solicitacao consultarPorId(int idSolicitacao) {

        String sql = "SELECT * FROM Solicitacao WHERE idSolicitacao = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitacao);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("idCliente"),
                        rs.getInt("idAnimal")
                );

                solicitacao.setIdSolicitacao(rs.getInt("idSolicitacao"));

                solicitacao.setDataSolicitacao(
                        rs.getDate("dataSolicitacao").toLocalDate()
                );

                solicitacao.setStatus(rs.getString("status"));

                return solicitacao;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar solicitacao: " + e.getMessage());
        }

        return null;
    }
    
    //sobrecarga
    public Solicitacao consultarPorId(int idSolicitacao,
        Connection conexao) throws SQLException {

    String sql = "SELECT * FROM Solicitacao WHERE idSolicitacao = ?";

    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

        stmt.setInt(1, idSolicitacao);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            Solicitacao solicitacao = new Solicitacao(
                    rs.getInt("idCliente"),
                    rs.getInt("idAnimal")
            );

            solicitacao.setIdSolicitacao(rs.getInt("idSolicitacao"));

            solicitacao.setDataSolicitacao(
                    rs.getDate("dataSolicitacao").toLocalDate()
            );

            solicitacao.setStatus(rs.getString("status"));

            return solicitacao;
        }
    }

    return null;
}

    public ArrayList<Solicitacao> consultarPorCliente(int idCliente) {

        ArrayList<Solicitacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM Solicitacao WHERE idCliente = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("idCliente"),
                        rs.getInt("idAnimal")
                );

                solicitacao.setIdSolicitacao(rs.getInt("idSolicitacao"));

                solicitacao.setDataSolicitacao(
                        rs.getDate("dataSolicitacao").toLocalDate()
                );

                solicitacao.setStatus(rs.getString("status"));

                lista.add(solicitacao);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar solicitacoes: " + e.getMessage());
        }

        return lista;
    }

    public boolean atualizarStatus(int idSolicitacao, String status) {

        String sql = "UPDATE Solicitacao SET status = ? WHERE idSolicitacao = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idSolicitacao);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status: " + e.getMessage());
        }

        return false;
    }
    
    //sobrecarga
    public boolean atualizarStatus(int idSolicitacao, String status,
            Connection conexao) throws SQLException {

        String sql = "UPDATE Solicitacao SET status = ? "
                + "WHERE idSolicitacao = ? AND status = 'Pendente'";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idSolicitacao);

            return stmt.executeUpdate() > 0;
        }
    }
}
