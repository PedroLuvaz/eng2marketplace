package com.eng2marketplace.view;

import com.eng2marketplace.controller.CompradorController;
import com.eng2marketplace.model.Comprador;
import java.util.List;
import java.util.Scanner;

public class CompradorView {
    private CompradorController compradorController;
    private Scanner scanner;

    public CompradorView() {
        this.compradorController = new CompradorController();
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Compradores ---");
            System.out.println("1. Adicionar Comprador");
            System.out.println("2. Listar Compradores");
            System.out.println("3. Remover Comprador");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();  

            switch (opcao) {
                case 1 -> adicionarComprador();
                case 2 -> listarCompradores();
                case 3 -> removerComprador();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void adicionarComprador() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        compradorController.adicionarComprador(nome, email, senha, cpf);
        System.out.println("Comprador adicionado com sucesso!");
    }

    private void listarCompradores() {
        List<Comprador> compradores = compradorController.listarCompradores();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
        } else {
            compradores.forEach(System.out::println);
        }
    }

    private void removerComprador() {
        System.out.print("Informe o CPF do comprador a ser removido: ");
        String cpf = scanner.nextLine();
        if (compradorController.removerComprador(cpf)) {
            System.out.println("Comprador removido com sucesso!");
        } else {
            System.out.println("Comprador não encontrado.");
        }
    }
}
