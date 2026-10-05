package conta.domain.model;

public class ContaPoupanca extends Conta {
    public ContaPoupanca(Cliente cliente, String numero, Double saldo, String senha) {
        super(cliente, numero, saldo, senha);
    }
    public ContaPoupanca(Cliente cliente, Double saldo, String senha) {
        super(cliente, saldo, senha);
    }

    public ContaPoupanca(Cliente cliente, String numero, Double saldo) {
        super(cliente, numero, saldo);
    }

    @Override
    public String toString() {
        return "Numero: " + getNumero() + "\n" +
                "Correntista: " + getCliente().getNome() + "\n" +
                "Saldo: R$" + getSaldo() + "\n" +
                "Tipo: Conta Poupanca" + "\n";
    }
}