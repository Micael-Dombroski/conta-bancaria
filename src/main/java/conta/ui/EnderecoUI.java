package conta.ui;

import conta.api.ViaCepClient;
import conta.domain.model.Endereco;
import conta.infra.repository.EnderecoRepositoryImpl;
import conta.domain.service.EnderecoService;

import java.util.Optional;
import java.util.Scanner;

public class EnderecoUI {
    private final EnderecoService enderecos;
    private final Scanner scan;
    public EnderecoUI(Scanner scan) {
        this.enderecos = new EnderecoService(new EnderecoRepositoryImpl());
        this.scan = scan;
    }

    void start() {
        int opt = 0;
        do {
            System.out.println("Menu Enderecos");
            System.out.println("==============");
            System.out.println("[1] Cadastrar");
            System.out.println("[2] Editar");
            System.out.println("[3] Consultar por CEP e Numero");
            System.out.println("[4] Consultar por CEP");
            System.out.println("[5] Consultar todos");
            System.out.println("[6] Excluir");
            System.out.println("[0] Sair");
            System.out.println("==============");
            System.out.print("-> ");
            opt = lerInteiro();
            System.out.println();
            switch (opt) {
                case 1:
                    menuCadastroEndereco();
                    break;
                case 2:
                    atualizarEndereco();
                    break;
                case 3:
                    consultarEndereco();
                    break;
                case 4:
                    consultarTodosDoCep();
                    break;
                case 5:
                    consultarTodos();
                    break;
                case 6:
                    excluirEndereco();
                break;
                case 0:
                    System.out.println("\tSaindo...\n");
                    break;
                default:
                    System.out.println("\tOpcao invalida\n");
            }
            if(opt != 0) esperarInput();
        } while(opt != 0);
    }
    private void consultarTodos() {
        System.out.println("Listando todos os enderecos");
        System.out.println("---------------------------");
        for (Endereco e : enderecos.listarTodos()) {
            System.out.println(e);
            System.out.println("---------------");
        }
    }

    private void consultarTodosDoCep() {
        System.out.print("Informe o CEP dos enderecos que deseja buscar:");
        String cep = scan.nextLine();
        cep = normalizaCep(cep);
        if(cep.isBlank()) return;
        System.out.println("\nListando todos os enderecos do CEP " + cep);
        System.out.println("-------------------------------------------------");
        for (Endereco e : enderecos.listarPorCep(cep)) {
            System.out.println(e);
            System.out.println("---------------");
        }
    }

    void consultarEndereco() {
        System.out.print("Informe o CEP do endereco: ");
        String cep = scan.nextLine();
        cep = normalizaCep(cep);
        if(cep.isBlank()) return;
        System.out.print("\nInforme o numero do endereco: (Informe 'N/A' caso nao tenha numero)");
        String numero = scan.nextLine();
        Optional<Endereco> end = enderecos.consultar(cep, numero);
        if(end.isPresent()) {
            System.out.println("\nInformacoes do endereco");
            System.out.println("-----------------------");
            System.out.println(end);
        } else {
            System.out.println("\nEndereco nao encontrado");
        }
    }

    private void excluirEndereco() {
        System.out.print("\nInforme o CEP do endereco que deseja excluir: ");
        String cep = scan.nextLine();
        cep = normalizaCep(cep);
        if(cep.isBlank()) return;
        System.out.print("\nInforme o numero do endereco que deseja excluir: ");
        String numero = scan.nextLine();
        if(enderecos.excluir(cep, numero)) {
            System.out.println("\nEndereco excluido com sucesso");
        } else {
            System.out.println("\nNao foi possivel excluir o endereco");
        }
    }

    private void menuCadastroEndereco() {
        System.out.println("Menu de cadastro");
        System.out.println("----------------");
        System.out.println("[1] Buscar informacoes pelo CEP");
        System.out.println("[2] Preencher todas as informacoes");
        System.out.print("-> ");
        int opt = lerInteiro();
        System.out.println();
        Endereco end = null;
        switch (opt) {
            case 1:
                end = buscaCepApi();
                break;
            case 2:
                end = cadastrarEndereco();
                break;
            default:
                System.out.println("Opcao invalida");
        }
        if (end != null) {
            System.out.println(end);
            if(enderecos.salvar(end)) {
                System.out.println("Endereco cadastrado com sucesso");
            } else {
                System.out.println("Nao foi possivel cadastrar o endereco");
            }
        } else {
            System.out.println("Endereco nulo");
        }
    }

    Endereco buscaCepApi() {
        System.out.print("\nInforme o CEP: ");
        String cep = scan.nextLine();
        cep = normalizaCep(cep);
        if(cep.isBlank()) return null;
        Optional<Endereco> resultado = Optional.empty();
        try {
            resultado = new ViaCepClient().buscar(cep);
        } catch (Exception e) {
            System.out.println("\nErro: " + e.getMessage());
        }
        return resultado.orElse(null);
    }

    Endereco cadastrarEndereco() {
        System.out.print("Informe o CEP: ");
        String cep = scan.nextLine();
        cep = normalizaCep(cep);
        if(cep.isBlank()) return null;
        System.out.print("\nInforme o logradouro: ");
        String logradouro = scan.nextLine();
        System.out.print("\nInforme o complemento: ");
        String complemento = scan.nextLine();
        System.out.print("\nInforme o numero: ");
        String numero = scan.nextLine();
        System.out.print("\nInforme o bairro: ");
        String bairro = scan.nextLine();
        System.out.print("\nInforme a cidade: ");
        String localidade = scan.nextLine();
        System.out.print("\nInforme a UF (2 letras): ");
        String uf = scan.nextLine();
        System.out.println();
        return new Endereco(cep, logradouro,
                complemento, numero, bairro,
                localidade, uf);
    }

    private void atualizarEndereco() {
        int opt = 0;
        System.out.print("Informe o CEP do endereco que deseja atualizar: ");
        String cep = scan.nextLine();
        cep = normalizaCep(cep);
        if(cep.isBlank()) return;
        System.out.print("\nInforme o numero do endereco que deseja atualizar: ");
        String numero = scan.nextLine();
        Optional<Endereco> antigo = enderecos.consultar(cep, numero);
        if(antigo.isEmpty()) {
            System.out.println("\nEndereco nao encontrado");
            return;
        }
        Endereco novo = cadastrarEndereco();
        System.out.println("Deseja salvar alteracoes?");
        System.out.println("[1] Sim");
        System.out.println("[0] Nao");
        opt = lerInteiro();
        if(opt == 1) {
            if(enderecos.atualizar(antigo.get(), novo)) {
                System.out.println("\nEndereco atualizado");
            }
        }
    }

    private int lerInteiro() {
        int valor = -1;
        try {
            valor = Integer.parseInt(scan.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return valor;
    }

    static String normalizaCep(String cep) {
        String normalizado = "";
        try {
            normalizado = Endereco.normalizarCep(cep);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return normalizado;
    }

    private void esperarInput() {
        System.out.println("Pressione ENTER para voltar ao menu");
        scan.nextLine();
    }
}
