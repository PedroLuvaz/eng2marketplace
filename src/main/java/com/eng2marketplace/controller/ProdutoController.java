package com.eng2marketplace.controller;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.ProdutoRepository;
import java.util.List;

public class ProdutoController {
    private ProdutoRepository produtoRepository;

    public ProdutoController() {
        this.produtoRepository = new ProdutoRepository();
    }

    public void adicionarProduto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, Loja loja) {
        Produto produto = new Produto(nome, valor, tipo, quantidade, marca, descricao, loja);
        produtoRepository.salvar(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.listar();
    }

    public boolean removerProduto(String nome) {
        return produtoRepository.remover(nome);
    }
}
