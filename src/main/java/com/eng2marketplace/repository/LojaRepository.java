package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável por gerenciar as operações de persistência de lojas.
 */
public class LojaRepository {

    private static final String ARQUIVO_LOJAS = "src/main/data/lojas.json"; // Caminho do arquivo JSON
    private final Gson gson; // Instância do Gson para manipulação de JSON
    private final Type listType = new TypeToken<ArrayList<Loja>>() {}.getType(); // Tipo genérico para lista de lojas

    /**
     * Construtor que inicializa o Gson e garante que o arquivo JSON exista.
     */
    public LojaRepository() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting() // Configura o Gson para formatar o JSON de forma legível
            .create();
        criarArquivoSeNaoExistir();
    }

    /**
     * Cria o arquivo JSON caso ele não exista.
     */
    private void criarArquivoSeNaoExistir() {
        try {
            File file = new File(ARQUIVO_LOJAS);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Cria diretórios, se necessário
                salvarLista(new ArrayList<>()); // Cria o arquivo com uma lista vazia
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Salva uma nova loja no repositório.
     * 
     * @param loja Loja a ser salva
     */
    public void salvar(Loja loja) {
        List<Loja> lojas = listar();
        lojas.add(loja);
        salvarLista(lojas);
    }

    /**
     * Lista todas as lojas armazenadas no repositório.
     * 
     * @return Lista de lojas
     */
    public List<Loja> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_LOJAS)) {
            List<Loja> lojas = gson.fromJson(reader, listType);
            return lojas != null ? lojas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Remove uma loja com base no CPF/CNPJ.
     * 
     * @param cpfCnpj CPF ou CNPJ da loja a ser removida
     * @return true se a loja foi removida, false caso contrário
     */
    public boolean remover(String cpfCnpj) {
        List<Loja> lojas = listar();
        boolean removido = lojas.removeIf(loja -> loja.getCpfCnpj().equals(cpfCnpj));
        if (removido) {
            salvarLista(lojas);
        }
        return removido;
    }

    /**
     * Salva a lista de lojas no arquivo JSON.
     * 
     * @param lojas Lista de lojas a ser salva
     */
    private void salvarLista(List<Loja> lojas) {
        try (FileWriter writer = new FileWriter(ARQUIVO_LOJAS)) {
            gson.toJson(lojas, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Remove todas as lojas do repositório.
     */
    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    /**
     * Busca uma loja pelo CPF/CNPJ.
     * 
     * @param cpfCnpj CPF ou CNPJ da loja
     * @return Optional contendo a loja, se encontrada
     */
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
     * Atualiza os dados de uma loja existente.
     * 
     * @param loja Loja com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Loja loja) {
        // Verifica se a loja existe com o mesmo CPF/CNPJ
        Optional<Loja> existente = buscarPorCpfCnpj(loja.getCpfCnpj());
        if (existente.isEmpty()) {
            return false; // Loja não encontrada
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