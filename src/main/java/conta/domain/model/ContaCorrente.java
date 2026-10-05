package conta.domain.model;

public class ContaCorrente extends Conta {
    public ContaCorrente(Cliente cliente, String numero, Double saldo, String senha) {
        super(cliente, numero, saldo, senha);
    }
    public ContaCorrente(Cliente cliente, Double saldo, String senha) {
        super(cliente, saldo, senha);
    }
    public ContaCorrente(Cliente cliente, String numero, Double saldo) {
        super(cliente, numero, saldo);
    }
    @Override
    public String toString() {
        return "Numero: " + getNumero() + "\n" +
                "Correntista: " + getCliente().getNome() + "\n" +
                "Saldo: R$" + getSaldo() + "\n" +
                "Tipo: Conta Corrente" + "\n";
    }
}