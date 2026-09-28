package conta.dao;

import conta.database.DataBaseConnection;
import conta.domain.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnderecoDAO {
    public EnderecoDAO(){
    }

    public void adicionar(Endereco endereco)  {
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = "SELECT 1 FROM enderecos WHERE cep = ? AND numero = ?";
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, endereco.getCep());
                ps.setString(2, endereco.getNumero());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Endereco já existe");
                        return;
                    }
                }
            }
            sql = """
                INSERT INTO enderecos
                (cep, logradouro, complemento, numero, bairro, cidade, uf)
                VALUES(?,?,?,?,?,?,?)""";
            try(PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, endereco.getCep());
                ps.setString(2, endereco.getLogradouro());
                ps.setString(3, endereco.getComplemento());
                ps.setString(4, endereco.getNumero() == null ?
                        "N/A" : endereco.getNumero());
                ps.setString(5, endereco.getBairro());
                ps.setString(6, endereco.getLocalidade());
                ps.setString(7, endereco.getUf());

                if(ps.executeUpdate() == 1) System.out.println("Endereco inserido com sucesso!");
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void editar(Endereco  antigo, Endereco novo) {
        Integer id = -1;
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = """
                SELECT id FROM enderecos WHERE
                cep = ? AND numero = ?
                """;
            try(PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, antigo.getCep());
                ps.setString(2, antigo.getNumero() == null ?
                        "N/A" : antigo.getNumero());
                try(ResultSet rs = ps.executeQuery()) {
                    if(rs.next()) id = Integer.valueOf(rs.getString("id"));
                }
            }
            if(id == -1) {
                System.out.println("Endereco não encontrado");
                return;
            }
            sql = """
            UPDATE enderecos SET cep = ?, logradouro = ?, complemento = ?, numero = ?, bairro = ?, 
                cidade = ?, uf = ? WHERE id = ?
            """;
            try(PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, novo.getCep());
                ps.setString(2, novo.getLogradouro());
                ps.setString(3, novo.getComplemento());
                ps.setString(4, novo.getNumero() == null ?
                        "N/A" : novo.getNumero());
                ps.setString(5, novo.getBairro());
                ps.setString(6, novo.getLocalidade());
                ps.setString(7, novo.getUf());
                ps.setInt(8, id);
                if(ps.executeUpdate() == 1) {
                    System.out.println("Endereco atualizado com sucesso!");
                }
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void excluir(String cep, String numero) {
        try (Connection conexao = DataBaseConnection.conectar()) {
                Integer id = -1;
            String sql = """
            SELECT id FROM enderecos WHERE
            cep = ? AND numero = ?
            """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, cep);
                ps.setString(2, numero == null ? "N/A" : numero);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) id = rs.getInt("id");
                }
            }
            if (id == -1) {
                System.out.println("Endereco não encontrado");
                return;
            }

            sql = "DELETE FROM enderecos WHERE id = ?";
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 1) System.out.println("Endereco excluído com sucesso!");
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*public Endereco consultarPorCEPNumero(String cep, String numero) {

    }

    public List<Endereco> consultarTodosDoCEP() {

    }

    public List<Endereco> consultarTodos() {

    }

    private void converterEntidadeParaSqlCommandParametros() {

    }

    private Endereco converterSqlDataReaderParaEntidades() {

    }*/
}