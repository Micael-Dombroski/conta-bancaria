package conta.api;

import conta.dao.EnderecoDAO;
import conta.domain.model.Endereco;
import org.junit.jupiter.api.Test;

import static conta.api.ViaCepClient.buscarCEP;
import static org.junit.jupiter.api.Assertions.*;

public class EnderecoDAOEditarTest {

    @Test
    void editarEnderecoComSucesso() throws Exception {
        EnderecoDAO dao = new EnderecoDAO();
        Endereco antigo = buscarCEP("88501103");
        Endereco novo = new Endereco(antigo);
        novo.setNumero("3A Sala 2");

        assertNotNull(antigo, "O endereço retornado pelo CEP não deveria ser nulo");

        assertDoesNotThrow(() -> dao.editar(antigo, novo));

        // Reverte para o estado original, pra não sujar o banco
        assertDoesNotThrow(() -> dao.editar(novo, antigo));
    }
}