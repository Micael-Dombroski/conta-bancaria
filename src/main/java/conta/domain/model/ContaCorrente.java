package conta.domain.model;

public class ContaCorrente extends Conta {
    public ContaCorrente(Cliente cliente, Integer numero, Double saldo, String senha) {
        super(cliente, numero, saldo, senha);
    }
}