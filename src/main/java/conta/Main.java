package conta;

import conta.infra.security.HashSenha;
import conta.ui.ContaBancariaUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContaBancariaUI ui = new ContaBancariaUI(scanner);
        ui.start();
    }
}