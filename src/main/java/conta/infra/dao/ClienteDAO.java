package conta.infra.dao;

import conta.infra.database.DataBaseConnection;
import conta.domain.model.Cliente;
import conta.domain.model.Endereco;
import conta.infra.security.HashCPF;
import conta.infra.security.CPFCrypto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO {

    private static String normalizar(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    public boolean adicionar(Cliente cliente) {
        String cpf = normalizar(cliente.getCpf());

        if (getID(cpf) != -1) {
            System.out.println("CPF ja cadastrado");
            return false;
        }
        if(EnderecoDAO.getID(cliente.getEndereco().getCep(), cliente.getEndereco().getNumero()) == -1) {
            System.out.println("Endereco nao encontrado");
            return false;
        }
        String sql = """
            INSERT INTO clientes
            (cpf_crypt, cpf_hash, nome, data_nascimento, endereco_id)
            VALUES (?,?,?,?,?)""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, CPFCrypto.criptografar(cpf));
            ps.setString(2, HashCPF.hash(cpf));
            ps.setString(3, cliente.getNome());
            ps.setDate(4, java.sql.Date.valueOf(cliente.getNascimento()));
            ps.setInt(5, EnderecoDAO.getID(cliente.getEndereco().getCep(),
                    cliente.getEndereco().getNumero()));

            if (ps.executeUpdate() == 1) {
                System.out.println("Cliente inserido com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente", e);
        }
        return false;
    }

    public boolean editar(Cliente cliente) {
        String cpf = normalizar(cliente.getCpf());
        int id = getID(cpf);

        if (id == -1) {
            System.out.println("CPF nao cadastrado");
            return false;
        }
        if(EnderecoDAO.getID(cliente.getEndereco().getCep(), cliente.getEndereco().getNumero()) == -1) {
            System.out.println("Endereco nao encontrado");
            return false;
        }
        String sql = """
            UPDATE clientes
            SET nome = ?, data_nascimento = ?, endereco_id = ?,
                data_modificacao = CURRENT_TIMESTAMP
            WHERE id = ?""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setDate(2, java.sql.Date.valueOf(cliente.getNascimento()));
            ps.setInt(3, EnderecoDAO.getID(cliente.getEndereco().getCep(),
                    cliente.getEndereco().getNumero()));
            ps.setInt(4, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("Cliente atualizado com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
        return false;
    }

    public boolean excluir(String cpf) {
        int id = getID(normalizar(cpf));

        if (id == -1) {
            System.out.println("Cliente nao cadastrado");
            return false;
        }
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                System.out.println("Cliente excluido com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente", e);
        }
        return false;
    }

    public Optional<Cliente> consultarPorCPF(String cpf) {
        String sql = """
            SELECT cpf_crypt, nome, data_nascimento, endereco_id
            FROM clientes WHERE cpf_hash = ?""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, HashCPF.hash(normalizar(cpf)));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearCliente(rs)) :
                        Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar cliente", e);
        }
    }

    public List<Cliente> consultarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT cpf_crypt, nome, data_nascimento, endereco_id FROM clientes";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) clientes.add(mapearCliente(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        }
        return clientes;
    }

    private static Cliente mapearCliente(ResultSet rs) throws SQLException {
        java.time.LocalDate nasc = rs.getDate("data_nascimento").toLocalDate();
        Endereco end = EnderecoDAO.buscarPorID(rs.getInt("endereco_id")).
                orElseThrow(() -> new IllegalStateException(
                "Endereco nao encontrado"));
        return new Cliente(
                rs.getString("nome"),
                CPFCrypto.descriptografar(rs.getString("cpf_crypt")),
                end,
                nasc.getYear(),
                nasc.getMonthValue(),
                nasc.getDayOfMonth()
        );
    }

    static int getID(String cpf) {
        String sql = "SELECT id FROM clientes WHERE cpf_hash = ?";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, HashCPF.hash(normalizar(cpf)));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar id do cliente", e);
        }
    }

    static Optional<Cliente> buscarPorID(Integer id) {
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = """
                    SELECT * FROM clientes WHERE
                    id = ?
                    """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearCliente(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }
}