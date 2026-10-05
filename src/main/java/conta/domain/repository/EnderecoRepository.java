package conta.domain.repository;

import conta.domain.model.Endereco;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository {
    boolean salvar(Endereco endereco);
    Optional<Endereco> buscarPorCepENumero(String cep, String numero);
    List<Endereco> listarPorCep(String cep);
    List<Endereco> listarTodos();
    boolean atualizar(Endereco antigo, Endereco novo);
    boolean excluir(String cep, String numero);
}