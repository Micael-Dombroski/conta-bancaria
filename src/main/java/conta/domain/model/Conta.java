package conta.domain.model;

public abstract class Conta {
    private static Integer proxID = 1;
    private Integer ID;
    private Cliente cliente;
    private Integer numero;
    private Double saldo;
    private String senha;

    public Conta(Cliente cliente, Integer numero, Double saldo, String senha) {
        this.ID = proxID++;
        this.cliente = cliente;
        this.numero = numero;
        this.saldo = saldo;
        this.senha = senha;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}