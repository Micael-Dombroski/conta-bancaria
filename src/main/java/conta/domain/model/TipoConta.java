package conta.domain.model;

public enum TipoConta {
    CORRENTE(1), POUPANCA(2);

    private final int codigo;
    TipoConta(int codigo) { this.codigo = codigo; }
    public int getCodigo() { return codigo; }

    public static TipoConta fromCodigo(int codigo) {
        for (TipoConta tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de conta inválido: " + codigo);
    }
}