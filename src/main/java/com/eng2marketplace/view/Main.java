package com.eng2marketplace.view;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Menu Principal
        while (true) {
            System.out.println("Selecione uma opção:");
            System.out.println("1. Acessar Menu da Loja");
            System.out.println("2. Acessar Menu do Comprador");
            System.out.println("3. Acessar Menu de Produto");
            System.out.println("0. Sair");
            
            int opcao = scanner.nextInt();
            
            switch (opcao) {
                case 1:
                    LojaView lojaView = new LojaView();
                    lojaView.menu();
                    System.out.println();
                    break;
                case 2:
                    CompradorView compradorView = new CompradorView();
                    compradorView.menu();
                    System.out.println();
                    break;
                case 3:
                    ProdutoView produtoView = new ProdutoView();
                    produtoView.menu();
                    System.out.println();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    scanner.close();
                    System.out.println();
                    return; // Sai do programa
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}


