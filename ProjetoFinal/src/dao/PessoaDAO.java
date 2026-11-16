
package dao;

import conexao.Conexao;
import model.Pessoa;
import java.sql.*;

public class PessoaDAO {

    Connection conexao;

    public PessoaDAO() {
        conexao = new Conexao().getConexao();
    }
     
    public PessoaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public int inserirPessoa(Pessoa p) {

        String sql = "INSERT INTO Pessoa (idEndereco, nome, cpf, telefone, email) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getEndereco().getIdEndereco());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getCpf());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());

            int linhas = ps.executeUpdate();

            if (linhas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        int idGerado = rs.getInt(1);
                        p.setIdPessoa(idGerado);
                        return idGerado;
                    }
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar pessoa: " +erro.getMessage());
        }

        return -1;
    }
}
