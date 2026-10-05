package conta.api;

import conta.infra.dao.EnderecoDAO;
import conta.domain.model.Endereco;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EnderecoDAOTest {

    private Endereco end;
    private EnderecoDAO dao;

    @BeforeEach
    void setUp() {
        end = new Endereco(
                "22461-000",
                "Rua Jardim Botânico",
                "até 518 - lado par",
                "666",
                "Jardim Botânico",
                "Rio de Janeiro",
                "RJ"
        );
        dao = new EnderecoDAO();
        dao.adicionar(end);
    }

    @AfterEach
    void tearDown() throws Exception {
        limpar();
    }

    private void limpar() throws Exception {
        dao.excluir("22461-000", "777");
        dao.excluir("22461-000", "888");
        dao.excluir("22461-000", "666");
        dao.excluir("22461-000", "38 bloco D");
    }

    @Test
    void adicionarEnderecoComSucesso() throws Exception {
        assertTrue(dao.adicionar(new Endereco(
                "22461-000",
                "Rua Jardim Botânico",
                "até 518 - lado par",
                "888",
                "Jardim Botânico",
                "Rio de Janeiro",
                "RJ"
        )), "Nao foi possivel adicionar o endereco");
        dao.excluir("22461-000","888");
    }

    @Test
    void editarEnderecoComSucesso() throws Exception {
        Endereco novo = new Endereco(end);
        novo.setNumero("38 bloco D");

        assertTrue(dao.editar(end, novo), "Nao foi possivel editar o endereco");
    }

    @Test
    void excluirEnderecoComSucesso() throws Exception {
        dao.adicionar(new Endereco(
                        "22461-000",
                        "Rua Jardim Botânico",
                        "até 518 - lado par",
                        "777",
                        "Jardim Botânico",
                        "Rio de Janeiro",
                        "RJ"));
        assertTrue(dao.excluir("22461-000", "777"),
                "Nao foi possivel excluir o endereco");
    }

    @Test
    void consultarEnderecoComSucesso() {
        Optional<Endereco> retorno = dao.consultarPorCEPNumero("22461-000", "666");
        //System.out.println(retorno);
        assertTrue(retorno.isPresent(), "O endereco retornado pelo CEP e numero nao deveria ser nulo");
    }

    @Test
    void consultarTodosEnderecosDoCep() {
        List<Endereco> enderecos = dao.consultarTodosDoCEP("22461-000");
        enderecos.forEach(e -> System.out.println(e));
        assertTrue(enderecos.stream().anyMatch(c -> c.getNumero().equals("666")),
                "O endereco inserido no setUp deveria estar na lista");
    }

    @Test
    void consultarTodosEnderecos() {
        List<Endereco> enderecos = dao.consultarTodos();
        enderecos.forEach(e -> System.out.println(e));
        assertTrue(enderecos.stream().anyMatch(c -> c.getCep().equals("22461-000")
                        && c.getNumero().equals("666")),
                "O endereco inserido no setUp deveria estar na lista");
    }
}