package com.eng2marketplace.controller;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.ProdutoRepository;
import com.eng2marketplace.repository.LojaRepository;

import java.util.List;

/**
 * Controller responsável por gerenciar as operações relacionadas aos produtos.
 */
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;

    /**
     * Construtor que inicializa os repositórios necessários.
     */
    public ProdutoController() {
        this.lojaRepository = new LojaRepository();
        this.produtoRepository = new ProdutoRepository(lojaRepository);
    }

    /**
     * Adiciona um novo produto ao repositório.
     *
     * @param nome       Nome do produto.
     * @param valor      Valor do produto.
     * @param tipo       Tipo do produto.
     * @param quantidade Quantidade disponível do produto.
     * @param marca      Marca do produto.
     * @param descricao  Descrição do produto.
     * @param loja       Loja associada ao produto.
     */
    public void adicionarProduto(String nome, double valor, String tipo, int quantidade,
                                 String marca, String descricao, Loja loja) {
        Produto produto = new Produto(nome, valor, tipo, quantidade, marca, descricao, loja);
        produtoRepository.salvar(produto);
    }

    /**
     * Lista todos os produtos disponíveis no repositório.
     *
     * @return Lista de produtos.
     */
    public List<Produto> listarProdutos() {
        return produtoRepository.listar();
    }

    /**
     * Lista os produtos associados a uma loja específica.
     *
     * @param cpfCnpjLoja CPF ou CNPJ da loja.
     * @return Lista de produtos da loja.
     */
    public List<Produto> listarProdutosPorLoja(String cpfCnpjLoja) {
        return produtoRepository.listarPorLoja(cpfCnpjLoja);
    }

    /**
     * Remove um produto pelo nome.
     *
     * @param nome Nome do produto a ser removido.
     * @return true se o produto foi removido com sucesso, false caso contrário.
     */
    public boolean removerProduto(String nome) {
        return produtoRepository.remover(nome);
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id ID do produto.
     * @return Produto encontrado ou null se não existir.
     */
    public Produto buscarProdutoPorId(String id) {
        return produtoRepository.buscarPorId(id).orElse(null);
    }

    /**
     * Remove um produto pelo ID.
     *
     * @param id ID do produto a ser removido.
     * @return true se o produto foi removido com sucesso, false caso contrário.
     */
    public boolean removerProdutoPorId(String id) {
        return produtoRepository.removerPorId(id);
    }

    public boolean atualizarProduto(Produto produto, String id) {
        return produtoRepository.atualizar(produto, p -> p.getId().equals(id));
    }
}
