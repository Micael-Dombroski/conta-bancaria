package conta.domain.repository;

import conta.domain.model.Conta;
import conta.domain.model.TipoConta;

import java.util.List;
import java.util.Optional;

public interface ContaRepository {
    boolean salvar(Conta conta);
    Optional<Conta> buscarPorNumero(String numero);
    Optional<String> buscarHashSenha(String numero);
    List<Conta> listarTodas();
    List<Conta> listarPorTipo(TipoConta tipo);
    boolean atualizarSaldo(Conta conta);
    boolean alterarSenha(Conta conta);
    boolean excluir(String numero);
}