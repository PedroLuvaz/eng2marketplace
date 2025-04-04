package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.List;

public class ProdutoView {
    private final MarketplaceFacade facade;
    private final ConsoleInput scanner;
    private Loja lojaLogada;

    public ProdutoView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void menu(Loja lojaLogada) {
        this.lojaLogada = lojaLogada;
        Integer opcao;
        do {
            System.out.println("\n--- Gestão de Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Listar Meus Produtos");
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
        } while (opcao != 0);
    }

    private void adicionarProduto() {
        System.out.println("\n--- Adicionar Produto ---");
        
        String nome = scanner.askText("Nome: ", ".{2,100}", "Nome inválido! Deve ter entre 2 e 100 caracteres.");
        Double valor = scanner.askDouble("Valor (ex: 10.99): ", 0.01, Double.MAX_VALUE, "Valor inválido! Deve ser maior que 0.");
        String tipo = scanner.askText("Tipo: ", ".{2,50}", "Tipo inválido! Deve ter entre 2 e 50 caracteres.");
        Integer quantidade = scanner.askInteger("Quantidade: ", 0, Integer.MAX_VALUE, "Quantidade inválida! Deve ser 0 ou mais.");
        String marca = scanner.askText("Marca: ", ".{2,50}", "Marca inválida! Deve ter entre 2 e 50 caracteres.");
        String descricao = scanner.askText("Descrição: ", ".{5,250}", "Descrição inválida! Deve ter entre 5 e 250 caracteres.");

        facade.adicionarProduto(nome, valor, tipo, quantidade, marca, descricao, lojaLogada);
        System.out.println("\nProduto cadastrado com sucesso!");
    }

    private void listarProdutos() {
        List<Produto> produtos = facade.listarProdutosPorLoja(lojaLogada.getCpfCnpj());
        if (produtos.isEmpty()) {
            System.out.println("\nNenhum produto cadastrado para esta loja.");
        } else {
            System.out.println("\n--- Produtos da Loja " + lojaLogada.getNome() + " ---");
            produtos.forEach(produto -> {
                System.out.println("\nID: " + produto.getId()); // <-- Mostra o ID
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Valor: R$ " + String.format("%.2f", produto.getValor()));
                System.out.println("Tipo: " + produto.getTipo());
                System.out.println("Quantidade: " + produto.getQuantidade());
                System.out.println("Marca: " + produto.getMarca());
                System.out.println("Descrição: " + produto.getDescricao());
            });
        }
    }

    private void removerProduto() {
        System.out.println("\n--- Remover Produto ---");
        String id = scanner.askText("Informe o ID do produto a ser removido: ", ".{5,}", "ID inválido!");
    
        if (facade.removerPorId(id)) {
            System.out.println("\nProduto removido com sucesso!");
        } else {
            System.out.println("\nProduto não encontrado ou não pertence à sua loja.");
        }
    }
    
}