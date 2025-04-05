package com.eng2marketplace.repository;

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

public class LojaRepository {
    private static final String ARQUIVO_LOJAS = "src/main/data/lojas.json";
    private final Gson gson;
    private final Type listType = new TypeToken<ArrayList<Loja>>() {}.getType();

    public LojaRepository() {
        // Configura o GSON para formatar bonito e lidar com tipos complexos
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            java.io.File file = new java.io.File(ARQUIVO_LOJAS);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Cria diretórios se necessário
                salvarLista(new ArrayList<>()); // Cria arquivo com array vazio
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    public void salvar(Loja loja) {
        List<Loja> lojas = listar();
        lojas.add(loja);
        salvarLista(lojas);
    }

    public List<Loja> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_LOJAS)) {
            List<Loja> lojas = gson.fromJson(reader, listType);
            return lojas != null ? lojas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean remover(String cpfCnpj) {
        List<Loja> lojas = listar();
        boolean removido = lojas.removeIf(loja -> loja.getCpfCnpj().equals(cpfCnpj));
        if (removido) {
            salvarLista(lojas);
        }
        return removido;
    }

    private void salvarLista(List<Loja> lojas) {
        try (FileWriter writer = new FileWriter(ARQUIVO_LOJAS)) {
            gson.toJson(lojas, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    public Optional<Loja> buscarPorCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty()) {
            return Optional.empty();
        }
        
        // Remove formatação para comparação
        String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");
        
        return listar().stream()
            .filter(loja -> {
                String docLoja = loja.getCpfCnpj().replaceAll("[^0-9]", "");
                return docLoja.equals(cpfCnpjNumerico);
            })
            .findFirst();
    }

    /**
 * Atualiza uma loja existente
 * @param loja Loja com os dados atualizados
 * @return true se a atualização foi bem-sucedida, false caso contrário
 */
public boolean atualizar(Loja loja) {
    // Primeiro verifica se a loja existe (com o mesmo CPF/CNPJ)
    Optional<Loja> existente = buscarPorCpfCnpj(loja.getCpfCnpj());
    if (existente.isEmpty()) {
        return false; // Loja não existe para ser atualizada
    }
    
    List<Loja> lojas = listar();
    // Remove a loja antiga
    lojas.removeIf(l -> l.getCpfCnpj().equals(loja.getCpfCnpj()));
    // Adiciona a loja atualizada
    lojas.add(loja);
    salvarLista(lojas);
    return true;
}
}