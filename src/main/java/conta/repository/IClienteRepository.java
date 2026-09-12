package conta.repository;

import conta.domain.model.Cliente;

import java.util.List;

public interface IClienteRepository {
    public Cliente consultarPorCpf(String cpf);
    public List<Cliente> consultarTodos();
    public void editar(Cliente cliente);
    public void excluir(String cpf);
    public void salvar(Cliente cliente);
}