package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.view.input.ConsoleInput;


import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            System.out.println("\n--- Menu do Comprador ---");
            // Exibe a pontuação do comprador logado
            int pontos = facade.getCompradorLogado().getPontuacao();
            System.out.println("Pontuação atual: " + pontos);
            System.out.println("1. Logout");
            System.out.println("2. Menu do Carrinho");
            System.out.println("3. Finalizar Compra");
            System.out.println("4. Ver Histórico de Pedidos");
            System.out.println("0. Voltar");

            opcao = scanner.getNumber(0, 4);

            if (opcao == null) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            switch (opcao) {
                case 1 -> {
                    logoutComprador();
                    opcao = 0;
                }
                case 2 -> menuCarrinho();
                case 3 -> {
                    try {
                        new PedidoView(facade).finalizarCompra();
                        // Exibe a pontuação após finalizar a compra
                        int pontosAposCompra = facade.getCompradorLogado().getPontuacao();
                        System.out.println("Sua pontuação atual: " + pontosAposCompra);
                    } catch (Exception e) {
                        System.out.println("Erro ao finalizar compra: " + e.getMessage());
                    }
                }
                case 4 -> {
                    try {
                        new PedidoView(facade).menuHistorico();
                    } catch (Exception e) {
                        System.out.println("Erro ao acessar histórico: " + e.getMessage());
                    }
                }
                case 0 -> System.out.println("Voltando ao menu principal...");
            }
        } while (opcao == null || opcao != 0);
    }

    void cadastrarComprador() {
        System.out.println("\n--- Cadastro de Comprador ---");
        String nome = scanner.askName("Nome: ", 99, "Nome inválido!");
        String email = scanner.askMail("Email: ", "Email inválido!");
        String senha = scanner.askText("Senha (mínimo 8 caracteres): ", ".{8,}", "Senha inválida!");
        String cpf = scanner.askCPF("CPF: ", "CPF inválido!");
        String endereco = scanner.askText("Endereço: ", ".{5,250}", "Endereço inválido!");

        try {
            facade.cadastrarComprador(nome, email, senha, cpf, endereco);
            System.out.println("Comprador cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar comprador: " + e.getMessage());
        }
    }

    public void loginComprador() {
        System.out.println("\n--- Login do Comprador ---");
        String cpf = scanner.askCPF("CPF: ", "CPF inválido!");
        String senha = scanner.askText("Senha: ", ".{8,}", "Senha inválida!");

        if (facade.loginComprador(cpf, senha)) {
            System.out.println("Login realizado com sucesso! Bem-vindo, " +
                facade.getCompradorLogado().getNome() + "!");
        } else {
            System.out.println("CPF ou senha incorretos.");
        }
    }

    private void logoutComprador() {
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
            System.out.println("3. Alterar quantidade no carrinho");
            System.out.println("4. Listar produtos no carrinho");
            System.out.println("5. Limpar carrinho");
            System.out.println("6. Finalizar compra");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 6);

            if (opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> adicionarAoCarrinho();
                case 2 -> removerDoCarrinho();
                case 3 -> alterarQuantidadeCarrinho();
                case 4 -> listarCarrinho();
                case 5 -> limparCarrinho();
                case 6 -> {
                    try {
                        new PedidoView(facade).finalizarCompra();
                    } catch (Exception e) {
                        System.out.println("Erro ao finalizar compra: " + e.getMessage());
                    }
                }
                case 0 -> System.out.println("Voltando ao menu anterior...");
            }
        } while (opcao == null || opcao != 0);
    }

    private void adicionarAoCarrinho() {
        List<Produto> produtos = facade.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Não há produtos disponíveis no momento.");
            return;
        }

        System.out.println("\n--- Produtos Disponíveis ---");
        produtos.forEach(produto ->
            System.out.printf("%s - %s - R$%.2f - Estoque: %d%n",
                produto.getId(),
                produto.getNome(),
                produto.getValor(),
                produto.getQuantidade()));

        String produtoId = scanner.askText("Informe o ID do produto: ", ".+", "ID inválido!");

        Optional<Produto> produtoOpt = produtos.stream()
            .filter(p -> p.getId().equals(produtoId))
            .findFirst();

        if (produtoOpt.isEmpty()) {
            System.out.println("Produto não encontrado!");
            return;
        }

        Produto produto = produtoOpt.get();
        int estoqueDisponivel = produto.getQuantidade();

        if (estoqueDisponivel <= 0) {
            System.out.println("Produto sem estoque disponível!");
            return;
        }

        System.out.print("Quantidade (1-" + estoqueDisponivel + "): ");
        Integer quantidade = scanner.getNumber(1, estoqueDisponivel);

        if (quantidade == null || quantidade <= 0) {
            System.out.println("Quantidade inválida!");
            return;
        }

        if (facade.adicionarAoCarrinho(produtoId, quantidade)) {
            System.out.printf("%d unidade(s) de %s adicionada(s) ao carrinho!%n",
                quantidade, produto.getNome());
        } else {
            System.out.println("Falha ao adicionar ao carrinho.");
        }
    }

    private void removerDoCarrinho() {
        Map<String, Integer> carrinho = facade.getCarrinho();
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        listarCarrinho();
        String produtoId = scanner.askText("Informe o ID do produto a remover: ", ".+", "ID inválido!");

        if (facade.removerDoCarrinho(produtoId)) {
            System.out.println("Produto removido do carrinho!");
        } else {
            System.out.println("Produto não encontrado no carrinho.");
        }
    }

    private void alterarQuantidadeCarrinho() {
        Map<String, Integer> carrinho = facade.getCarrinho();
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        listarCarrinho();
        String produtoId = scanner.askText("Informe o ID do produto: ", ".+", "ID inválido!");

        if (!carrinho.containsKey(produtoId)) {
            System.out.println("Produto não encontrado no carrinho.");
            return;
        }

        List<Produto> produtos = facade.listarProdutos();
        Optional<Produto> produtoOpt = produtos.stream()
            .filter(p -> p.getId().equals(produtoId))
            .findFirst();

        if (produtoOpt.isEmpty()) {
            System.out.println("Produto não existe mais no catálogo!");
            return;
        }

        Produto produto = produtoOpt.get();
        int estoqueDisponivel = produto.getQuantidade();
        int quantidadeAtual = carrinho.get(produtoId);

        System.out.printf("Quantidade atual: %d | Estoque disponível: %d%n",
            quantidadeAtual, estoqueDisponivel);

        System.out.print("Nova quantidade (1-" + estoqueDisponivel + "): ");
        Integer novaQuantidade = scanner.getNumber(1, estoqueDisponivel);

        if (novaQuantidade == null || novaQuantidade <= 0) {
            System.out.println("Quantidade inválida!");
            return;
        }

        if (facade.alterarQuantidadeCarrinho(produtoId, novaQuantidade)) {
            System.out.printf("Quantidade de %s atualizada para %d!%n",
                produto.getNome(), novaQuantidade);
        } else {
            System.out.println("Falha ao atualizar quantidade.");
        }
    }

    private void listarCarrinho() {
        Map<String, Integer> carrinho = facade.getCarrinho();
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        System.out.println("\n--- Seu Carrinho ---");
        List<Produto> produtos = facade.listarProdutos();
        double total = 0.0;

        for (Map.Entry<String, Integer> item : carrinho.entrySet()) {
            String produtoId = item.getKey();
            int quantidade = item.getValue();

            Optional<Produto> produtoOpt = produtos.stream()
                .filter(p -> p.getId().equals(produtoId))
                .findFirst();

            if (produtoOpt.isPresent()) {
                Produto produto = produtoOpt.get();
                double subtotal = produto.getValor() * quantidade;
                total += subtotal;

                System.out.printf("%s - %s%n", produtoId, produto.getNome());
                System.out.printf("  %d x R$%.2f = R$%.2f%n",
                    quantidade, produto.getValor(), subtotal);
            } else {
                System.out.printf("%s - Produto não disponível%n", produtoId);
                System.out.printf("  %d unidade(s)%n", quantidade);
            }
        }

        System.out.printf("%nTotal do Carrinho: R$%.2f%n", total);
    }

    private void limparCarrinho() {
        facade.limparCarrinho();
        System.out.println("Carrinho limpo com sucesso!");
    }

}
