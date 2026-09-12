package conta.repository;

import conta.domain.model.Conta;

import java.util.List;

public interface IContaRepository {
    public Conta consultarPorId(Integer id);
    public List<Conta> consultarPorCliente(String cpf);
    public List<Conta> consultarTodas();
    public void editar(Conta conta);
    public void excluir(Integer id);
    public void salvar(Conta conta);
}
