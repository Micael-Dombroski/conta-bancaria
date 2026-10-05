package conta.api;

import conta.dao.EnderecoDAO;
import conta.domain.model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static conta.api.ViaCepClient.buscarCEP;
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
    }

    @Test
    void adicionarEnderecoComSucesso() throws Exception {

        assertNotNull(end, "O endereço retornado pelo CEP não deveria ser nulo");

        assertDoesNotThrow(() -> dao.adicionar(end),
                "Adicionar o endereço não deveria lançar exceção");
    }

    @Test
    void editarEnderecoComSucesso() throws Exception {
        Endereco novo = new Endereco(end);
        novo.setNumero("3A Sala 2");

        assertNotNull(end, "O endereço retornado pelo CEP não deveria ser nulo");

        assertDoesNotThrow(() -> dao.editar(end, novo));

        // Reverte para o estado original, pra não sujar o banco
        assertDoesNotThrow(() -> dao.editar(novo, end));
    }

    @Test
    void excluirEnderecoComSucesso() throws Exception {
        assertDoesNotThrow(() -> dao.excluir("22461-000", "666"));
    }

    @Test
    void consultarEnderecoComSucesso() {
        Endereco retorno = dao.consultarPorCEPNumero("22461-000", "666");
        System.out.println(retorno);
        assertNotNull(retorno, "O endereço retornado pelo CEP e número não deveria ser nulo");
    }
}