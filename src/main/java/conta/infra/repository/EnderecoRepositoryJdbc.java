package conta.infra.repository;

import conta.domain.model.Endereco;
import conta.domain.repository.EnderecoRepository;
import conta.infra.dao.EnderecoDAO;

import java.util.List;
import java.util.Optional;

public class EnderecoRepositoryJdbc implements EnderecoRepository {
    private final EnderecoDAO dao;

    public EnderecoRepositoryJdbc(EnderecoDAO dao) { this.dao = dao; }

    @Override public boolean salvar(Endereco e) { return dao.adicionar(e); }
    @Override public Optional<Endereco> buscarPorCepENumero(String cep, String numero) { return dao.consultarPorCEPNumero(cep, numero); }
    @Override public List<Endereco> listarPorCep(String cep) { return dao.consultarTodosDoCEP(cep); }
    @Override public List<Endereco> listarTodos() { return dao.consultarTodos(); }
    @Override public boolean atualizar(Endereco antigo, Endereco novo) { return dao.editar(antigo, novo); }
    @Override public boolean excluir(String cep, String numero) { return dao.excluir(cep, numero); }
}