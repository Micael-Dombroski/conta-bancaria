package conta.repository;

import conta.domain.model.Endereco;

import java.util.List;

public interface IEnderecoRepository {
    public Endereco consultarPorCep(String cep);
    public List<Endereco> consultarTodos();
    public void excluir(String cep);
    public void salvar(Endereco endereco);
}
