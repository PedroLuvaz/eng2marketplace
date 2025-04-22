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
            } else if (facade.isCompradorLogado()) {
                System.out.println("Comprador logado: " + facade.getCompradorLogado().getNome());
            } else {
                System.out.println("Nenhum usuário logado.");
            }

            if (lojaLogada != null) {
                System.out.println("1. Menu da Loja");
                System.out.println("2. Menu de Produto");
                System.out.println("3. Logout da Loja");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                Integer opcao = scanner.getNumber(0, 3);
                if (opcao == null) continue;

                switch (opcao) {
                    case 1 -> new LojaView(facade).menu();
                    case 2 -> new ProdutoView(facade).menu(lojaLogada);
                    case 3 -> {
                        lojaLogada = null;
                        System.out.println("Logout realizado com sucesso.");
                    }
                    case 0 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                }

            } else if (facade.isCompradorLogado()) {
                System.out.println("1. Menu do Comprador");
                System.out.println("2. Logout do Comprador");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                Integer opcao = scanner.getNumber(0, 2);
                if (opcao == null) continue;

                switch (opcao) {
                    case 1 -> new CompradorView(facade).menu();
                    case 2 -> {
                        facade.logoutComprador();
                        System.out.println("Logout realizado com sucesso.");
                    }
                    case 0 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                }

            } else {
                // Sem login
                System.out.println("1. Login da Loja");
                System.out.println("2. Login do Comprador");
                System.out.println("3. Criar Conta de Loja");
                System.out.println("4. Criar Conta de Comprador");
                System.out.println("5. Menu do Administrador");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                Integer opcao = scanner.getNumber(0, 5);
                if (opcao == null) continue;

                switch (opcao) {
                    case 1 -> lojaLogada = fazerLoginLoja(facade, scanner);
                    case 2 -> fazerLoginComprador(facade, scanner);
                    case 3 -> {
                        LojaView view = new LojaView(facade);
                        view.adicionarLoja();
                    }
                    case 4 -> {
                        CompradorView view = new CompradorView(facade);
                        view.cadastrarComprador();
                    }
                    case 5 -> {
                        facade.verificarOuCriarAdminPadrao();
                        if (fazerLoginAdministrador(facade, scanner)) {
                            new AdministradorView(facade).menu();
                        } else {
                            System.out.println("Login do administrador falhou.");
                        }
                    }
                    case 0 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                }
            }
        }
    }

    private static boolean fazerLoginAdministrador(MarketplaceFacade facade, ConsoleInput scanner) {
        System.out.println("\n--- Login do Administrador ---");
    
        String login = scanner.askText("Email: ", ".+@.+\\..+", "Email inválido.");
        String senha = scanner.askText("Senha: ", ".{4,}", "Senha deve ter pelo menos 4 caracteres.");
    
        try {
            return facade.loginAdministrador(login, senha);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
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
            }
        }
        System.out.println("Número máximo de tentativas atingido.");
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
            }
        }
        System.out.println("Número máximo de tentativas atingido.");
    }
}