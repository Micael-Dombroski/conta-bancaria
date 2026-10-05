package conta.domain.service;

public class BuscaCepException extends Exception {
    public BuscaCepException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}