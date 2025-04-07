package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.view.input.ConsoleInput;

public class Main {
    
    public static void main(String[] args) {
        ConsoleInput scanner = new ConsoleInput();
        MarketplaceFacade facade = new MarketplaceFacade();
        Loja lojaLogada = null;

        while (true) {
            System.out.println("\n=== Marketplace ===");
            if (lojaLogada != null) {
                System.out.println("Loja logada: " + lojaLogada.getNome());
            }
            if (facade.isCompradorLogado()) {
                System.out.println("Comprador logado: " + facade.getCompradorLogado().getNome());
            }
            System.out.println("1. Menu da Loja");
            System.out.println("2. Menu do Comprador");
            System.out.println("3. Menu de Produto");
            System.out.println("4. Login da Loja");
            System.out.println("5. Login do Comprador");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            Integer opcao = scanner.getNumber(0, 5);

            if (opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> new LojaView(facade).menu();
                case 2 -> new CompradorView(facade).menu();
                case 3 -> {
                    if (lojaLogada != null) {
                        new ProdutoView(facade).menu(lojaLogada);
                    } else {
                        System.out.println("Por favor, faça login como loja primeiro.");
                    }
                }
                case 4 -> lojaLogada = fazerLoginLoja(facade, scanner);
                case 5 -> {
                    if (!facade.isCompradorLogado()) {
                        fazerLoginComprador(facade, scanner);
                    } else {
                        System.out.println("Já existe um comprador logado.");
                    }
                }
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
            }
        }
    }

    private static Loja fazerLoginLoja(MarketplaceFacade facade, ConsoleInput scanner) {
        int tentativas = 0;
        while (tentativas < 5) {
            try {
                System.out.println("\n--- Login da Loja ---");
                String cpfCnpj = scanner.askCPFCNPJ("CPF/CNPJ: ", "Documento inválido!");
                String senha = scanner.askText("Senha: ", ".{8,}", "Senha inválida!");
                
                Loja loja = facade.loginLoja(cpfCnpj, senha);
                System.out.println("Login realizado com sucesso!");
                return loja;
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println(e.getMessage());
                tentativas++;
                if (tentativas >= 5) {
                    System.out.println("Número máximo de tentativas atingido. Retornando ao menu.");
                    return null;
                }
            }
        }
        return null;
    }
    
    private static void fazerLoginComprador(MarketplaceFacade facade, ConsoleInput scanner) {
        int tentativas = 0;
        while (tentativas < 5) {
            try {
                System.out.println("\n--- Login do Comprador ---");
                String cpf = scanner.askCPF("CPF: ", "CPF inválido!");
                String senha = scanner.askText("Senha: ", ".{8,}", "Senha inválida!");
                
                if (facade.loginComprador(cpf, senha)) {
                    System.out.println("Login realizado com sucesso! Bem-vindo, " + 
                        facade.getCompradorLogado().getNome() + "!");
                    return;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println(e.getMessage());
                tentativas++;
                if (tentativas >= 5) {
                    System.out.println("Número máximo de tentativas atingido. Retornando ao menu.");
                    return;
                }
            }
        }
    }
}