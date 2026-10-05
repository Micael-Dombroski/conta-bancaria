package conta.domain.service;

import conta.domain.model.Conta;
import conta.domain.repository.ContaRepository;
import conta.infra.security.HashSenha;

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

    public void depositar(String numero, double valor) {
        Conta conta = contas.buscarPorNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Conta nao encontrada"));
        conta.depositar(valor);          // valida valor > 0
        contas.atualizarSaldo(conta);
    }

    public void sacar(String numero, String senha, double valor) {
        Conta conta = autenticar(numero, senha)
                .orElseThrow(() -> new IllegalArgumentException("Numero ou senha invalidos"));
        conta.sacar(valor);              // valida saldo suficiente
        contas.atualizarSaldo(conta);
    }
}