package conta.domain.service;

import conta.domain.model.Conta;
import conta.domain.repository.ContaRepository;
import conta.infra.security.HashSenha;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ContaService {
    private final ContaRepository contas;

    public ContaService(ContaRepository contas) {
        this.contas = contas;
    }

    public Optional<Conta> autenticar(String numero, String senha) {
        return contas.buscarHashSenha(numero)
                .filter(hash -> HashSenha.verificar(senha, hash))
                .flatMap(h -> contas.buscarPorNumero(numero));
    }

    public boolean excluir(String numero, String senha) {
        return autenticar(numero, senha).isPresent() && contas.excluir(numero);
    }

    public boolean depositar(String numero, BigDecimal valor) {
        Conta conta = contas.buscarPorNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Conta nao encontrada"));
        conta.depositar(valor);          // valida valor > 0
        if (!contas.atualizarSaldo(conta)) {
            System.out.println("Falha ao atualizar saldo");
            return false;
        } else {
            return true;
        }
    }

    public boolean sacar(String numero, String senha, BigDecimal valor) {
        Conta conta = autenticar(numero, senha)
                .orElseThrow(() -> new IllegalArgumentException("Numero ou senha invalidos"));
        conta.sacar(valor);              // valida saldo suficiente
        if (!contas.atualizarSaldo(conta)) {
            System.out.println("Falha ao atualizar saldo");
            return false;
        } else {
            return true;
        }
    }

    public List<Conta> listarTodas() {
        return contas.listarTodas();
    }

    public List<Conta> listarPorTipo(int tipo) {
        return contas.listarPorTipo(tipo);
    }

    public boolean alterarSenha(String numero, String senhaAtual, String novaSenha) {
        Conta conta = autenticar(numero, senhaAtual)
                .orElseThrow(() -> new IllegalArgumentException("Numero ou senha invalidos"));
        conta.setSenha(novaSenha);
        return contas.alterarSenha(conta);
    }

    public boolean salvar(Conta conta) {
        return contas.salvar(conta);
    }
}