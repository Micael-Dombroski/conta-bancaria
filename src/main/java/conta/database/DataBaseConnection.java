package conta.database;
import java.sql.*;

public class DataBaseConnection {
    private static final String CONEXAO = System.getenv("DB_URL");
    private static final String USUARIO = System.getenv("DB_USER");
    private static final String SENHA = System.getenv("DB_PASSWORD");

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(CONEXAO, USUARIO, SENHA);
    }
}