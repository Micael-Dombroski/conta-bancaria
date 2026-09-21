package conta.test;

import conta.dao.EnderecoDAO;
import conta.domain.model.Endereco;

import static conta.api.ViaCepClient.buscarCEP;

public class EnderecoDAOAdicionarTeste {
    public static void main(String[] args) {
        EnderecoDAO dao = new EnderecoDAO();
        try
        {
            Endereco endereco = buscarCEP("88501103");
            System.out.println(endereco);
            dao.adicionar(endereco);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
