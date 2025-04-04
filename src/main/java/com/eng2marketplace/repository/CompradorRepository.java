package com.eng2marketplace.repository;

import com.eng2marketplace.model.Comprador;
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

public class CompradorRepository {
    private static final String ARQUIVO_COMPRADORES = "src/main/data/compradores.json";
    private final Gson gson;
    private final Type listType = new TypeToken<ArrayList<Comprador>>() {}.getType();

    public CompradorRepository() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            java.io.File file = new java.io.File(ARQUIVO_COMPRADORES);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                salvarLista(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    public void salvar(Comprador comprador) {
        List<Comprador> compradores = listar();
        compradores.add(comprador);
        salvarLista(compradores);
    }

    public List<Comprador> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_COMPRADORES)) {
            List<Comprador> compradores = gson.fromJson(reader, listType);
            return compradores != null ? compradores : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean remover(String cpf) {
        List<Comprador> compradores = listar();
        boolean removido = compradores.removeIf(comprador -> comprador.getCpf().equals(cpf));
        if (removido) {
            salvarLista(compradores);
        }
        return removido;
    }

    private void salvarLista(List<Comprador> compradores) {
        try (FileWriter writer = new FileWriter(ARQUIVO_COMPRADORES)) {
            gson.toJson(compradores, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    public Optional<Comprador> buscarPorCpf(String cpf) {
        // Remove formatação para comparação
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        
        return listar().stream()
            .filter(comprador -> {
                String cpfComprador = comprador.getCpf().replaceAll("[^0-9]", "");
                return cpfComprador.equals(cpfNumerico);
            })
            .findFirst();
    }
/**
 * Atualiza um comprador existente
 * @param comprador Comprador com os dados atualizados
 * @return true se a atualização foi bem-sucedida, false caso contrário
 */
public boolean atualizar(Comprador comprador) {
    List<Comprador> compradores = listar();
    boolean removido = compradores.removeIf(c -> c.getCpf().equals(comprador.getCpf()));
    if (removido) {
        compradores.add(comprador);
        salvarLista(compradores);
        return true;
    }
    return false;
}

}