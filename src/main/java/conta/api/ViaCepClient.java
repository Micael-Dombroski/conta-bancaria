package conta.api;
import com.google.gson.Gson;
import conta.domain.model.Endereco;
import org.apache.http.client.fluent.Request;
public class ViaCepClient {
    public static Endereco buscarCEP(String cep) throws Exception {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        //requisicao get
        String jsonResponse = Request.Get(url).
                connectTimeout(1000)//tempo limite de conexao
                .socketTimeout(1000)//tempo maximo de leitura
                .execute()//executa a requisicao
                .returnContent()//retorna o conteudo da resposta
                .asString(); //converte o json para string
        //converte json para endereco
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Endereco.class);
    }
}
