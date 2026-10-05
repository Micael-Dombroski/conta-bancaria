package conta.dao;

import conta.database.DataBaseConnection;
import conta.domain.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderecoDAO {
    public EnderecoDAO(){
    }

    public boolean adicionar(Endereco endereco)  {
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = "SELECT 1 FROM enderecos WHERE cep = ? AND numero = ?";
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, endereco.getCep());
                ps.setString(2, endereco.getNumero() == null ? "N/A"
                        : endereco.getNumero());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Endereco ja existe");
                        return false;
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
                ps.setString(7, endereco.getUf().toUpperCase());

                if(ps.executeUpdate() == 1) {
                    System.out.println("Endereco inserido com sucesso!");
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public boolean editar(Endereco antigo, Endereco novo) {
        int idAntigo = getID(antigo.getCep(), antigo.getNumero());
        if (idAntigo == -1) {
            System.out.println("Endereco nao encontrado");
            return false;
        }
        int idNovo = getID(novo.getCep(), novo.getNumero());
        if (idNovo != -1 && idNovo != idAntigo) {
            System.out.println("Endereco ja cadastrado");
            return false;
        }
        String sql = """
        UPDATE enderecos SET cep = ?, logradouro = ?, complemento = ?, numero = ?,
            bairro = ?, cidade = ?, uf = ?, data_modificacao = CURRENT_TIMESTAMP
        WHERE id = ?""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, novo.getCep());
            ps.setString(2, novo.getLogradouro());
            ps.setString(3, novo.getComplemento());
            ps.setString(4, novo.getNumero() == null ? "N/A" : novo.getNumero());
            ps.setString(5, novo.getBairro());
            ps.setString(6, novo.getLocalidade());
            ps.setString(7, novo.getUf().toUpperCase());
            ps.setInt(8, idAntigo);
            if (ps.executeUpdate() == 1) {
                System.out.println("Endereco atualizado com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar endereco", e);
        }
        return false;
    }

    public boolean excluir(String cep, String numero) {
        Integer id = getID(cep, numero);
        if (id == -1) {
            System.out.println("Endereco nao encontrado");
            return false;
        }
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = "DELETE FROM enderecos WHERE id = ?";
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 1) {
                    System.out.println("Endereco excluido com sucesso!");
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public Optional<Endereco> consultarPorCEPNumero(String cep, String numero) {
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = """
            SELECT * FROM enderecos WHERE
            cep = ? AND numero = ?
            """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, cep);
                ps.setString(2, numero == null ? "N/A" : numero);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()) {
                        return Optional.of(mapearEndereco(rs));
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Endereco> consultarTodosDoCEP(String cep) {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM enderecos WHERE cep = ?";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, cep);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) enderecos.add(mapearEndereco(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar enderecos", e);
        }
        return enderecos;
    }

    public List<Endereco> consultarTodos() {
        List<Endereco> enderecos = null;
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = """
            SELECT * FROM enderecos
            """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                enderecos = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) enderecos.add(mapearEndereco(rs));
                }
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return enderecos;
    }

    private static Endereco mapearEndereco(ResultSet rs) throws SQLException {
        return new Endereco(
                rs.getString("cep"),
                rs.getString("logradouro"),
                rs.getString("complemento"),
                rs.getString("numero"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("uf")
        );
    }

    static Integer getID(String cep, String numero) {
        Integer id = -1;
        try (Connection conexao = DataBaseConnection.conectar()) {
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
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return id;
    }

    static Optional<Endereco> buscarPorID(Integer id) {
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = """
            SELECT * FROM enderecos WHERE
            id = ?
            """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()) return Optional.of(mapearEndereco(rs));
                }
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }
}