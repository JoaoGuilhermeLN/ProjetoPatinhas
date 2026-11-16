package conexao;

import java.sql.*;
import java.io.FileInputStream;
import java.util.Properties;

public class Conexao {

    static Connection conexao = null;
    String user;
    String password;

    String database = "Patinhas";
    String url = "jdbc:mysql://localhost:3306/" + database;

    public Conexao() {
        try {

            // Ler o arquivo que guarda o usuario e a senha
            Properties config = new Properties();
            FileInputStream arquivo = new FileInputStream("config.properties");
            config.load(arquivo);
            arquivo.close();

            // Pegar o usuario e a senha do arquivo
            user = config.getProperty("db.user");
            password = config.getProperty("db.password");
            conexao = DriverManager.getConnection(url, user, password);
            System.out.println("Conexao realizada com sucesso");
        } catch (SQLException | java.io.IOException e) {
            System.out.println("Problema na conexao: " + e.getMessage());
        }
    }

    public static Connection getConexao() {
        return conexao;
    }
}
