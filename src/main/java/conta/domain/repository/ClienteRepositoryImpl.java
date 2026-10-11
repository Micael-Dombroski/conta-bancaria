package conta.domain.repository;

import conta.domain.model.Cliente;
import conta.infra.dao.ClienteDAO;

import java.util.List;
import java.util.Optional;

public class ClienteRepositoryImpl implements ClienteRepository {
    private final ClienteDAO dao;
    public ClienteRepositoryImpl() {
        this.dao = new ClienteDAO();
    }

    @Override
    public boolean salvar(Cliente cliente) {
        return dao.adicionar(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return dao.consultarPorCPF(cpf);
    }

    @Override
    public List<Cliente> listarTodos() {
        return dao.consultarTodos();
    }

    @Override
    public boolean atualizar(Cliente cliente) {
        return dao.editar(cliente);
    }

    @Override
    public boolean excluir(String cpf) {
        return dao.excluir(cpf);
    }
}
