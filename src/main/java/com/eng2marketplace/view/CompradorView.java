package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Comprador;

import java.util.List;
import java.util.Scanner;

public class CompradorView {
    private MarketplaceFacade facade;
    private Scanner scanner;

    public CompradorView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Compradores ---");
            System.out.println("1. Cadastrar Comprador");
            System.out.println("2. Listar Compradores");
            System.out.println("3. Remover Comprador");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarComprador();
                case 2 -> listarCompradores();
                case 3 -> removerComprador();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarComprador() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        facade.cadastrarComprador(nome, email, senha, cpf, endereco);
        System.out.println("Comprador cadastrado com sucesso!");
    }

    private void listarCompradores() {
        List<Comprador> compradores = facade.listarCompradores();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
        } else {
            compradores.forEach(System.out::println);
        }
    }

    private void removerComprador() {
        System.out.print("Informe o email do comprador a ser removido: ");
        String email = scanner.nextLine();
        if (facade.removerComprador(email)) {
            System.out.println("Comprador removido com sucesso!");
        } else {
            System.out.println("Comprador não encontrado.");
        }
    }
}
