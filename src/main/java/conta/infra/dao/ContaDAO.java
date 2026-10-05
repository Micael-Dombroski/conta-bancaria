package conta.dao;

import conta.database.DataBaseConnection;
import conta.domain.model.Cliente;
import conta.domain.model.Conta;
import conta.domain.model.ContaCorrente;
import conta.domain.model.ContaPoupanca;
import conta.security.HashSenha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContaDAO {
    public boolean adicionar(Conta conta) {

        if (getID(conta.getNumero()) != -1) {
            System.out.println("Conta ja cadastrada");
            return false;
        }
        int clienteID = ClienteDAO.getID(conta.getCliente().getCpf());
        if(clienteID == -1) {
            System.out.println("Cliente nao cadastrado");
            return false;
        }
        String sql = """
            INSERT INTO contas
            (numero, cliente_id, saldo, senha_hash, tipo)
            VALUES (?,?,?,?,?)""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, conta.getNumero());
            ps.setInt(2, clienteID);
            ps.setDouble(3, conta.getSaldo());
            ps.setString(4, HashSenha.hash(conta.getSenha()));
            ps.setInt(5, conta instanceof ContaCorrente ? 1 :
                    conta instanceof ContaPoupanca ? 2 : 0);

            if (ps.executeUpdate() == 1) {
                System.out.println("Conta inserida com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir conta", e);
        }
        return false;
    }

    public boolean atualizarSaldo(Conta conta) {
        int id = getID(conta.getNumero());

        if (id == -1) {
            System.out.println("Conta nao cadastrada");
            return false;
        }
        String sql = """
            UPDATE contas
            SET saldo = ?,
                data_modificacao = CURRENT_TIMESTAMP
            WHERE id = ?""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDouble(1, conta.getSaldo());
            ps.setInt(2, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("Conta atualizada com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar conta", e);
        }
        return false;
    }

    public boolean editarSenha(Conta conta) {
        int id = getID(conta.getNumero());

        if (id == -1) {
            System.out.println("Conta nao cadastrada");
            return false;
        }
        String sql = """
            UPDATE contas
            SET senha_hash = ?,
                data_modificacao = CURRENT_TIMESTAMP
            WHERE id = ?""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, HashSenha.hash(conta.getSenha()));
            ps.setInt(2, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("Conta atualizada com sucesso!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar conta", e);
        }
        return false;
    }

    public boolean excluir(String numero, String senha) {
        String sql = "SELECT id, senha_hash FROM contas WHERE numero = ?";
        int id;
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Conta nao cadastrada");
                    return false;
                }
                if (!HashSenha.verificar(senha, rs.getString("senha_hash"))) {
                    System.out.println("Senha incorreta");
                    return false;
                }
                id = rs.getInt("id");
            }
            try (PreparedStatement del = conexao.prepareStatement("DELETE FROM contas WHERE id = ?")) {
                del.setInt(1, id);
                boolean ok = del.executeUpdate() == 1;
                if (ok) System.out.println("Conta excluida com sucesso!");
                return ok;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir conta", e);
        }
    }

    public Optional<Conta> consultarPorNumeroSenha(String numero, String senha) {
        String sql = """
            SELECT numero, saldo, senha_hash, cliente_id, tipo
            FROM contas WHERE numero = ?""";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && HashSenha.verificar(senha, rs.getString("senha_hash"))) {
                    return Optional.of(mapearConta(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar conta", e);
        }
    }
    public boolean excluirPorCpf(String cpf) {
        int clienteId = ClienteDAO.getID(cpf);
        if (clienteId == -1) return false;

        String sql = "DELETE FROM contas WHERE cliente_id = ?";
        try (Connection c = DataBaseConnection.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar contas", e);
        }
    }


    public List<Conta> consultarPorTipo(Integer tipo) {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT numero, saldo, cliente_id, tipo FROM contas where tipo = ?";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
             ps.setInt(1, tipo);
             try (ResultSet rs = ps.executeQuery()) {
                 while (rs.next()) contas.add(mapearConta(rs));
             }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contas", e);
        }
        return contas;
    }

    public List<Conta> consultarTodos() {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT numero, saldo, cliente_id, tipo FROM contas";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) contas.add(mapearConta(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contas", e);
        }
        return contas;
    }

    private static Conta mapearConta(ResultSet rs) throws SQLException {
        int tipo = rs.getInt("tipo");
        String numero = rs.getString("numero");
        double saldo = rs.getDouble("saldo");
        Cliente cliente = ClienteDAO.buscarPorID(rs.getInt("cliente_id"))
                .orElseThrow(() -> new IllegalStateException(
                        "Cliente inexistente para a conta " + numero));
        switch (tipo) {
            case 1:
                return new ContaCorrente(
                        cliente,
                        numero,
                        saldo
                );
            case 2:
                return new ContaPoupanca(
                        cliente,
                        numero,
                        saldo
                );
            default:
                throw new IllegalStateException("Tipo de conta invalido: " + tipo);
        }
    }

    static int getID(String numero) {
        String sql = "SELECT id FROM contas WHERE numero = ?";
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar id da conta", e);
        }
    }
}