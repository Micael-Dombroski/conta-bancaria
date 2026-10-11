package conta.domain.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente {
    private String nome;
    private String cpf;
    private Endereco endereco;
    private LocalDate nascimento;

    public Cliente(String nome, String cpf, Endereco endereco, LocalDate nascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.nascimento = nascimento;
    }
    public Cliente(Cliente cliente) {
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.endereco = cliente.getEndereco();
        this.nascimento = cliente.getNascimento();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nasicmento) {
        this.nascimento = nasicmento;
    }

    public static String formatar(String cpf) {
        String d = cpf.replaceAll("\\D", "");
        if (d.length() != 11) throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        return d.substring(0, 3) + "." + d.substring(3, 6) + "."
                + d.substring(6, 9) + "-" + d.substring(9);
    }

    @Override
    public String toString() {
        DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "CPF: " + formatar(cpf) + "\n" +
                "Nome: " + nome + "\n" +
                "Nascimento: " + nascimento.format(BR) + "\n";
    }
}
