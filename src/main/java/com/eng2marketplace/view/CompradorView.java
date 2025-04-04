package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.List;

public class CompradorView {
    private final MarketplaceFacade facade;
    private final ConsoleInput scanner;

    public CompradorView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void menu() {
        Integer opcao;
        do {
            System.out.println("\n--- Gestão de Compradores ---");
            
            if (facade.isCompradorLogado()) {
                // Menu quando o comprador está logado
                System.out.println("1. Logout");
                System.out.println("2. Menu do Carrinho");
                System.out.println("0. Voltar");
                
                opcao = scanner.getNumber(0, 2);
                
                if (opcao == null) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                switch (opcao) {
                    case 1 -> logoutComprador();
                    case 2 -> menuCarrinho();
                    case 0 -> System.out.println("Voltando ao menu principal...");
                }
            } else {
                // Menu quando o comprador não está logado
                System.out.println("1. Cadastrar Comprador");
                System.out.println("2. Login do Comprador");
                System.out.println("0. Voltar");
                
                opcao = scanner.getNumber(0, 2);
                
                if (opcao == null) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                switch (opcao) {
                    case 1 -> cadastrarComprador();
                    case 2 -> loginComprador();
                    case 0 -> System.out.println("Voltando ao menu principal...");
                }
            }
        } while (opcao != 0);
    }

    private void cadastrarComprador() {
        String nome = scanner.askName("Nome (entre 2 e 99 letras): ", 99, "Nome inválido!");
        String email = scanner.askMail("Email (padrão aaa@bbb.ccc): ", "Email inválido!");
        String senha = scanner.askText("Senha (pelo menos 8 caracteres): ", ".{8,}", "Senha inválida!");
        String cpf = scanner.askCPF("CPF (somente números ou com ponto e hífen): ", "Número de documento inválido!");
        String endereco = scanner.askText("Endereço (entre 5 e 250 caracteres): ", ".{5,250}", "Endereço inválido!");

        facade.cadastrarComprador(nome, email, senha, cpf, endereco);
        System.out.println("Comprador cadastrado com sucesso!");
    }

    public void loginComprador() {
        if (facade.isCompradorLogado()) {
            System.out.println("Já existe um comprador logado.");
            return;
        }

        String cpf = scanner.askCPF("CPF: ", "Número de documento inválido!");
        String senha = scanner.askText("Senha: ", ".{8,}", "Senha inválida!");

        if (facade.loginComprador(cpf, senha)) {
            System.out.println("Login realizado com sucesso! Bem-vindo, " + 
                facade.getCompradorLogado().getNome() + "!");
        } else {
            System.out.println("CPF ou senha incorretos.");
        }
    }

    private void logoutComprador() {
        if (!facade.isCompradorLogado()) {
            System.out.println("Nenhum comprador está logado.");
            return;
        }

        String nome = facade.getCompradorLogado().getNome();
        facade.logoutComprador();
        System.out.println("Logout realizado com sucesso. Até logo, " + nome + "!");
    }

    private void menuCarrinho() {
        Integer opcao;
        do {
            System.out.println("\n--- Carrinho de Compras ---");
            System.out.println("1. Adicionar produto ao carrinho");
            System.out.println("2. Remover produto do carrinho");
            System.out.println("3. Listar produtos no carrinho");
            System.out.println("4. Limpar carrinho");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 4);

            if (opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> adicionarAoCarrinho();
                case 2 -> removerDoCarrinho();
                case 3 -> listarCarrinho();
                case 4 -> limparCarrinho();
                case 0 -> System.out.println("Voltando ao menu anterior...");
            }
        } while (opcao != 0);
    }

    private void adicionarAoCarrinho() {
        List<Produto> produtos = facade.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Não há produtos disponíveis no momento.");
            return;
        }

        System.out.println("\n--- Produtos Disponíveis ---");
        produtos.forEach(produto -> 
            System.out.println(produto.getId() + " - " + produto.getNome() + 
                " - R$" + produto.getValor()));

        String produtoId = scanner.askText("Informe o ID do produto a ser adicionado: ", ".+", "ID inválido!");
        
        if (facade.adicionarAoCarrinho(produtoId)) {
            System.out.println("Produto adicionado ao carrinho com sucesso!");
        } else {
            System.out.println("Não foi possível adicionar o produto ao carrinho.");
        }
    }

    private void removerDoCarrinho() {
        List<String> carrinho = facade.getCarrinho();
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        System.out.println("\n--- Produtos no Carrinho ---");
        carrinho.forEach(System.out::println);

        String produtoId = scanner.askText("Informe o ID do produto a ser removido: ", ".+", "ID inválido!");
        
        if (facade.removerDoCarrinho(produtoId)) {
            System.out.println("Produto removido do carrinho com sucesso!");
        } else {
            System.out.println("Produto não encontrado no carrinho.");
        }
    }

    private void listarCarrinho() {
        List<String> carrinho = facade.getCarrinho();
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
        } else {
            System.out.println("\n--- Produtos no Carrinho ---");
            List<Produto> todosProdutos = facade.listarProdutos();
            
            carrinho.forEach(produtoId -> {
                todosProdutos.stream()
                    .filter(p -> p.getId().equals(produtoId))
                    .findFirst()
                    .ifPresentOrElse(
                        produto -> System.out.println(produtoId + " - " + produto.getNome() + 
                                                  " - R$" + produto.getValor()),
                        () -> System.out.println(produtoId + " - Produto não encontrado")
                    );
            });
        }
    }

    private void limparCarrinho() {
        facade.limparCarrinho();
        System.out.println("Carrinho limpo com sucesso!");
    }
}