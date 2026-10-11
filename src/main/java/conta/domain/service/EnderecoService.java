package conta.domain.service;

import conta.domain.model.Endereco;
import conta.domain.repository.EnderecoRepository;

import java.util.List;
import java.util.Optional;

public class EnderecoService {
    private final EnderecoRepository enderecos;
    public EnderecoService(EnderecoRepository enderecos) {
        this.enderecos = enderecos;
    }

    public boolean excluir(String cep, String numero) {
        return enderecos.excluir(cep, numero);
    }

    public List<Endereco> listarTodos() {
        return enderecos.listarTodos();
    }

    public List<Endereco> listarPorCep(String cep) {
        return enderecos.listarPorCep(cep);
    }

    public Optional<Endereco> consultar(String cep, String numero) {
        return enderecos.buscarPorCepENumero(cep, numero);
    }

    public boolean salvar(Endereco endereco) {
        return enderecos.salvar(endereco);
    }

    public boolean atualizar(Endereco antigo, Endereco novo) {
        return enderecos.atualizar(antigo, novo);
    }
}
