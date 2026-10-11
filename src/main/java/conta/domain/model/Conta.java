package conta.domain.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public abstract class Conta {
    private Cliente cliente;
    private String numero;
    private BigDecimal saldo;
    private String senha;

    public Conta(Cliente cliente, String numero, BigDecimal saldo, String senha) {
        this.cliente = cliente;
        this.numero = numero;
        this.saldo = saldo;
        this.senha = senha;
    }
    public Conta(Cliente cliente, BigDecimal saldo, String senha) {
        Random random = new Random();
        this.numero = String.format("%010d", random.nextLong(10_000_000_000L));
        this.cliente = cliente;
        this.saldo = saldo;
        this.senha = senha;
    }
    public Conta(Cliente cliente, String numero, BigDecimal saldo) {
        this.cliente = cliente;
        this.numero = numero;
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor deve ser positivo");
        if (valor.compareTo(saldo.add(limite())) >= 0) throw new IllegalStateException("Saldo insuficiente");
        saldo = saldo.subtract(valor);
    }
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor deve ser positivo");
        saldo = saldo.add(valor);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNumero() {
        return numero;
    }

    protected BigDecimal limite() {
        return BigDecimal.ZERO;
    }

    public static String formatarMoeda(BigDecimal valor) {
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return moeda.format(valor);
    }
}