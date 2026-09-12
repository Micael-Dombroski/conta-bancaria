package conta.test;
import static conta.api.ViaCepClient.buscarCEP;
import conta.domain.model.Endereco;

public class ConsultaCepApiTeste {
    public static void main(String[] args) {
        try
        {
            Endereco endereco = buscarCEP("88501103");
            System.out.println(endereco);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
