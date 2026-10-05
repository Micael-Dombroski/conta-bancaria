package conta.api;

import com.google.gson.*;
import conta.domain.model.Endereco;
import conta.domain.service.BuscaCepException;
import conta.domain.service.BuscadorCep;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.Optional;

public class ViaCepClient implements BuscadorCep {
    private static final int TIMEOUT_MS = 4000;

    @Override
    public Optional<Endereco> buscar(String cep) throws BuscaCepException {
        String digitos = cep == null ? "" : cep.replaceAll("\\D", "");
        if (digitos.length() != 8) {
            throw new IllegalArgumentException("CEP deve ter 8 digitos");
        }
        String url = "https://viacep.com.br/ws/" + digitos + "/json/";
        try {
            String json = Request.Get(url)
                    .connectTimeout(TIMEOUT_MS)
                    .socketTimeout(TIMEOUT_MS)
                    .execute()
                    .returnContent()
                    .asString();

            JsonObject o = JsonParser.parseString(json).getAsJsonObject();
            if (o.has("erro")) return Optional.empty();

            return Optional.of(new Endereco(
                    digitos.substring(0, 5) + "-" + digitos.substring(5),
                    texto(o, "logradouro"),
                    texto(o, "complemento"),
                    null,                       // numero: o usuario informa
                    texto(o, "bairro"),
                    texto(o, "localidade"),
                    texto(o, "uf")));
        } catch (IOException | JsonParseException | IllegalStateException e) {
            throw new BuscaCepException("Nao foi possivel consultar o ViaCEP", e);
        }
    }

    private static String texto(JsonObject o, String campo) {
        JsonElement e = o.get(campo);
        return (e == null || e.isJsonNull()) ? "" : e.getAsString();
    }
}