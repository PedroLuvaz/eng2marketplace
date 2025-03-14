package com.eng2marketplace.view;

import com.eng2marketplace.controller.LojaController;
import com.eng2marketplace.model.Loja;
import java.util.List;
import java.util.Scanner;

public class LojaView {
    private LojaController lojaController;
    private Scanner scanner;

    public LojaView() {
        this.lojaController = new LojaController();
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Lojas ---");
            System.out.println("1. Adicionar Loja");
            System.out.println("2. Listar Lojas");
            System.out.println("3. Remover Loja");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();  

            switch (opcao) {
                case 1 -> adicionarLoja();
                case 2 -> listarLojas();
                case 3 -> removerLoja();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void adicionarLoja() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CPF/CNPJ: ");
        String cpfCnpj = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        lojaController.adicionarLoja(nome, email, senha, cpfCnpj, endereco);
        System.out.println("Loja adicionada com sucesso!");
    }

    private void listarLojas() {
        List<Loja> lojas = lojaController.listarLojas();
        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada.");
        } else {
            lojas.forEach(System.out::println);
        }
    }

    private void removerLoja() {
        System.out.print("Informe o CPF/CNPJ da loja a ser removida: ");
        String cpfCnpj = scanner.nextLine();
        if (lojaController.removerLoja(cpfCnpj)) {
            System.out.println("Loja removida com sucesso!");
        } else {
            System.out.println("Loja não encontrada.");
        }
    }
}
