package conta.api;

import conta.infra.dao.ClienteDAO;
import conta.domain.model.Cliente;
import conta.domain.model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteDAOTest {
    private static final String CPF = "275.626.790-29";
    private Endereco end;
    private Cliente cliente;
    private ClienteDAO dao;

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
        cliente = new Cliente(
                "Cuca Beludo",
                CPF,
                end,
                1984,
                12,
                31
        );
        dao = new ClienteDAO();
        dao.excluir(CPF);
        dao.adicionar(cliente);
    }

    @AfterEach
    void tearDown() {
        dao.excluir(CPF);
    }

    @Test
    void adicionarClienteComSucesso() throws Exception {
        assertTrue(() -> dao.adicionar(cliente),
                "Nao foi possivel adicionar o cliente");
    }

    @Test
    void editarClienteComSucesso() throws Exception {
        Cliente novo = new Cliente(cliente);
                novo.setNome("Marginal do Grau");

        assertTrue(() -> dao.editar(novo),
                "Nao foi possivel editar o cliente");
    }

    @Test
    void consultarClienteComSucesso() {
        Optional<Cliente> retorno = dao.consultarPorCPF("275.626.790-29");
        //System.out.println(retorno);
        assertTrue(retorno.isPresent(), "O cliente retornado pelo CPF nao deveria ser nulo");
    }

    @Test
    void excluirClienteComSucesso() throws Exception {
        assertTrue(() -> dao.excluir("275.626.790-29"),
                "Nao foi possivel excluir o cliente");
        Optional<Cliente> retorno = dao.consultarPorCPF("275.626.790-29");
        assertFalse(retorno.isPresent(), "Nao deveria ser possivel consultar o cliente excluido");
    }

    @Test
    void consultarTodosClientes() {
        List<Cliente> clientes = dao.consultarTodos();
        assertTrue(clientes.stream().anyMatch(c -> c.getCpf().equals(CPF)),
                "O cliente inserido no setUp deveria estar na lista");
    }
}
