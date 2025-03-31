package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.view.input.ConsoleInput;
import java.util.List;
import java.util.Scanner;

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
            System.out.println("1. Menu da Loja");
            System.out.println("2. Menu do Comprador");
            System.out.println("3. Menu de Produto");
            System.out.println("4. Login da Loja");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            Integer opcao = scanner.getNumber(0, 4);

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
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
            }
        }
    }

    private static Loja fazerLoginLoja(MarketplaceFacade facade, ConsoleInput scanner) {
        String cpfCnpj = scanner.askCPFCNPJ("CPF/CNPJ: ", "Documento inválido!");
        String senha = scanner.askText("Senha: ", ".{8,}", "Senha inválida!");

        List<Loja> lojas = facade.listarLojas();
        Loja loja = lojas.stream()
                .filter(l -> l.getCpfCnpj().equals(cpfCnpj) && l.getSenha().equals(senha))
                .findFirst()
                .orElse(null);

        if (loja != null) {
            System.out.println("Login realizado com sucesso!");
            return loja;
        } else {
            System.out.println("CPF/CNPJ ou senha incorretos.");
            return null;
        }
    }
}