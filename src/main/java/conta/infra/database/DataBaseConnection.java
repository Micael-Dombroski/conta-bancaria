package conta.database;
import java.sql.*;

public class DataBaseConnection {
    private static final String CONEXAO = System.getenv("DB_URL");
    private static final String USUARIO = System.getenv("DB_USER");
    private static final String SENHA = System.getenv("DB_PASSWORD");

    public static Connection conectar() throws SQLException {
        if (CONEXAO == null || CONEXAO.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente CONEXAO nao definida");
        }
        if (USUARIO == null || USUARIO.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente USUARIO nao definida");
        }
        if (SENHA == null || SENHA.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente SENHA nao definida");
        }
        return DriverManager.getConnection(CONEXAO, USUARIO, SENHA);
    }
}