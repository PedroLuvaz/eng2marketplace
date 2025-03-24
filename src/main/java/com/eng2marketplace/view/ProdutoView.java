package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.List;

public class ProdutoView {
    private MarketplaceFacade facade;
    private ConsoleInput scanner;

    public ProdutoView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void menu() {
        Integer opcao;
        do {
            System.out.println("\n--- Gestão de Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Remover Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 3);
            if(opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> adicionarProduto();
                case 2 -> listarProdutos();
                case 3 -> removerProduto();
                case 0 -> System.out.println("Voltando ao menu principal...");
            }
        } while (opcao == null);
    }

    private void adicionarProduto() {
        String nome = scanner.askText("Nome (entre 2 e 99 caracteres): ", ".{2,99}", "Nome inválido!");
        double valor = scanner.askValue("Valor: ", "Valor inválido!");
        String tipo = scanner.askText("Tipo (entre 2 e 99 caracteres): ", ".{2,99}", "Tipo inválido!");
        int quantidade = scanner.askNumber("Quantidade (entre 0 e 1.000.000): ", 0, 1_000_000, "Quantidade inválida!");
        String marca = scanner.askText("Marca (entre 2 e 99 caracteres): ", ".{2,99}", "Marca inválido!");
        String descricao = scanner.askText("Descrição (opcional, no máximo 500 caracteres): ", ".{,500}", "Descrição muito longa!");

        // Assumindo que a loja já foi cadastrada anteriormente
        String nomeLoja = scanner.askText("Informe o nome da loja: ", ".{2,99}", "Nome inválido!");

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
        String nome = scanner.askText("Informe o nome do produto a ser removido (entre 2 e 99 caracteres): ", ".{2,99}", "Nome inválido!");

        if (facade.removerProduto(nome)) {
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}
