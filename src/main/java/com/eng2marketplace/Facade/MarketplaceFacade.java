package com.eng2marketplace.Facade;

import com.eng2marketplace.controller.CompradorController;
import com.eng2marketplace.controller.LojaController;
import com.eng2marketplace.controller.ProdutoController;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;

import java.util.List;
import java.util.Map;

public class MarketplaceFacade {
    private LojaController lojaController;
    private ProdutoController produtoController;
    private CompradorController compradorController;
    private int tentativasLoginComprador = 0;
    private int tentativasLoginLoja = 0;

    public MarketplaceFacade() {
        this.lojaController = new LojaController();
        this.produtoController = new ProdutoController();
        this.compradorController = new CompradorController();
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
}