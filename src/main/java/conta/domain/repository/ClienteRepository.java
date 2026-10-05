package conta.domain.repository;

import conta.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    boolean salvar(Cliente cliente);
    Optional<Cliente> buscarPorCpf(String cpf);
    List<Cliente> listarTodos();
    boolean atualizar(Cliente cliente);
    boolean excluir(String cpf);
}