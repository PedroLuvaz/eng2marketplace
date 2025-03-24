package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.view.input.ConsoleInput;

public class Main {
    public static void main(String[] args) {
        ConsoleInput scanner = new ConsoleInput();
        MarketplaceFacade facade = new MarketplaceFacade();

        while (true) {
            System.out.println("\n=== Marketplace ===");
            System.out.println("1. Acessar Menu da Loja");
            System.out.println("2. Acessar Menu do Comprador");
            System.out.println("3. Acessar Menu de Produto");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            Integer opcao = scanner.getNumber(0, 3);
            if (opcao == null) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            switch (opcao) {
                case 1 -> new LojaView(facade).menu();
                case 2 -> new CompradorView(facade).menu();
                case 3 -> new ProdutoView(facade).menu();
                case 0 -> {
                    System.out.println("Saindo...");
                    // scanner.close();
                    return;
                }
            }
        }
    }
}
