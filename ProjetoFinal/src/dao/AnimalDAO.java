package dao;

import conexao.Conexao;
import model.Animal;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class AnimalDAO {

    Connection conexao;

    public AnimalDAO() {
        conexao = new Conexao().getConexao();
    }

    //inserir os dados de um animal no mysql
    public void inserir(Animal a) {
        try {
            String sql = "insert into animal (nome, especie, raca, idadeAproximada, sexo, porte, status) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, a.getNomeAnimal());
            ps.setString(2, a.getEspecie());
            ps.setString(3, a.getRaca());
            ps.setInt(4, a.getIdadeAproximada());
            ps.setString(5, a.getSexo());
            ps.setString(6, a.getPorte());
            ps.setString(7, a.getStatus());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cadastro de animail realizado");
        } catch (Exception e) {
            System.out.println("Erro na insercao: " + e);
        }
    }

    // listar todos os animais
    public ArrayList<Animal> consultarTodos() {

        ArrayList<Animal> animais = new ArrayList<Animal>();

        try {
            String sql = "SELECT * FROM animal";
            PreparedStatement ps = conexao.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Animal a = new Animal(
                        rs.getInt("idAnimal"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getString("raca"),
                        rs.getInt("idadeAproximada"),
                        rs.getString("sexo"),
                        rs.getString("porte")
                );

                a.setStatus(rs.getString("status"));

                animais.add(a);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro na consulta: " + e.getMessage());
        }

        return animais;
    }

    // procurar animal pelo id
    public Animal consultarPorId(int idAnimal) {

        Animal animal = null;

        try {
            String sql = "SELECT * FROM animal WHERE idAnimal = ?";

            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setInt(1, idAnimal);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                animal = new Animal(
                        rs.getInt("idAnimal"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getString("raca"),
                        rs.getInt("idadeAproximada"),
                        rs.getString("sexo"),
                        rs.getString("porte")
                );

                animal.setStatus(rs.getString("status"));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());

        }

        return animal;

    }

    // atualizar algum dado
    public void atualizar(Animal a) {

        try {
            String sql = "UPDATE animal SET nome = ?, especie = ?, raca = ?, idadeAproximada = ?, sexo = ?, porte = ? WHERE idAnimal = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);

            ps.setString(1, a.getNomeAnimal());
            ps.setString(2, a.getEspecie());
            ps.setString(3, a.getRaca());
            ps.setInt(4, a.getIdadeAproximada());
            ps.setString(5, a.getSexo());
            ps.setString(6, a.getPorte());
            ps.setInt(7, a.getIdAnimal());

            int linhas = ps.executeUpdate();

            if (linhas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Animal atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Animal não encontrado!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro na atualização: " + e.getMessage());
        }
    }

    public void atualizarStatus(int idAnimal, String status) {

        String sql = "UPDATE Animal SET status = ? WHERE idAnimal = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, idAnimal);

            int linhas = ps.executeUpdate();

            if (linhas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Status atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Animal não encontrado!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao atualizar status: " + e.getMessage());
        }
    }

//sobrecarga
    public boolean atualizarStatus(int idAnimal, String status,
            Connection conexao) throws SQLException {

        String sql = "UPDATE Animal SET status = ? "
                + "WHERE idAnimal = ? AND status = 'Disponivel'";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idAnimal);

            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Animal> consultarDisponiveis() {

        ArrayList<Animal> animais = new ArrayList<Animal>();

        try {
            String sql = "SELECT * FROM animal WHERE status = 'Disponivel'";
            PreparedStatement ps = conexao.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Animal a = new Animal(
                        rs.getInt("idAnimal"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getString("raca"),
                        rs.getInt("idadeAproximada"),
                        rs.getString("sexo"),
                        rs.getString("porte")
                );

                a.setStatus(rs.getString("status"));

                animais.add(a);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        }

        return animais;
    }

    public void remover(int idAnimal) {

        String verificar = "SELECT COUNT(*) FROM Adocao WHERE idAnimal = ?";
        String remover = "DELETE FROM Animal WHERE idAnimal = ?";

        try {

            // Verifica se o animal possui adoção registrada
            try (PreparedStatement ps = conexao.prepareStatement(verificar)) {

                ps.setInt(1, idAnimal);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(null,
                                "Não é possível remover um animal com adoção registrada!");
                        return;
                    }
                }
            }

            // Remove o animal
            try (PreparedStatement ps = conexao.prepareStatement(remover)) {

                ps.setInt(1, idAnimal);

                int linhas = ps.executeUpdate();

                if (linhas > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Animal removido com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Animal não encontrado!");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao remover animal: " + e.getMessage());
        }
    }

}
