package conta.ui;

import conta.domain.model.Cliente;
import conta.domain.model.Endereco;
import conta.domain.repository.ClienteRepositoryImpl;
import conta.domain.repository.EnderecoRepositoryImpl;
import conta.domain.service.ClienteService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class ClienteUI {
    private final ClienteService clientes;
    private Scanner scan;
    public ClienteUI(Scanner scan) {
        this.clientes = new ClienteService(new ClienteRepositoryImpl());
        this.scan = scan;
    }

    void start() {
        int opt = 0;
        do {
            System.out.println("Menu Clientes");
            System.out.println("==============");
            System.out.println("[1] Cadastrar");
            System.out.println("[2] Editar");
            System.out.println("[3] Consultar por CPF");
            System.out.println("[4] Consultar todos");
            System.out.println("[5] Excluir");
            System.out.println("[0] Sair");
            System.out.println("==============");
            System.out.print("-> ");
            opt = lerInteiro();
            switch (opt) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    atualizarCliente();
                    break;
                case 3:
                    consultarCliente();
                    break;
                case 4:
                    consultarTodos();
                    break;
                case 5:
                    excluirCliente();
                    break;
                case 0:
                    System.out.println("\tSaindo...\n");
                    break;
                default:
                    System.out.println("\tOpcao invalida\n");
            }
            if(opt != 0 && opt != 2) esperarInput();
        } while(opt != 0);
    }

    private void cadastrarCliente() {
        System.out.print("\nInforme o CPF (so numeros): ");
        String cpf = scan.nextLine();
        if(!validarTamanhoCPF(cpf)) return;
        System.out.print("\nInforme o Nome: ");
        String nome = scan.nextLine();
        LocalDate nascimento = lerDataNascimento();
        if(nascimento== null || !validarDataNascimento(nascimento)) return;
        System.out.print("\nInforme o cep do endereco: ");
        String cep = scan.nextLine();
        cep = EnderecoUI.normalizaCep(cep);
        if(cep.isBlank()) return;
        System.out.print("\nInforme o numero do endereco: ");
        String numero = scan.nextLine();
        Optional<Endereco> end = new EnderecoRepositoryImpl().buscarPorCepENumero(cep, numero);
        end.ifPresent(endereco -> {
            if(clientes.salvar(new Cliente(nome, cpf, endereco, nascimento))) {
                System.out.println("\nCliente cadastrado com sucesso");
            } else {
                System.out.println("\nNao foi possivel cadastrar o cliente");
            }
        });
    }

    private void consultarCliente() {
        System.out.print("\nInforme o CPF do cliente que deseja consultar (so numeros): ");
        String cpf = scan.nextLine();
        if(!validarTamanhoCPF(cpf)) return;
        Optional<Cliente> cliente = clientes.consultar(cpf);
        if(cliente.isEmpty()) {
            System.out.println("\nCliente nao encontrado");
        } else {
            System.out.println("\nInformacoes do cliente");
            System.out.println("----------------------");
            System.out.println(cliente.get());
        }
    }

    private void excluirCliente() {
        System.out.print("\nInforme o CPF do cliente que deseja excluir (so numeros): ");
        String cpf = scan.nextLine();
        if(!validarTamanhoCPF(cpf)) return;
        if(clientes.excluir(cpf)) {
            System.out.println("\nCliente excluido com sucesso");
        } else {
            System.out.println("\nNao foi possivel excluir o cliente");
        }
    }

    private void consultarTodos() {
        System.out.println("Lista de clientes");
        System.out.println("=================");
        clientes.listarTodos().forEach(c -> {
            System.out.println(c);
            System.out.println("-----------------");
        });
    }

    private void atualizarCliente() {
        System.out.print("\nInforme o CPF do cliente que deseja editar (so numeros): ");
        String cpf = scan.nextLine();
        Optional<Cliente> resultado = clientes.consultar(cpf);
        if(resultado.isPresent()) {
            Cliente cliente = resultado.get();
            int opt = 0;
            do {
                System.out.println("\nO que deseja editar?");
                System.out.println("[1] Nome");
                System.out.println("[2] Endereco");
                System.out.println("[3] Data de nascimento");
                System.out.println("[4] Salvar alteracoes");
                System.out.println("[0] Descartar alteracoes");
                System.out.print("-> ");
                opt = lerInteiro();
                switch (opt) {
                    case 1:
                        System.out.print("\nInforme o nome atualizado: ");
                        String nome = scan.nextLine();
                        if(!nome.trim().isEmpty()) {
                            cliente.setNome(nome.trim());
                        } else {
                            System.out.println("O nome nao pode estar vazio");
                        }
                        break;
                    case 2:
                        System.out.print("\nInforme o CEP do endereco atualizado: ");
                        String cep = scan.nextLine();
                        System.out.print("\nInforme o numero do endereco atualizado: ");
                        String numero = scan.nextLine();
                        Optional<Endereco> end = new EnderecoRepositoryImpl().buscarPorCepENumero(cep, numero);
                        if(end.isPresent()) {
                            cliente.setEndereco(end.get());
                        } else {
                            System.out.println("\nEndereco nao encontrado");
                        }
                        break;
                    case 3:
                        LocalDate nascimento = lerDataNascimento();
                        if(nascimento == null || validarDataNascimento(nascimento)) cliente.setNascimento(nascimento);
                        break;
                    case 4:
                        if(clientes.atualizar(cliente)) {
                            System.out.println("\nCliente atualizado");
                        } else {
                            System.out.println("\nFalha ao atualizar o cliente");
                        }
                        break;
                    case 0:
                        System.out.println("Alteracoes descartadas");
                        break;
                    default:
                        System.out.println("\nOpcao invalida");
                }
                if(opt != 0 && opt != 4) esperarInput();
            } while(opt != 0 && opt != 4);
            if(opt == 4) clientes.atualizar(cliente);
        } else {
            System.out.println("\nCliente nao cadastrado");
            esperarInput();
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

    private void esperarInput() {
        System.out.println("Pressione ENTER para voltar ao menu");
        scan.nextLine();
    }

    private boolean validarTamanhoCPF(String cpf) {
        if(cpf.length() == 11) return true;
        System.out.println("O CPF deve ter 11 digitos");
        return false;
    }

    private boolean validarDataNascimento(LocalDate nascimento) {
        LocalDate hoje = LocalDate.now();
        if(nascimento.isBefore(LocalDate.of(1940, 1, 1)) ||
            nascimento.isAfter(hoje.minusYears(14))) {
            System.out.println("Data de nascimento invalida");
            return false;
        }
        return true;
    }

    private LocalDate lerDataNascimento() {
        LocalDate nascimento = null;
        try {
            System.out.print("\nInforme a data de nascimento (dd/mm/yyyy): ");
            String nasc = scan.nextLine();
            String[] data = nasc.split("/");
            if(data.length != 3) {
                System.out.println("\nData invalida");
            } else {
                nascimento = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return nascimento;
    }
}
