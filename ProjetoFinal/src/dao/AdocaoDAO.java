package dao;

import conexao.Conexao;
import model.Adocao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.Animal;
import model.Cliente;
import model.Endereco;

public class AdocaoDAO {

    private Connection conexao;

    public AdocaoDAO() {
        conexao = new Conexao().getConexao();
    }

    public boolean inserir(Adocao adocao) {

        String sql = "INSERT INTO Adocao (idAnimal, idCliente, dataAdocao) VALUES (?, ?, ?)";

        if (conexao == null) {
            return false;
        }

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, adocao.getAnimal().getIdAnimal());
            stmt.setInt(2, adocao.getCliente().getIdPessoa());
            stmt.setDate(3, Date.valueOf(adocao.getDataAdocao()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao registrar adocao: " + e.getMessage());
        }

        return false;
    }
    
    //sobrecarga
    public boolean inserir(Adocao adocao, Connection conexao)
        throws SQLException {

    String sql = "INSERT INTO Adocao (idAnimal, idCliente, dataAdocao) VALUES (?, ?, ?)";

    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

        stmt.setInt(1, adocao.getAnimal().getIdAnimal());
        stmt.setInt(2, adocao.getCliente().getIdPessoa());
        stmt.setDate(3, Date.valueOf(adocao.getDataAdocao()));

        return stmt.executeUpdate() > 0;
    }
}

    private final String SQL_CONSULTA
            = "SELECT ad.*, a.nome AS nomeAnimal, a.especie, a.raca, "
            + "a.idadeAproximada, a.sexo, a.porte, a.status, "
            + "p.nome AS nomeCliente, p.cpf, p.telefone, p.email, "
            + "c.dataNascimento, c.tipoMoradia, "
            + "e.idEndereco, e.rua, e.numero, e.bairro, "
            + "e.cidade, e.estado, e.cep "
            + "FROM Adocao ad "
            + "INNER JOIN Animal a ON ad.idAnimal = a.idAnimal "
            + "INNER JOIN Cliente c ON ad.idCliente = c.idPessoa "
            + "INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa "
            + "INNER JOIN Endereco e ON p.idEndereco = e.idEndereco";

    private Adocao montarAdocao(ResultSet rs) throws SQLException {

        Animal animal = new Animal(
                rs.getInt("idAnimal"),
                rs.getString("nomeAnimal"),
                rs.getString("especie"),
                rs.getString("raca"),
                rs.getInt("idadeAproximada"),
                rs.getString("sexo"),
                rs.getString("porte")
        );

        animal.setStatus(rs.getString("status"));

        Endereco endereco = new Endereco(
                rs.getInt("idEndereco"),
                rs.getString("rua"),
                rs.getString("numero"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("cep")
        );

        Cliente cliente = new Cliente(
                rs.getString("dataNascimento"),
                rs.getString("tipoMoradia"),
                rs.getInt("idCliente"),
                rs.getString("nomeCliente"),
                rs.getString("cpf"),
                rs.getString("telefone"),
                rs.getString("email"),
                endereco
        );

        return new Adocao(
                rs.getInt("idAdocao"),
                rs.getString("dataAdocao"),
                animal,
                cliente
        );
    }

    public ArrayList<Adocao> consultarTodos() {

        ArrayList<Adocao> lista = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(SQL_CONSULTA); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarAdocao(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar adocoes: " + e.getMessage());
        }

        return lista;
    }

    public Adocao consultarPorId(int idAdocao) {

        String sql = SQL_CONSULTA + " WHERE ad.idAdocao = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idAdocao);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return montarAdocao(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar adocao: " + e.getMessage());
        }

        return null;
    }

    public ArrayList<Adocao> consultarPorCliente(int idCliente) {

        ArrayList<Adocao> lista = new ArrayList<>();

        String sql = SQL_CONSULTA + " WHERE ad.idCliente = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(montarAdocao(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar adocoes: " + e.getMessage());
        }

        return lista;
    }

    public boolean verificarAnimalAdotado(int idAnimal) {

        String sql = "SELECT idAdocao FROM Adocao WHERE idAnimal = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idAnimal);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erro ao verificar adocao: " + e.getMessage());
            throw new IllegalStateException("Falha ao verificar adocao", e);
        }
    }
}
