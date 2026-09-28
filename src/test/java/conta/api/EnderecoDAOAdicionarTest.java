package conta.api;

import conta.dao.EnderecoDAO;
import conta.domain.model.Endereco;
import org.junit.jupiter.api.Test;

import static conta.api.ViaCepClient.buscarCEP;
import static org.junit.jupiter.api.Assertions.*;

public class EnderecoDAOAdicionarTest {

    @Test
    void adicionarEnderecoComSucesso() throws Exception {
        EnderecoDAO dao = new EnderecoDAO();
        Endereco endereco = buscarCEP("88501103");

        assertNotNull(endereco, "O endereço retornado pelo CEP não deveria ser nulo");

        assertDoesNotThrow(() -> dao.adicionar(endereco),
                "Adicionar o endereço não deveria lançar exceção");
    }
}