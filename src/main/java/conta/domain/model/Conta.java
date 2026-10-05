package conta.domain.model;

import java.util.Random;

public abstract class Conta {
    private Cliente cliente;
    private String numero;
    private Double saldo;
    private String senha;
    private Random random = new Random();

    public Conta(Cliente cliente, String numero, Double saldo, String senha) {
        this.cliente = cliente;
        this.numero = numero;
        this.saldo = saldo;
        this.senha = senha;
    }
    public Conta(Cliente cliente, Double saldo, String senha) {
        this.numero = String.format("%010d", random.nextLong(10_000_000_000L));
        this.cliente = cliente;
        this.saldo = saldo;
        this.senha = senha;
    }
    public Conta(Cliente cliente, String numero, Double saldo) {
        this.cliente = cliente;
        this.numero = numero;
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    public void sacar(Double valor) {
        this.saldo -= valor;
    }
    public void depositar(Double valor) {
        this.saldo += valor;
    }
    public void transferir(Conta contaDestino, Double valor) {
        this.sacar(valor);
        contaDestino.depositar(valor);
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

    public void setNumero(String numero) {
        this.numero = numero;
    }
}