package conta.domain.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ContaCorrente extends Conta {
    public ContaCorrente(Cliente cliente, String numero, BigDecimal saldo, String senha) {
        super(cliente, numero, saldo, senha);
    }
    public ContaCorrente(Cliente cliente, BigDecimal saldo, String senha) {
        super(cliente, saldo, senha);
    }
    public ContaCorrente(Cliente cliente, String numero, BigDecimal saldo) {
        super(cliente, numero, saldo);
    }

    public BigDecimal getLimite() {
        return limite();
    }

    @Override
    protected BigDecimal limite() {
        return new BigDecimal("1000.00");
    }
    @Override
    public String toString() {
        return "Numero: " + getNumero() + "\n" +
                "Correntista: " + getCliente().getNome() + "\n" +
                "Saldo: " + formatarMoeda(getSaldo()) + "\n" +
                "Limite: " + formatarMoeda(limite()) + "\n" +
                "Tipo: Conta Corrente" + "\n";
    }
}