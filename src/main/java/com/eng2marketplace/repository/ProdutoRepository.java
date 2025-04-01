package com.eng2marketplace.repository;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepository {
    private static final String ARQUIVO_PRODUTOS = "src/main/data/produtos.json";
    private final LojaRepository lojaRepository;
    private final Gson gson;
    private final Type listType = new TypeToken<ArrayList<Produto>>() {}.getType();
    

    public ProdutoRepository(LojaRepository lojaRepository) {
        this.lojaRepository = lojaRepository;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            java.io.File file = new java.io.File(ARQUIVO_PRODUTOS);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                salvarLista(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    public void salvar(Produto produto) {
        List<Produto> produtos = listar();
        produtos.add(produto);
        salvarLista(produtos);
    }

    public List<Produto> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_PRODUTOS)) {
            List<Produto> produtos = gson.fromJson(reader, listType);
            if (produtos == null) {
                return new ArrayList<>();
            }
            
            // Reconstruir a relação com as lojas
            for (Produto produto : produtos) {
                String cpfCnpjLoja = produto.getLoja().getCpfCnpj();
                Optional<Loja> loja = lojaRepository.listar().stream()
                    .filter(l -> l.getCpfCnpj().equals(cpfCnpjLoja))
                    .findFirst();
                
                loja.ifPresent(produto::setLoja);
            }
            
            return produtos;
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Produto> listarPorLoja(String cpfCnpjLoja) {
        return listar().stream()
            .filter(p -> p.getLoja().getCpfCnpj().equals(cpfCnpjLoja))
            .toList();
    }

    public boolean remover(String nome) {
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(p -> p.getNome().equalsIgnoreCase(nome));
        if (removido) {
            salvarLista(produtos);
        }
        return removido;
    }

    private void salvarLista(List<Produto> produtos) {
        try (FileWriter writer = new FileWriter(ARQUIVO_PRODUTOS)) {
            gson.toJson(produtos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    public Optional<Produto> buscarPorNome(String nome) {
        return listar().stream()
            .filter(p -> p.getNome().equalsIgnoreCase(nome))
            .findFirst();
    }
    public Optional<Produto> buscarPorId(String id) {
        return listar().stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();
    }
    
    public boolean removerPorId(String id) {
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(p -> p.getId().equals(id));
        if (removido) {
            salvarLista(produtos);
        }
        return removido;
    }
}