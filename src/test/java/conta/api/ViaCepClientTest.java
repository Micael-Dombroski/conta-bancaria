package conta.api;

import conta.domain.model.Endereco;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static conta.api.ViaCepClient.buscarCEP;
import static org.junit.jupiter.api.Assertions.*;

class ViaCepClientTest {
    @Test
    void buscarCepTest() {
        Endereco endereco = null;
        try
        {
            endereco = buscarCEP("88501103");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Endereco resultadoEsperado = new Endereco("88501-103",
                "Avenida Marechal Floriano",
                "de 341 a 499 - lado ímpar",
                null,
                "Centro",
                "Lages",
                "SC");
        Assertions.assertEquals(resultadoEsperado, endereco);
    }
}