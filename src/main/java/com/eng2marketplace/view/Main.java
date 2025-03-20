package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MarketplaceFacade facade = new MarketplaceFacade();

        while (true) {
            System.out.println("\n=== Marketplace ===");
            System.out.println("1. Acessar Menu da Loja");
            System.out.println("2. Acessar Menu do Comprador");
            System.out.println("3. Acessar Menu de Produto");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> new LojaView(facade).menu();
                case 2 -> new CompradorView(facade).menu();
                case 3 -> new ProdutoView(facade).menu();
                case 0 -> {
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}