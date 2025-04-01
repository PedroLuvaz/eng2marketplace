package com.eng2marketplace.controller;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.ProdutoRepository;
import com.eng2marketplace.repository.LojaRepository;

import java.util.List;

public class ProdutoController {
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;

    public ProdutoController() {
        this.produtoRepository = new ProdutoRepository(new LojaRepository());
        this.lojaRepository = new LojaRepository();
    }

    public void adicionarProduto(String nome, double valor, String tipo, int quantidade, 
                                String marca, String descricao, Loja loja) {
        Produto produto = new Produto(nome, valor, tipo, quantidade, marca, descricao, loja);
        produtoRepository.salvar(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.listar();
    }

    public List<Produto> listarProdutosPorLoja(String cpfCnpjLoja) {
        return produtoRepository.listarPorLoja(cpfCnpjLoja);
    }

    public boolean removerProduto(String nome) {
        return produtoRepository.remover(nome);
    }
}