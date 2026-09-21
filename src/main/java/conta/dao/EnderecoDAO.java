package conta.dao;

import conta.database.DataBaseConnection;
import conta.domain.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnderecoDAO {
    public EnderecoDAO(){
    }

    public void adicionar(Endereco endereco)  {
        try (Connection conexao = DataBaseConnection.conectar()) {
            String sql = """
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

    /*public void editar(Endereco  endereco) {

    }

    public void excluir(String cep, String numero) {

    }

    public Endereco consultarPorCEPNumero(String cep, String numero) {

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