
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
            conexao.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return modelo;
    }

    public TableModel botaoAnimaisDis() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public DefaultTableModel consultarAnimaisStatus(){
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        modelo.addColumn("Especie");
        modelo.addColumn("Quantidade");
        
        String sql = "SELECT Especie, COUNT (*) AS quantidade"
                + "FROM Animal GROUP BY especie";
                
        try {
              PreparedStatement stmt = conexao.prepareStatement(sql);
              ResultSet rs = stmt.executeQuery(); 
              
              while(rs.next()){
                  modelo.addRow(new Object[]{
                    rs.getString("Especie"),
                    rs.getInt("Quantidade"),
                });
              }
              rs.close();
              stmt.close();
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        return modelo;
    }
}
