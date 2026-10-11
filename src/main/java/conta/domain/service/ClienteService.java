package conta.domain.service;

import conta.domain.model.Cliente;
import conta.domain.model.Endereco;
import conta.domain.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteRepository clientes;
    public ClienteService(ClienteRepository clientes) {
        this.clientes = clientes;
    }

    public boolean excluir(String cpf) {
        return clientes.excluir(cpf);
    }

    public List<Cliente> listarTodos() {
        return clientes.listarTodos();
    }

    public Optional<Cliente> consultar(String cpf) {
        return clientes.buscarPorCpf(cpf);
    }

    public boolean salvar(Cliente cliente) {
        return clientes.salvar(cliente);
    }

    public boolean atualizar(Cliente cliente) {
        return clientes.atualizar(cliente);
    }
}
