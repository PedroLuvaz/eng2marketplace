package com.eng2marketplace.view;

import com.eng2marketplace.controller.ProdutoController;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import java.util.List;
import java.util.Scanner;

public class ProdutoView {
    private ProdutoController produtoController;
    private Scanner scanner;

    public ProdutoView() {
        this.produtoController = new ProdutoController();
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Remover Produto");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();  

            switch (opcao) {
                case 1 -> adicionarProduto();
                case 2 -> listarProdutos();
                case 3 -> removerProduto();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void adicionarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();  
        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();
        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();  
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        // Simulação de escolha de loja (para simplificar)
        System.out.print("Nome da Loja: ");
        String nomeLoja = scanner.nextLine();
        Loja loja = new Loja(nomeLoja, "", "", "", ""); 

        produtoController.adicionarProduto(nome, valor, tipo, quantidade, marca, descricao, loja);
        System.out.println("Produto adicionado com sucesso!");
    }

    private void listarProdutos() {
        List<Produto> produtos = produtoController.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            produtos.forEach(System.out::println);
        }
    }

    private void removerProduto() {
        System.out.print("Informe o nome do produto a ser removido: ");
        String nome = scanner.nextLine();
        if (produtoController.removerProduto(nome)) {
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}
