package dao;

import conexao.Conexao;
import model.Solicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

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
}
