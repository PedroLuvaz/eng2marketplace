package com.eng2marketplace.repository;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepository {
    private static final String FILE_NAME = "./data/produtos.txt";
    private final LojaRepository lojaRepository;

    public ProdutoRepository(LojaRepository lojaRepository) {
        this.lojaRepository = lojaRepository;
    }

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
                    String cpfCnpjLoja = dados[6];
                    Optional<Loja> loja = lojaRepository.listar().stream()
                            .filter(l -> l.getCpfCnpj().equals(cpfCnpjLoja))
                            .findFirst();

                    if (loja.isPresent()) {
                        produtos.add(new Produto(
                                dados[0],
                                Double.parseDouble(dados[1]),
                                dados[2],
                                Integer.parseInt(dados[3]),
                                dados[4],
                                dados[5],
                                loja.get()
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler produtos: " + e.getMessage());
        }
        return produtos;
    }

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

    public boolean remover(String nome) {
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(p -> p.getNome().equalsIgnoreCase(nome));
        if (removido) {
            salvarArquivo(produtos);
        }
        return removido;
    }

    private void salvarArquivo(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Produto produto : produtos) {
                bw.write(produto.getNome() + ";" +
                        produto.getValor() + ";" +
                        produto.getTipo() + ";" +
                        produto.getQuantidade() + ";" +
                        produto.getMarca() + ";" +
                        produto.getDescricao() + ";" +
                        produto.getLoja().getCpfCnpj());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
}