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

    public MarketplaceFacade() {
        this.lojaController = new LojaController();
        this.produtoController = new ProdutoController();
        this.compradorController = new CompradorController();
    }

    // Métodos para Loja (mantidos existentes)
    public void adicionarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        lojaController.adicionarLoja(nome, email, senha, cpfCnpj, endereco);
    }

    public List<Loja> listarLojas() {
        return lojaController.listarLojas();
    }

    public boolean removerLoja(String cpfCnpj) {
        return lojaController.removerLoja(cpfCnpj);
    }

    public Loja buscarLojaPorCpfCnpj(String cpfCnpj) {
        return lojaController.buscarLojaPorCpfCnpj(cpfCnpj);
    }

    // Métodos para Produto (mantidos existentes)
    public void adicionarProduto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, Loja loja) {
        produtoController.adicionarProduto(nome, valor, tipo, quantidade, marca, descricao, loja);
    }

    public List<Produto> listarProdutos() {
        return produtoController.listarProdutos();
    }

    public boolean removerProduto(String nome) {
        return produtoController.removerProduto(nome);
    }

    public boolean removerPorId(String id) {
        return produtoController.removerProdutoPorId(id);
    }

    public List<Produto> listarProdutosPorLoja(String cpfCnpjLoja) {
        return produtoController.listarProdutosPorLoja(cpfCnpjLoja);
    }

    // Métodos para Comprador (atualizados)
    public void cadastrarComprador(String nome, String email, String senha, String cpf, String endereco) {
        compradorController.adicionarComprador(nome, email, senha, cpf, endereco);
    }

    public List<Comprador> listarCompradores() {
        return compradorController.listarCompradores();
    }

    public boolean removerComprador(String cpf) {
        return compradorController.removerComprador(cpf);
    }

    public Comprador buscarCompradorPorCpf(String cpf) {
        return compradorController.buscarCompradorPorCpf(cpf);
    }

    // Novos métodos para login e carrinho
    public boolean loginComprador(String cpf, String senha) {
        return compradorController.login(cpf, senha);
    }

    public void logoutComprador() {
        compradorController.logout();
    }

    public boolean isCompradorLogado() {
        return compradorController.isLoggedIn();
    }

    public Comprador getCompradorLogado() {
        return compradorController.getCompradorLogado();
    }

    public boolean adicionarAoCarrinho(String produtoId, int quantidade) {
        return compradorController.adicionarAoCarrinho(produtoId, quantidade);
    }

    public boolean removerDoCarrinho(String produtoId) {
        return compradorController.removerDoCarrinho(produtoId);
    }

    public void limparCarrinho() {
        compradorController.limparCarrinho();
    }

    public Map<String, Integer> getCarrinho() {
        return compradorController.getCarrinho();
    }

    public boolean alterarQuantidadeCarrinho(String produtoId, int novaQuantidade) {
        return compradorController.alterarQuantidadeCarrinho(produtoId, novaQuantidade);
    }
}