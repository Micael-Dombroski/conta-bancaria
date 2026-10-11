package conta.domain.model;

import java.math.BigDecimal;

public class ContaPoupanca extends Conta {
    public ContaPoupanca(Cliente cliente, String numero, BigDecimal saldo, String senha) {
        super(cliente, numero, saldo, senha);
    }
    public ContaPoupanca(Cliente cliente, BigDecimal saldo, String senha) {
        super(cliente, saldo, senha);
    }

    public ContaPoupanca(Cliente cliente, String numero, BigDecimal saldo) {
        super(cliente, numero, saldo);
    }

    @Override
    public String toString() {
        return "Numero: " + getNumero() + "\n" +
                "Correntista: " + getCliente().getNome() + "\n" +
                "Saldo: " + formatarMoeda(getSaldo()) + "\n" +
                "Tipo: Conta Poupanca" + "\n";
    }
}