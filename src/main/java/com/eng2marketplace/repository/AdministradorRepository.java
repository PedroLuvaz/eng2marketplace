package com.eng2marketplace.repository;

import com.eng2marketplace.model.Administrador;
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
 * Repositório para gerenciar operações de persistência de dados dos administradores.
 * Os dados são armazenados em um arquivo JSON.
 */
public class AdministradorRepository {

    private static final String ARQUIVO_ADMINISTRADORES = "src/main/data/administradores.json";
    private final Gson gson;
    private final Type listType;

    /**
     * Construtor. Inicializa o Gson e garante que o arquivo JSON exista.
     */
    public AdministradorRepository() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        this.listType = new TypeToken<ArrayList<Administrador>>() {}.getType();
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            File file = new File(ARQUIVO_ADMINISTRADORES);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                salvarLista(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON de administradores: " + e.getMessage());
        }
    }

    public void salvar(Administrador administrador) {
        List<Administrador> administradores = listar();
        administradores.add(administrador);
        salvarLista(administradores);
    }

    public List<Administrador> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_ADMINISTRADORES)) {
            List<Administrador> administradores = gson.fromJson(reader, listType);
            return administradores != null ? administradores : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler administradores do arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<Administrador> buscarPorEmail(String email) {
        return listar().stream()
            .filter(admin -> admin.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }

    public boolean removerPorEmail(String email) {
        List<Administrador> administradores = listar();
        boolean removido = administradores.removeIf(admin -> admin.getEmail().equalsIgnoreCase(email));
        if (removido) {
            salvarLista(administradores);
        }
        return removido;
    }

    private void salvarLista(List<Administrador> administradores) {
        try (FileWriter writer = new FileWriter(ARQUIVO_ADMINISTRADORES)) {
            gson.toJson(administradores, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar administradores no arquivo JSON: " + e.getMessage());
        }
    }

    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    public boolean atualizar(Administrador administrador) {
        Optional<Administrador> existente = buscarPorEmail(administrador.getEmail());
        if (existente.isEmpty()) {
            return false;
        }

        List<Administrador> administradores = listar();
        administradores.removeIf(a -> a.getEmail().equalsIgnoreCase(administrador.getEmail()));
        administradores.add(administrador);
        salvarLista(administradores);
        return true;
    }
}