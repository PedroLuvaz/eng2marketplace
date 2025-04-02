package com.eng2marketplace.Facade;

import com.eng2marketplace.controller.CompradorController;
import com.eng2marketplace.controller.LojaController;
import com.eng2marketplace.controller.ProdutoController;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;

import java.util.List;

public class MarketplaceFacade {
    private LojaController lojaController;
    private ProdutoController produtoController;
    private CompradorController compradorController;

    public MarketplaceFacade() {
        this.lojaController = new LojaController();
        this.produtoController = new ProdutoController();
        this.compradorController = new CompradorController();
    }

    // Métodos para Loja
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
        // Implementação da busca por CPF/CNPJ
        return lojaController.buscarLojaPorCpfCnpj(cpfCnpj);
    }

    // Métodos para Produto
    public void adicionarProduto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, Loja loja) {
        produtoController.adicionarProduto(nome, valor, tipo, quantidade, marca, descricao, loja);
    }

    public List<Produto> listarProdutos() {
        return produtoController.listarProdutos();
    }

    public boolean removerProduto(String nome) {
        return produtoController.removerProduto(nome);
    }

    public boolean removerPorId(String id){
        return produtoController.removerProdutoPorId(id);
    }
    

    // Métodos para Comprador
    public void cadastrarComprador(String nome, String email, String senha, String cpf, String endereco) {
        compradorController.adicionarComprador(nome, email, senha, cpf, endereco);
    }

    public List<Comprador> listarCompradores() {
        return compradorController.listarCompradores();
    }

    public boolean removerComprador(String email) {
        return compradorController.removerComprador(email);
    }

    public Comprador buscarCompradorPorCpf(String cpf) {
        return compradorController.buscarCompradorPorCpf(cpf);
    }

    // Adicione este método na classe MarketplaceFacade
public List<Produto> listarProdutosPorLoja(String cpfCnpjLoja) {
    return produtoController.listarProdutosPorLoja(cpfCnpjLoja);
}
}
