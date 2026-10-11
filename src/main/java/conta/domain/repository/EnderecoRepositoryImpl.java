package conta.domain.repository;

import conta.domain.model.Endereco;
import conta.infra.dao.EnderecoDAO;

import java.util.List;
import java.util.Optional;

public class EnderecoRepositoryImpl implements EnderecoRepository {
    private final EnderecoDAO dao;
    public EnderecoRepositoryImpl() {
        this.dao = new EnderecoDAO();
    }

    @Override
    public boolean salvar(Endereco endereco) {
        return dao.adicionar(endereco);
    }

    @Override
    public Optional<Endereco> buscarPorCepENumero(String cep, String numero) {
        return dao.consultarPorCEPNumero(cep, numero);
    }

    @Override
    public List<Endereco> listarPorCep(String cep) {
        return dao.consultarTodosDoCEP(cep);
    }

    @Override
    public List<Endereco> listarTodos() {
        return dao.consultarTodos();
    }

    @Override
    public boolean atualizar(Endereco antigo, Endereco novo) {
        return dao.editar(antigo, novo);
    }

    @Override
    public boolean excluir(String cep, String numero) {
        return dao.excluir(cep, numero);
    }
}
