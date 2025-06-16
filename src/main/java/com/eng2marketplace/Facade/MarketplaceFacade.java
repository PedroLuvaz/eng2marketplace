package com.eng2marketplace.Facade;

import com.eng2marketplace.controller.AdministradorController;
import com.eng2marketplace.controller.CompradorController;
import com.eng2marketplace.controller.LojaController;
import com.eng2marketplace.controller.PedidoController;
import com.eng2marketplace.controller.ProdutoController;
import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Avaliacao;

import java.util.List;
import java.util.Map;

public class MarketplaceFacade {
    private AdministradorController administradorController;
    private LojaController lojaController;
    private ProdutoController produtoController;
    private CompradorController compradorController;
    private PedidoController pedidoController;
    private int tentativasLoginComprador = 0;
    private int tentativasLoginLoja = 0;

    public MarketplaceFacade() {
        this.lojaController = new LojaController();
        this.produtoController = new ProdutoController();
        this.compradorController = new CompradorController();
        this.pedidoController = new PedidoController();
        this.administradorController = new AdministradorController();
    }

    // ========== ADMINISTRADOR ==========

     public boolean loginAdministrador(String email, String senha) {
        return administradorController.login(email, senha);
    }

    public void logoutAdministrador() {
        administradorController.logout();
    }

    public void adicionarAdministrador(String nome, String email, String senha) {
        administradorController.adicionarAdministrador(nome, email, senha);
    }

    public boolean removerAdministrador(String email) {
        return administradorController.removerAdministrador(email);
    }

    public boolean administradorLogadoTemPermissao(String operacao) {
        return administradorController.verificarPermissao(operacao);
    }

    public boolean isAdministradorLogado() {
        return administradorController.isLoggedIn();
    }

    // Se precisar acessar diretamente o controller:
    public AdministradorController getAdministradorController() {
        return administradorController;
    }

    public List<Administrador> listarAdministradores() {
    return administradorController.listarAdministradores();
    }

    public void verificarOuCriarAdminPadrao() {
        if (administradorController.listarAdministradores().isEmpty()) {
            adicionarAdministrador("Administrador Padrão", "admin@marketplace.com", "admin123");
            System.out.println("Administrador padrão criado!");
        }
    }

    // Métodos para Loja (mantidos existentes)

    // Adiciona uma nova loja ao sistema
    public void adicionarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        lojaController.adicionarLoja(nome, email, senha, cpfCnpj, endereco);
    }

    // Lista todas as lojas cadastradas
    public List<Loja> listarLojas() {
        return lojaController.listarLojas();
    }

    // Remove uma loja com base no CPF/CNPJ
    public boolean removerLoja(String cpfCnpj) {
        return lojaController.removerLoja(cpfCnpj);
    }

    // Busca uma loja específica pelo CPF/CNPJ
    public Loja buscarLojaPorCpfCnpj(String cpfCnpj) {
        return lojaController.buscarLojaPorCpfCnpj(cpfCnpj);
    }

    // Métodos para Produto (mantidos existentes)

    // Adiciona um novo produto ao sistema
    public void adicionarProduto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, Loja loja) {
        produtoController.adicionarProduto(nome, valor, tipo, quantidade, marca, descricao, loja);
    }

    // Lista todos os produtos cadastrados
    public List<Produto> listarProdutos() {
        return produtoController.listarProdutos();
    }

    // Remove um produto com base no nome
    public boolean removerProduto(String nome) {
        return produtoController.removerProduto(nome);
    }

    // Remove um produto com base no ID
    public boolean removerPorId(String id) {
        return produtoController.removerProdutoPorId(id);
    }

    // Lista todos os produtos de uma loja específica
    public List<Produto> listarProdutosPorLoja(String cpfCnpjLoja) {
        return produtoController.listarProdutosPorLoja(cpfCnpjLoja);
    }

    // Métodos para Comprador (atualizados)

    // Cadastra um novo comprador no sistema
    public void cadastrarComprador(String nome, String email, String senha, String cpf, String endereco) {
        compradorController.adicionarComprador(nome, email, senha, cpf, endereco);
    }

    // Lista todos os compradores cadastrados
    public List<Comprador> listarCompradores() {
        return compradorController.listarCompradores();
    }

    // Remove um comprador com base no CPF
    public boolean removerComprador(String cpf) {
        return compradorController.removerComprador(cpf);
    }

    // Busca um comprador específico pelo CPF
    public Comprador buscarCompradorPorCpf(String cpf) {
        return compradorController.buscarCompradorPorCpf(cpf);
    }

    public void logoutLoja() {
        lojaController.logout();
    }

    // Realiza o logout do comprador logado
    public void logoutComprador() {
        compradorController.logout();
    }

    // Verifica se há um comprador logado no sistema
    public boolean isCompradorLogado() {
        return compradorController.isLoggedIn();
    }

    // Retorna o comprador atualmente logado
    public Comprador getCompradorLogado() {
        return compradorController.getCompradorLogado();
    }

    // Adiciona um produto ao carrinho do comprador logado
    public boolean adicionarAoCarrinho(String produtoId, int quantidade) {
        return compradorController.adicionarAoCarrinho(produtoId, quantidade);
    }

    // Remove um produto do carrinho do comprador logado
    public boolean removerDoCarrinho(String produtoId) {
        return compradorController.removerDoCarrinho(produtoId);
    }

    // Limpa todos os itens do carrinho do comprador logado
    public void limparCarrinho() {
        compradorController.limparCarrinho();
    }

    // Retorna o carrinho do comprador logado
    public Map<String, Integer> getCarrinho() {
        return compradorController.getCarrinho();
    }

    // Altera a quantidade de um produto no carrinho do comprador logado
    public boolean alterarQuantidadeCarrinho(String produtoId, int novaQuantidade) {
        return compradorController.alterarQuantidadeCarrinho(produtoId, novaQuantidade);
    }

    // Realiza o login de uma loja com CPF/CNPJ e senha
    public Loja loginLoja(String cpfCnpj, String senha) {
        if (tentativasLoginLoja >= 5) {
            throw new IllegalStateException("Número máximo de tentativas excedido. Tente novamente mais tarde.");
        }

        Loja loja = lojaController.fazerLogin(cpfCnpj, senha);
        if (loja == null) {
            tentativasLoginLoja++;
            throw new IllegalArgumentException("CPF/CNPJ ou senha incorretos. Tentativas restantes: " + (5 - tentativasLoginLoja));
        }
        tentativasLoginLoja = 0; // Reseta se login for bem-sucedido
        return loja;
    }

    public boolean loginComprador(String cpf, String senha) {
        if (tentativasLoginComprador >= 5) {
            throw new IllegalStateException("Número máximo de tentativas excedido. Tente novamente mais tarde.");
        }

        boolean success = compradorController.login(cpf, senha);
        if (!success) {
            tentativasLoginComprador++;
            throw new IllegalArgumentException("CPF ou senha incorretos. Tentativas restantes: " + (5 - tentativasLoginComprador));
        }
        tentativasLoginComprador = 0; // Reseta se login for bem-sucedido
        return true;
    }

    public Loja getLojaLogada() {
        return lojaController.getLojaLogada();
    }

        /**
     * Finaliza a compra do comprador logado, atualiza o estoque, cria o pedido e limpa o carrinho.
     * @return Pedido criado
     */
        public Pedido finalizarCompra(Map<String, Integer> carrinho, double valorTotal) {
            if (!isCompradorLogado()) {
                throw new IllegalStateException("Nenhum comprador está logado.");
            }

            Comprador comprador = getCompradorLogado();

            if (carrinho == null || carrinho.isEmpty()) {
                throw new IllegalStateException("O carrinho está vazio.");
            }

            // Verifica estoque e atualiza produtos
            for (Map.Entry<String, Integer> entry : carrinho.entrySet()) {
                String produtoId = entry.getKey();
                int quantidade = entry.getValue();

                Produto produto = listarProdutos().stream()
                    .filter(p -> p.getId().equals(produtoId))
                    .findFirst()
                    .orElse(null);

                if (produto == null) {
                    throw new IllegalStateException("Produto com ID " + produtoId + " não encontrado.");
                }

                if (produto.getQuantidade() < quantidade) {
                    throw new IllegalStateException("Estoque insuficiente para o produto: " + produto.getNome());
                }

                produto.setQuantidade(produto.getQuantidade() - quantidade);
                produtoController.atualizarProduto(produto, produto.getId());
            }

            // Aqui, usa o valorTotal passado (com desconto)
            double total = valorTotal;

            // Obtém a loja associada ao primeiro produto do carrinho
            String primeiroProdutoId = carrinho.keySet().iterator().next();
            Produto primeiroProduto = listarProdutos().stream()
                .filter(p -> p.getId().equals(primeiroProdutoId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado ao finalizar compra."));
            Loja loja = primeiroProduto.getLoja();

            Pedido pedido = pedidoController.criarPedido(comprador.getCpf(), carrinho, total, loja);

            // Dá pontos ao comprador pela compra (total com desconto)
            compradorController.finalizarCompra(total);

            compradorController.limparCarrinho();
            return pedido;
        }


    public List<Pedido> listarHistoricoCompras(String compradorCpf) {
        return pedidoController.listarPedidosPorComprador(compradorCpf);
    }

    public double calcularTotalCarrinho(Map<String, Integer> carrinho) {
        final double[] total = {0.0};
        List<Produto> produtos = produtoController.listarProdutos();

        for (Map.Entry<String, Integer> item : carrinho.entrySet()) {
            String produtoId = item.getKey();
            int quantidade = item.getValue();

            produtos.stream()
                .filter(p -> p.getId().equals(produtoId))
                .findFirst()
                .ifPresent(produto -> total[0] += produto.getValor() * quantidade);
        }

        return total[0];
    }

    public boolean podeAvaliarLoja(String compradorCpf, String lojaCpfCnpj) {
        List<Pedido> pedidos = listarHistoricoCompras(compradorCpf);
        return pedidos.stream()
            .anyMatch(p -> p.getLoja().getCpfCnpj().equals(lojaCpfCnpj) && p.getStatus().equalsIgnoreCase("FINALIZADO"));
    }

    public void avaliarLoja(String lojaCpfCnpj, String compradorCpf, int nota, String comentario) {
        lojaController.avaliarLoja(lojaCpfCnpj, compradorCpf, nota, comentario);
    }

    public List<Avaliacao> getAvaliacoesLoja(String lojaCpfCnpj) {
        return lojaController.getAvaliacoesLoja(lojaCpfCnpj);
    }

    public double getMediaAvaliacoes(String lojaCpfCnpj) {
        return lojaController.getMediaAvaliacoes(lojaCpfCnpj);
    }

    public String getConceitoLoja(String lojaCpfCnpj) {
        return lojaController.getConceitoLoja(lojaCpfCnpj);
    }


    public void avaliarProduto(String produtoId, String compradorCpf, int nota, String comentario) {
        // Validações
        if (produtoId == null || compradorCpf == null) {
            throw new IllegalArgumentException("ID do produto ou CPF inválidos");
        }

        Produto produto = produtoController.listarProdutos().stream()
            .filter(p -> produtoId.equals(p.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Cria e adiciona a avaliação (usando getAvaliacoes() que nunca retorna null)
        Avaliacao avaliacao = new Avaliacao(compradorCpf, nota, comentario);
        produto.getAvaliacoes().add(avaliacao);

        // Concede os pontos ao commprador por avaliar o produto
        compradorController.avaliarProdutoPontos(produtoId);

        // Persiste as alterações
        produtoController.atualizarProduto(produto, produtoId);
    }





}
