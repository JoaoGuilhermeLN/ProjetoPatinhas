package dao;

import conexao.Conexao;
import model.Endereco;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class EnderecoDAO {

    Connection conexao;

    public EnderecoDAO() {
        conexao = new Conexao().getConexao();
    }
    
     public EnderecoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    //inserir os dados do endereco no mysql
    
public int inserirEndereco(Endereco endereco) {

    String sql = "INSERT INTO Endereco (rua, numero, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = conexao.prepareStatement(
            sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, endereco.getRua());
        ps.setString(2, endereco.getNumero());
        ps.setString(3, endereco.getBairro());
        ps.setString(4, endereco.getCidade());
        ps.setString(5, endereco.getEstado());
        ps.setString(6, endereco.getCep());

        int linhas = ps.executeUpdate();

        if (linhas > 0) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    endereco.setIdEndereco(idGerado);
                    return idGerado;
                }
            }
        }

    } catch (SQLException erro) {
        System.out.println("Erro ao cadastrar endereço: "
                + erro.getMessage());
    }

    return -1;
}


    //listar todos os enderecos
    public ArrayList<Endereco> consultarTodosEnderecos() {

        ArrayList<Endereco> enderecos = new ArrayList<Endereco>();

        try {
            String sql = "SELECT * FROM Endereco";
            PreparedStatement ps = conexao.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Endereco e = new Endereco(
                        rs.getInt("idEndereco"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );

                enderecos.add(e);

            }
        } catch (SQLException erro) {
            System.out.println("Erro ao consultar endereços: " + erro.getMessage());
        }

        return enderecos;

    }

    public Endereco consultarEnderecoPorId(int idEndereco) {
        Endereco endereco = null;
        try {
            String sql = "SELECT * FROM Endereco WHERE idEndereco = ?";

            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setInt(1, idEndereco);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                endereco = new Endereco(
                        rs.getInt("idEndereco"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep")
                );
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar endereço: " + erro.getMessage());
        }

        return endereco;
    }

    public void atualizarEndereco(Endereco e) {

        try {

            String sql = "Update Endereco set rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE idEndereco = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, e.getRua());
            ps.setString(2, e.getNumero());
            ps.setString(3, e.getBairro());
            ps.setString(4, e.getCidade());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getCep());
            ps.setInt(7, e.getIdEndereco());

            int linhas = ps.executeUpdate();

            if (linhas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Endereço atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Endereço não encontrado!");
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null,
                    "Erro na atualização: " + erro.getMessage());
        }
    }
    
    
public void removerEndereco(int idEndereco) {

    String verificar = "SELECT COUNT(*) FROM Pessoa WHERE idEndereco = ?";
    String remover = "DELETE FROM Endereco WHERE idEndereco = ?";

    try {
        
        // Verifica se o endereço está vinculado a alguma pessoa
        try (PreparedStatement ps = conexao.prepareStatement(verificar)) {

            ps.setInt(1, idEndereco);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Não é possível remover um endereço vinculado a uma pessoa!");
                    return;
                }
            }
        }

        // Remove o endereço
        try (PreparedStatement ps = conexao.prepareStatement(remover)) {

            ps.setInt(1, idEndereco);

            int linhas = ps.executeUpdate();

            if (linhas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Endereço removido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Endereço não encontrado!");
            }
        }

    } catch (SQLException erro) {
        JOptionPane.showMessageDialog(null,
                "Erro ao remover endereço: " + erro.getMessage());
    }
}

}

