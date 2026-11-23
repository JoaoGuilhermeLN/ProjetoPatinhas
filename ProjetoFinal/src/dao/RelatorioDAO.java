package dao;

import conexao.Conexao;
import model.Pessoa;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class RelatorioDAO {

    Connection conexao;

    public RelatorioDAO() {
        conexao = new Conexao().getConexao();
    }

    public RelatorioDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public DefaultTableModel consultarAnimais() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Espécie");
        modelo.addColumn("Raça");
        modelo.addColumn("Status");

        String sql = "SELECT * FROM Animal WHERE status = 'Disponivel'";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idAnimal"),
                    rs.getString("nome"),
                    rs.getString("especie"),
                    rs.getString("raca"),
                    rs.getString("status")
                });
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel consultarAnimaisEspecie() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Especie");
        modelo.addColumn("Quantidade");

        String sql = "SELECT Especie, COUNT(*) AS quantidade "
                + "FROM Animal GROUP BY especie";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("Especie"),
                    rs.getInt("Quantidade"),});
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel consultarAnimaisStatus() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Status");
        modelo.addColumn("Quantidade");

        String sql = "SELECT Status, COUNT(*) AS quantidade "
                + "FROM Animal GROUP BY status";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("Status"),
                    rs.getInt("Quantidade"),});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return modelo;
    }

    public DefaultTableModel consultarClientesAnimais() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Animal");
        modelo.addColumn("Cliente");
        modelo.addColumn("Data da adoção");

        String sql = "SELECT a.nome AS animal, "
                + "p.nome AS cliente, ad.dataAdocao "
                + "FROM Adocao ad "
                + "INNER JOIN Animal a ON ad.idAnimal = a.idAnimal "
                + "INNER JOIN Cliente c ON ad.idCliente = c.idPessoa "
                + "INNER JOIN Pessoa p ON c.idPessoa = p.idPessoa";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Sem conexão com o banco!");
            return modelo;
        }

        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("animal"),
                    rs.getString("cliente"),
                    rs.getDate("dataAdocao")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel consultarClientesEndereco() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Nome");
        modelo.addColumn("CPF");
        modelo.addColumn("Telefone");
        modelo.addColumn("Email");
        modelo.addColumn("Rua");
        modelo.addColumn("Número");
        modelo.addColumn("Bairro");
        modelo.addColumn("Cidade");
        modelo.addColumn("Estado");
        modelo.addColumn("CEP");

        String sql = "SELECT p.nome, p.cpf, p.telefone, p.email, "
                + "e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep "
                + "FROM Pessoa p "
                + "INNER JOIN Cliente c ON p.idPessoa = c.idPessoa "
                + "INNER JOIN Endereco e ON p.idEndereco = e.idEndereco";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Sem conexão com o banco!");
            return modelo;
        }

        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("rua"),
                    rs.getString("numero"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("estado"),
                    rs.getString("cep")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel consultarFuncionariosCargo() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Nome");
        modelo.addColumn("Matrícula");
        modelo.addColumn("Cargo");

        String sql = "SELECT p.nome, f.matricula, f.cargo "
                + "FROM Pessoa p "
                + "INNER JOIN Funcionario f ON p.idPessoa = f.idPessoa "
                + "ORDER BY f.cargo";

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Sem conexão com o banco!");
            return modelo;
        }

        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nome"),
                    rs.getInt("matricula"),
                    rs.getString("cargo")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return modelo;
    }
}
