package com.eng2marketplace.repository;

import com.eng2marketplace.model.Comprador;
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
 * Repositório para gerenciar operações de persistência de dados de compradores.
 * Os dados são armazenados em um arquivo JSON.
 */
public class CompradorRepository {

    private static final String ARQUIVO_COMPRADORES = "src/main/data/compradores.json"; // Caminho do arquivo JSON
    private final Gson gson; // Instância do Gson para manipulação de JSON
    private final Type listType; // Tipo genérico para lista de compradores

    /**
     * Construtor da classe. Inicializa o Gson e garante que o arquivo JSON exista.
     */
    public CompradorRepository() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        this.listType = new TypeToken<ArrayList<Comprador>>() {}.getType();
        criarArquivoSeNaoExistir();
    }

    /**
     * Cria o arquivo JSON caso ele não exista.
     */
    private void criarArquivoSeNaoExistir() {
        try {
            File file = new File(ARQUIVO_COMPRADORES);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Cria os diretórios necessários
                salvarLista(new ArrayList<>()); // Cria um arquivo vazio
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Salva um novo comprador no arquivo JSON.
     *
     * @param comprador Comprador a ser salvo.
     */
    public void salvar(Comprador comprador) {
        List<Comprador> compradores = listar();
        compradores.add(comprador);
        salvarLista(compradores);
    }

    /**
     * Lista todos os compradores armazenados no arquivo JSON.
     *
     * @return Lista de compradores.
     */
    public List<Comprador> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_COMPRADORES)) {
            List<Comprador> compradores = gson.fromJson(reader, listType);
            return compradores != null ? compradores : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Remove um comprador pelo CPF.
     *
     * @param cpf CPF do comprador a ser removido.
     * @return true se o comprador foi removido, false caso contrário.
     */
    public boolean remover(String cpf) {
        List<Comprador> compradores = listar();
        boolean removido = compradores.removeIf(comprador -> comprador.getCpf().equals(cpf));
        if (removido) {
            salvarLista(compradores);
        }
        return removido;
    }

    /**
     * Salva a lista de compradores no arquivo JSON.
     *
     * @param compradores Lista de compradores a ser salva.
     */
    private void salvarLista(List<Comprador> compradores) {
        try (FileWriter writer = new FileWriter(ARQUIVO_COMPRADORES)) {
            gson.toJson(compradores, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Remove todos os compradores do arquivo JSON.
     */
    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    /**
     * Busca um comprador pelo CPF.
     *
     * @param cpf CPF do comprador a ser buscado.
     * @return Optional contendo o comprador, se encontrado.
     */
    public Optional<Comprador> buscarPorCpf(String cpf) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", ""); // Remove formatação do CPF
        return listar().stream()
            .filter(comprador -> {
                String cpfComprador = comprador.getCpf().replaceAll("[^0-9]", "");
                return cpfComprador.equals(cpfNumerico);
            })
            .findFirst();
    }

    /**
     * Atualiza os dados de um comprador existente.
     *
     * @param comprador Comprador com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizar(Comprador comprador) {
        Optional<Comprador> existente = buscarPorCpf(comprador.getCpf());
        if (existente.isEmpty()) {
            return false; // Comprador não existe para ser atualizado
        }

        List<Comprador> compradores = listar();
        compradores.removeIf(c -> c.getCpf().equals(comprador.getCpf())); // Remove o comprador antigo
        compradores.add(comprador); // Adiciona o comprador atualizado
        salvarLista(compradores);
        return true;
    }
}