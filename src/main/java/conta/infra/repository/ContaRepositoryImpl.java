package conta.infra.repository;

import conta.domain.model.Conta;
import conta.domain.model.TipoConta;
import conta.domain.repository.ContaRepository;
import conta.infra.dao.ContaDAO;

import java.util.List;
import java.util.Optional;

public class ContaRepositoryImpl implements ContaRepository {
    private final ContaDAO dao;
    public ContaRepositoryImpl() {
        this.dao = new ContaDAO();
    }

    @Override
    public boolean salvar(Conta conta) {
        return dao.adicionar(conta);
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numero) {
        return dao.consultarPorNumero(numero);
    }

    @Override
    public Optional<String> buscarHashSenha(String numero) {
        return dao.consultarHashPorNumero(numero);
    }

    @Override
    public List<Conta> listarTodas() {
        return dao.consultarTodos();
    }

    @Override
    public List<Conta> listarPorTipo(int tipo) {
        return dao.consultarPorTipo(tipo);
    }

    @Override
    public boolean atualizarSaldo(Conta conta) {
        return dao.atualizarSaldo(conta);
    }

    @Override
    public boolean alterarSenha(Conta conta) {
        return dao.editarSenha(conta);
    }

    @Override
    public boolean excluir(String numero) {
        return dao.excluirPorNumero(numero);
    }
}
