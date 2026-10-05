package conta.api;

import conta.domain.model.Endereco;
import conta.domain.service.BuscadorCep;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("rede")
class ViaCepClientTest {
    private final BuscadorCep buscador = new ViaCepClient();

    @Test
    void cepExistenteDevolveEndereco() throws Exception {
        Endereco e = buscador.buscar("88501103").orElseThrow();

        assertEquals("88501-103", e.getCep());
        assertEquals("Lages", e.getLocalidade());
        assertEquals("SC", e.getUf());
        assertEquals("Centro", e.getBairro());
        assertFalse(e.getLogradouro().isBlank());
        assertNull(e.getNumero(), "O numero e informado pelo usuario, nao pela API");
    }

    @Test
    void cepComHifenTambemFunciona() throws Exception {
        Optional<Endereco> e = buscador.buscar("88501-103");

        assertTrue(e.isPresent());
        assertEquals("88501-103", e.get().getCep());
    }

    @Test
    void cepInexistenteDevolveVazio() throws Exception {
        assertTrue(buscador.buscar("99999999").isEmpty());
    }

    @Test
    void cepComMenosDeOitoDigitosLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> buscador.buscar("123"));
    }

    @Test
    void cepNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> buscador.buscar(null));
    }
}