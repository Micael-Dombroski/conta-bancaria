package conta.domain.model;

public class ContaPoupanca extends Conta {
    public ContaPoupanca(Cliente cliente, Integer numero, Double saldo, String senha) {
        super(cliente, numero, saldo, senha);
    }
}