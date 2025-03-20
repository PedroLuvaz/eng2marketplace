package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;

import java.util.List;
import java.util.Scanner;

public class ProdutoView {
    private MarketplaceFacade facade;
    private Scanner scanner;

    public ProdutoView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Remover Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> adicionarProduto();
                case 2 -> listarProdutos();
                case 3 -> removerProduto();
                case 0 -> System.out.println("Voltando ao menu principal...");
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

        // Assumindo que a loja já foi cadastrada anteriormente
        System.out.print("Informe o nome da loja: ");
        String nomeLoja = scanner.nextLine();

        List<Loja> lojas = facade.listarLojas();
        Loja lojaEncontrada = lojas.stream().filter(l -> l.getNome().equalsIgnoreCase(nomeLoja)).findFirst().orElse(null);

        if (lojaEncontrada != null) {
            facade.adicionarProduto(nome, valor, tipo, quantidade, marca, descricao, lojaEncontrada);
            System.out.println("Produto cadastrado com sucesso!");
        } else {
            System.out.println("Loja não encontrada. Produto não cadastrado.");
        }
    }

    private void listarProdutos() {
        List<Produto> produtos = facade.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            produtos.forEach(System.out::println);
        }
    }

    private void removerProduto() {
        System.out.print("Informe o nome do produto a ser removido: ");
        String nome = scanner.nextLine();
        if (facade.removerProduto(nome)) {
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}