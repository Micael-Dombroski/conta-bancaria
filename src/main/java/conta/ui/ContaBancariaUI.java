package conta.ui;

import conta.domain.repository.ClienteRepositoryImpl;
import conta.domain.repository.ContaRepositoryImpl;
import conta.domain.repository.EnderecoRepositoryImpl;
import conta.domain.service.ClienteService;
import conta.domain.service.ContaService;
import conta.domain.service.EnderecoService;

import java.util.Scanner;

public class ContaBancariaUI {
    private ContaUI contaUI;
    private ClienteUI clienteUI;
    private EnderecoUI enderecoUI;
    private Scanner scan;
    public ContaBancariaUI(Scanner scan) {
        this.scan = scan;
        contaUI = new ContaUI(scan);
        clienteUI = new ClienteUI(scan);
        enderecoUI = new EnderecoUI(scan);
    }

    public void start() {
        int opt = 0;
        do {
            System.out.println("Sistema bancario");
            System.out.println("=======================");
            System.out.println("[1] Menu de Enderecos");
            System.out.println("[2] Menu de Clientes");
            System.out.println("[3] Menu de Contas");
            System.out.println("[0] Sair");
            System.out.print("-> ");
            opt = capturaInteiro();
            switch (opt) {
                case 1:
                    enderecoUI.start();
                    break;
                case 2:
                    clienteUI.start();
                    break;
                case 3:
                    contaUI.start();
                    break;
                case 0:
                    System.out.println("\tSaindo...\n");
                    break;
                default:
                    System.out.println("\tOpcao invalida\n");
            }
        } while(opt != 0);
    }

    private int capturaInteiro() {
        int valor = -1;
        try {
            valor = Integer.parseInt(scan.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return valor;
    }
}
