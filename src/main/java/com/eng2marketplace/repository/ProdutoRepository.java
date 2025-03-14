package com.eng2marketplace.repository;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private static final String FILE_NAME = "produtos.txt";

    public void salvar(Produto produto) {
        List<Produto> produtos = listar();
        produtos.add(produto);
        salvarArquivo(produtos);
    }

    public List<Produto> listar() {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 7) {
                    Loja loja = new Loja(dados[6], "", "", "", ""); 
                    produtos.add(new Produto(dados[0], Double.parseDouble(dados[1]), dados[2], Integer.parseInt(dados[3]), dados[4], dados[5], loja));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler produtos: " + e.getMessage());
        }
        return produtos;
    }

    public boolean remover(String nome) {
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(produto -> produto.getNome().equals(nome));
        if (removido) {
            salvarArquivo(produtos);
        }
        return removido;
    }

    private void salvarArquivo(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Produto produto : produtos) {
                bw.write(produto.getNome() + ";" + produto.getValor() + ";" + produto.getTipo() + ";" + produto.getQuantidade() + ";" +
                        produto.getMarca() + ";" + produto.getDescricao() + ";" + produto.getLoja().getNome());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
}
