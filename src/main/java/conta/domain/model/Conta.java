package conta.domain.model;

public abstract class Conta {
    private static Integer proxID = 1;
    private Integer ID;
    private Cliente cliente;
    private Double saldo;

    public Conta(Cliente cliente, Double saldo) {
        this.ID = proxID++;
        this.cliente = cliente;
        this.saldo = saldo;
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
}