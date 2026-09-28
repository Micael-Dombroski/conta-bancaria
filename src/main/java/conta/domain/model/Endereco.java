package conta.domain.model;

import java.util.Objects;

public class Endereco {
    private String cep;
    private String logradouro;
    private String complemento;
    private String numero;
    private String bairro;
    private String localidade;
    private String uf;

    public Endereco() {
    }

    public Endereco(String cep, String logradouro, String complemento, String numero, String bairro, String localidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.numero = numero;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }
    public Endereco(Endereco endereco) {
        this.cep = endereco.getCep();
        this.logradouro = endereco.getLogradouro();
        this.complemento = endereco.getComplemento();
        this.numero = endereco.getNumero();
        this.bairro = endereco.getBairro();
        this.localidade = endereco.getLocalidade();
        this.uf = endereco.getUf();
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "CEP: " + cep + "\n" +
                "UF: " + uf + "\n" +
                "Cidade: " + localidade + "\n" +
                "Bairro: " + bairro + "\n" +
                "Logradouro: " + logradouro + "\n" +
                "Complemento: " + complemento + "\n" +
                "Número: " + numero + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Endereco)) return false;
        Endereco end = (Endereco) obj;
        if(Objects.equals(this.cep, end.cep)
                && Objects.equals(this.logradouro, end.logradouro)
                && Objects.equals(this.complemento, end.complemento)
                && Objects.equals(this.numero, end.numero)
                && Objects.equals(this.bairro, end.bairro)
                && Objects.equals(this.localidade, end.localidade)
                && Objects.equals(this.uf, end.uf)
        ) return true;
        return false;
    }
}
