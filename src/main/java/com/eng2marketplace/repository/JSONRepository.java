package com.eng2marketplace.repository;

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
import java.util.function.Predicate;

/**
 * Classe base para repositórios que armazenam dados em arquivos JSON
 * @param <T> Um tipo que será armazenado
 */
public abstract class JSONRepository<T> {
    private final Gson gson;
    private final Type listType;
    private final String file;

    public JSONRepository(String file, TypeToken<ArrayList<T>> listType) {
        this(file, listType, new GsonBuilder());
    }

    public JSONRepository(String file, TypeToken<ArrayList<T>> listType, GsonBuilder builder) {
        this.listType = listType.getType();
        this.file = file;
        this.gson = builder
            .setPrettyPrinting()
            .create();
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            java.io.File file = new java.io.File(this.file);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                salvarLista(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    private void salvarLista(List<T> itens) {
        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(itens, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    public void salvar(T item) {
        List<T> itens = listar();
        itens.add(item);
        salvarLista(itens);
    }

    public boolean remover(Predicate<T> condicao) {
        List<T> itens = listar();
        boolean removido = itens.removeIf(condicao);
        if(removido)
            salvarLista(itens);
        return removido;
    }

    /**
     * Busca um item por uma condição
     *
     * @param condicao Uma condição que encontra um item
     * @return Optional contendo o item, se encontrado
     */
    public Optional<T> buscar(Predicate<T> condicao) {
        return listar().stream()
            .filter(condicao)
            .findFirst();
    }

    public List<T> listar() {
        try (FileReader reader = new FileReader(this.file)) {
            List<T> itens = gson.fromJson(reader, listType);
            return itens != null ? itens : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void limpar() {
        salvarLista(new ArrayList<>());
    }

    public boolean atualizar(T item, Predicate<T> condicao) {
        if(remover(condicao)) {
            salvar(item);
            return true;
        }
        return false;
    }
}
