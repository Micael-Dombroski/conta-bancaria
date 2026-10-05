package conta.api;

import conta.infra.dao.ClienteDAO;
import conta.infra.dao.ContaDAO;
import conta.domain.model.*;
import conta.infra.dao.EnderecoDAO;
import conta.infra.security.HashSenha;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ContaDAOTest {
    private Endereco end = new Endereco(
            "22461-000",
            "Rua Jardim Botânico",
            "até 518 - lado par",
            "666",
            "Jardim Botânico",
            "Rio de Janeiro",
            "RJ"
    );
    private static final String CPF = "275.626.790-29";
    private Cliente cliente = new Cliente(
            "Cuca Beludo",
            CPF,
            end,
            1984,
            12,
            31
    );
    private Conta contaPoup = new ContaPoupanca(
            cliente,
            0.0,
            "Teste123"
    );
    private Conta contaCorr = new ContaCorrente(
            cliente,
            0.0,
            "Teste321"
    );
    private ContaDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ContaDAO();
        ClienteDAO clienteDao = new ClienteDAO();
        EnderecoDAO enderecoDao = new EnderecoDAO();

        dao.excluirPorCpf(CPF);
        clienteDao.excluir(CPF);

        if (enderecoDao.consultarPorCEPNumero("22461-000", "666").isEmpty()) {
            assertTrue(enderecoDao.adicionar(end), "Falha ao criar o endereco no setUp");
        }
        assertTrue(clienteDao.adicionar(cliente), "Falha ao criar o cliente no setUp");
        assertTrue(dao.adicionar(contaPoup), "Falha ao criar a conta poupanca no setUp");
        assertTrue(dao.adicionar(contaCorr), "Falha ao criar a conta corrente no setUp");
    }

    @AfterEach
    void tearDown() {
        dao.excluirPorCpf(CPF);
        new ClienteDAO().excluir(CPF);
    }

    @Test
    void adicionarContaComSucesso() {
        Conta lida = dao.consultarPorNumero(contaPoup.getNumero()).orElseThrow();

        assertInstanceOf(ContaPoupanca.class, lida);
        assertEquals(contaPoup.getNumero(), lida.getNumero());
        assertEquals(0.0, lida.getSaldo(), 0.001);
    }

    @Test
    void editarSenhaComSucesso() {
        contaPoup.setSenha("NovaSenha123");
        assertTrue(dao.editarSenha(contaPoup));

        String hash = dao.consultarHashPorNumero(contaPoup.getNumero()).orElseThrow();
        assertTrue(HashSenha.verificar("NovaSenha123", hash));
        assertFalse(HashSenha.verificar("Teste123", hash));
    }

    @Test
    void editarSaldoContaComSucesso() {
        contaCorr.depositar(1000.00);
        assertTrue(dao.atualizarSaldo(contaCorr), "Nao foi possivel editar o saldo da Conta");
        Conta lida = dao.consultarPorNumero(contaCorr.getNumero()).orElseThrow();
        assertEquals(1000.00, lida.getSaldo(), 0.001);
    }

    @Test
    void consultarContaCorrenteComSucesso() {
        Optional<Conta> retorno = dao.consultarPorNumero(contaCorr.getNumero());
        //System.out.println(retorno);
        assertTrue(retorno.isPresent(), "Nao foi possivel consulta a Conta");
    }

    @Test
    void consultarContaPoupancaComSucesso() {
        Optional<Conta> retorno = dao.consultarPorNumero(contaPoup.getNumero());
        //System.out.println(retorno);
        assertTrue(retorno.isPresent(), "Nao foi possivel consulta a Conta");
    }

    @Test
    void excluirContaPoupancaComSucesso() {
        assertTrue(dao.excluirPorNumero(contaPoup.getNumero()));
        Optional<Conta> conta = dao.consultarPorNumero(contaPoup.getNumero());
        assertFalse(conta.isPresent(), "Nao deveria ser possivel consulta a conta excluida");
    }

    @Test
    void excluirContaCorrenteComSucesso() {
        assertTrue(dao.excluirPorNumero(contaCorr.getNumero()));
        Optional<Conta> conta = dao.consultarPorNumero(contaCorr.getNumero());
        assertFalse(conta.isPresent(), "Nao deveria ser possivel consulta a conta excluida");
    }

    @Test
    void excluirContaInexistenteRetornaFalse() {
        assertFalse(dao.excluirPorNumero("0000000000"));
    }

    @Test
    void consultarContasCorrente() {
        List<Conta> contas = dao.consultarPorTipo(1);

        assertTrue(contas.stream().anyMatch(c -> c.getNumero().equals(contaCorr.getNumero())),
                "A conta corrente inserida deveria estar na lista");
        assertTrue(contas.stream().allMatch(c -> c instanceof ContaCorrente),
                "Só contas correntes deveriam ser retornadas");
    }

    @Test
    void consultarContasPoupanca() {
        List<Conta> contas = dao.consultarPorTipo(2);

        assertTrue(contas.stream().anyMatch(c -> c.getNumero().equals(contaPoup.getNumero())),
                "A conta poupança inserida deveria estar na lista");
        assertTrue(contas.stream().allMatch(c -> c instanceof ContaPoupanca),
                "Só contas poupança deveriam ser retornadas");
    }

    @Test
    void consultarTodasContas() {
        List<Conta> contas = dao.consultarTodos();

        assertTrue(contas.stream().anyMatch(c -> c.getNumero().equals(contaCorr.getNumero())),
                "A conta corrente inserida deveria estar na lista");
        assertTrue(contas.stream().anyMatch(c -> c.getNumero().equals(contaPoup.getNumero())),
                "A conta poupança inserida deveria estar na lista");
    }

    @Test
    void adicionarContaDuplicadaRetornaFalse() {
        assertFalse(dao.adicionar(contaPoup));
    }

    @Test
    void consultarContaInexistenteDevolveVazio() {
        assertTrue(dao.consultarPorNumero("0000000000").isEmpty());
    }
}