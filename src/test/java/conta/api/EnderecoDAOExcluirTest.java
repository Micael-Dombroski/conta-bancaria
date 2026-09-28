package conta.api;

import conta.dao.EnderecoDAO;
import conta.domain.model.Endereco;
import org.junit.jupiter.api.Test;

import static conta.api.ViaCepClient.buscarCEP;
import static org.junit.jupiter.api.Assertions.*;

public class EnderecoDAOExcluirTest {

    @Test
    void excluirEnderecoComSucesso() throws Exception {
        EnderecoDAO dao = new EnderecoDAO();
        assertDoesNotThrow(() -> dao.excluir("88010-000", "500"));
    }
}