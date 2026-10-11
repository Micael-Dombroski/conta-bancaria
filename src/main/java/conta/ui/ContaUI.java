package conta.ui;

import conta.domain.model.*;
import conta.domain.repository.ClienteRepositoryImpl;
import conta.domain.repository.ContaRepositoryImpl;
import conta.domain.service.ContaService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class ContaUI {
    private final ContaService contas;
    private Scanner scan;
    public ContaUI(Scanner scan) {
        this.contas = new ContaService(new ContaRepositoryImpl());
        this.scan = scan;
    }

    void start() {
        int opt = 0;
        do {
            System.out.println("Menu Contas");
            System.out.println("==============");
            System.out.println("[1] Cadastrar");
            System.out.println("[2] Acessar");
            System.out.println("[3] Consultar por tipo");
            System.out.println("[4] Consultar todos");
            System.out.println("[5] Excluir");
            System.out.println("[0] Sair");
            System.out.println("==============");
            System.out.print("-> ");
            opt = lerInteiro();
            switch (opt) {
                case 1:
                    cadastrarConta();
                    break;
                case 2:
                    acessarConta();
                    break;
                case 3:
                    consultarContasPorTipo();
                    break;
                case 4:
                    consultarTodasContas();
                    break;
                case 5:
                    excluirConta();
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

    private void cadastrarConta() {
        System.out.println("Informe o CPF do correntista da conta: ");
        String cpf = scan.nextLine();
        Optional<Cliente> cliente = new ClienteRepositoryImpl().buscarPorCpf(cpf);
        if(cliente.isPresent()) {
            int opt = 0;
            System.out.println("Escolha o tipo de conta: ");
            System.out.println("[1] Conta Corrente");
            System.out.println("[2] Conta Poupanca");
            System.out.print("->");
            while(true) {
                opt = lerInteiro();
                if(opt == 1 || opt == 2) break;
                System.out.println("Opcao invalida");
                System.out.print("-> ");
            }
            System.out.print("Cadastre a senha da conta: ");
            String senha = scan.nextLine();
            while(senha.trim().isEmpty()) {
                System.out.println("A senha nao pode ser vazia");
                System.out.print("Cadastre a senha da conta: ");
                senha = scan.nextLine();
            }
            Conta conta = null;
            if(opt == 1) {
                conta = new ContaCorrente(cliente.get(), BigDecimal.ZERO, senha);
            } else {
                conta = new ContaPoupanca(cliente.get(), BigDecimal.ZERO, senha);
            }
            if (contas.salvar(conta)) {
                System.out.println("Conta criada");
                System.out.println("Informacoes da conta: ");
                System.out.println(conta);
            } else {
                System.out.println("Nao foi possivel cadastrar a conta");
            }
        } else {
            System.out.println("Cliente nao cadastrado");
        }
    }

    private void acessarConta() {
        System.out.print("Numero da conta: ");
        String numero = scan.nextLine();
        System.out.print("Senha da conta: ");
        String senha = scan.nextLine();
        Optional<Conta> conta = contas.autenticar(numero, senha);
        if(conta.isPresent()) {
            menuConta(conta.get());
        } else {
            System.out.println("Numero/Senha invalidos");
            esperarInput();
        }
    }

    private void consultarContasPorTipo() {
        int opt = 0;
        System.out.println("Escolha o tipo de conta que deseja consultar: ");
        System.out.println("[1] Conta Corrente");
        System.out.println("[2] Conta Poupanca");
        System.out.print("-> ");
        opt = lerInteiro();
        switch (opt) {
            case 1:
                System.out.println("Lista de contas corrente");
                System.out.println("========================");
                contas.listarPorTipo(TipoConta.CORRENTE.getCodigo()).forEach(c-> {
                    System.out.println(c);
                    System.out.println("------------------------");
                });
                break;
            case 2:
                System.out.println("Lista de contas poupanca");
                System.out.println("========================");
                contas.listarPorTipo(TipoConta.POUPANCA.getCodigo()).forEach(c-> {
                    System.out.println(c);
                    System.out.println("------------------------");
                });
                break;
            default:
                System.out.println("Opcao invalida");
        }
    }

    private void consultarTodasContas() {
        System.out.println("Lista de contas");
        System.out.println("===================");
        contas.listarTodas().forEach(c -> {
            System.out.println(c);
            System.out.println("------------------------");
        });
    }

    private void excluirConta() {
        System.out.print("Informe o numero da conta que deseja excluir: ");
        String numero = scan.nextLine();
        System.out.print("Informe a senha da conta que deseja excluir: ");
        String senha = scan.nextLine();
        if(contas.excluir(numero, senha)) {
            System.out.println("Conta excluida com sucesso");
        } else {
            System.out.println("Nao foi possivel excluir a conta");
        }
    }

    private void menuConta(Conta conta) {
        int opt = 0;
        do {
            System.out.println("Menu da Conta");
            System.out.println("==================");
            System.out.println("[1] Consultar Saldo");
            System.out.println("[2] Depositar");
            System.out.println("[3] Sacar");
            System.out.println("[4] Alterar senha");
            System.out.println("[0] Sair");
            System.out.print("-> ");
            opt = lerInteiro();
            System.out.println();
            switch (opt) {
                case 1:
                    System.out.println("Saldo: " + Conta.formatarMoeda(conta.getSaldo()));
                    if(conta instanceof ContaCorrente cCorrente) {
                        System.out.println("Limite: " + Conta.formatarMoeda(cCorrente.getLimite()));
                    }
                    break;
                case 2:
                    System.out.println("Informe o valor que deseja depositar: ");
                    System.out.print("-> R$ ");
                    BigDecimal deposito = lerValor();
                    if(deposito == null) break;
                    try {
                        if(contas.depositar(conta.getNumero(), deposito)) {
                            System.out.println("Deposito efetuado com sucesso");
                            conta.depositar(deposito);
                        }
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Informe o valor que deseja sacar: ");
                    System.out.print("-> R$ ");
                    BigDecimal saque = lerValor();
                    System.out.println();
                    if(saque == null) break;
                    System.out.println("Confirme o saque com a senha: ");
                    String senha = scan.nextLine();
                    if(contas.autenticar(conta.getNumero(), senha).isPresent()) {
                        try {
                            if(contas.sacar(conta.getNumero(), senha, saque)) {
                                System.out.println("Saque efetuado com sucesso");
                                conta.sacar(saque);
                            }
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Senha invalida");
                    }
                    break;
                case 4:
                    System.out.print("Informe a senha antiga: ");
                    String antiga = scan.nextLine();
                    if(contas.autenticar(conta.getNumero(), antiga).isPresent()) {
                        System.out.print("\nInforme a senha nova: ");
                        String nova = scan.nextLine();
                        System.out.print("\nConfirme a senha nova: ");
                        String confirmarSenha = scan.nextLine();
                        if(nova.equals(confirmarSenha)) {
                            if(contas.alterarSenha(conta.getNumero(), antiga, nova)) {
                                System.out.println("Senha alterada com sucesso");
                            } else {
                                System.out.println("Nao foi possivel alterar a senha");
                            }
                        } else {
                            System.out.println("As senhas nao sao iguais");
                        }
                    } else {
                        System.out.println("Senha invalida");
                    }
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

    private int lerInteiro() {
        int valor = -1;
        try {
            valor = Integer.parseInt(scan.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return valor;
    }

    private BigDecimal lerValor() {
        try {
            return new BigDecimal(scan.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido");
            return null;
        }
    }

    private void esperarInput() {
        System.out.println("Pressione ENTER para voltar ao menu");
        scan.nextLine();
    }
}
