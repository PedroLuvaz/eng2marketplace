package com.eng2marketplace.repository;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de produtos
 */
public class ProdutoRepository {
    /**
     * Classe de transição entre a base de dados e o modelo real
     */
    public static class ProdutoTransicao {
        public String nome;
        public double valor;
        public String tipo;
        public int quantidade;
        public String marca;
        public String descricao;
        public String lojaDoc;  // Documento da loja

        /**
         * Converte essa instância para um produto
         * @param loja A loja vinculada a esse produto
         * @return Uma instância de produto
         */
        public Produto toProduto(Loja loja) {
            return new Produto(nome, valor, tipo, quantidade, marca, descricao, loja);
        }

        /**
         * Converte um produto em um produto de transição
         * @param p uma instância de produto
         * @return Uma instância de produto de transição
         */
        public static ProdutoTransicao fromProduto(Produto p) {
            ProdutoTransicao result = new ProdutoTransicao();
            result.nome = p.getNome();
            result.valor = p.getValor();
            result.tipo = p.getTipo();
            result.quantidade = p.getQuantidade();
            result.marca = p.getMarca();
            result.descricao = p.getDescricao();
            result.lojaDoc = p.getLoja().getCpfCnpj();

            return result;
        }
    }

    private final String fileName;
    private final Gson gson;
    private final LojaRepository lojaRepository;

    /**
     * Construtor
     * @param fileName O arquivo no qual a base de dados será salva
     * @param lojaRepository Um repositório de lojas
     */
    public ProdutoRepository(String fileName, LojaRepository lojaRepository) {
        this.fileName = fileName;
        this.gson = new Gson();
        this.lojaRepository = lojaRepository;
    }

    /**
     * Persiste o produto no repositório
     * @param produto O produto a ser salvo
     */
    public void salvar(Produto produto) {
        List<Produto> produtos = listar();
        produtos.add(produto);
        salvarArquivo(produtos);
    }

    /**
     * Lista todos os produtos salvos na base de dados
     * @return Uma lista com todos os produtos
     */
    public List<Produto> listar() {
        try (FileReader fr = new FileReader(this.fileName)) {
            ProdutoTransicao[] pts = gson.fromJson(fr, ProdutoTransicao[].class);
            List<Produto> produtos = new ArrayList<Produto>();

            for(ProdutoTransicao pt: pts) {
                Optional<Loja> loja = this.lojaRepository.listar().stream()
                    .filter(l -> l.getCpfCnpj().equals(pt.lojaDoc)).findFirst();
                loja.ifPresent(value -> produtos.add(pt.toProduto(value)));
            }

            return produtos;
        } catch (IOException e) {
            System.out.println("Erro ao ler produtos: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Lista todos os produtos por loja
     * @param cpfCnpjLoja O documento da loja
     * @return Todos os produtos vinculados à loja que possui o documento
     */
    public List<Produto> listarPorLoja(String cpfCnpjLoja) {
        List<Produto> todosProdutos = listar();
        List<Produto> produtosDaLoja = new ArrayList<>();
        for (Produto produto : todosProdutos) {
            if (produto.getLoja().getCpfCnpj().equals(cpfCnpjLoja)) {
                produtosDaLoja.add(produto);
            }
        }
        return produtosDaLoja;
    }

    /**
     * Remove o produto da base de dados
     * @param nome O nome do produto
     * @return true se o produto foi removido, senão false
     */
    public boolean remover(String nome) {
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(p -> p.getNome().equalsIgnoreCase(nome));
        if (removido) {
            salvarArquivo(produtos);
        }
        return removido;
    }

    /**
     * Salva todas as entradas em um arquivo
     * @param produtos As entradas de produto
     */
    private void salvarArquivo(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            ProdutoTransicao[] arr = new ProdutoTransicao[produtos.size()];
            List<ProdutoTransicao> pts = new ArrayList<>();
            for(Produto p: produtos) {
                pts.add(ProdutoTransicao.fromProduto(p));
            }
            pts.toArray(arr);
            String json = this.gson.toJson(arr);
            bw.write(json);
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
}
