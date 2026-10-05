package conta.domain.service;

import java.util.Optional;

import conta.domain.model.Endereco;

public interface BuscadorCep {
    /** Optional vazio = CEP nao existe. BuscaCepException = servico indisponivel. */
    Optional<Endereco> buscar(String cep) throws BuscaCepException;
}