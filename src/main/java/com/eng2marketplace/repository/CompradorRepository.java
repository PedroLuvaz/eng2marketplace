package com.eng2marketplace.repository;

import com.eng2marketplace.model.Comprador;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repositório de compradores
 */
public class CompradorRepository {
    private final String fileName;
    private final Gson gson;

    public CompradorRepository(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    /**
     * Salva um comprador na base de dados
     * @param comprador O comprador a ser persistido
     */
    public void salvar(Comprador comprador) {
        List<Comprador> compradores = listar();
        compradores.add(comprador);
        salvarArquivo(compradores);
    }

    /**
     * Lista todos os compradores na base de dados
     * @return Uma lista com todos os compradores
     */
    public List<Comprador> listar() {
        try (FileReader fr = new FileReader(this.fileName)) {
            Comprador[] compradores = this.gson.fromJson(fr, Comprador[].class);
            return new ArrayList<>(Arrays.asList(compradores));
        } catch (IOException e) {
            System.out.println("Erro ao salvar compradores: " + e.getMessage());
        }
        return new ArrayList<Comprador>();
    }

    /**
     * Remove um comprador da base de dados
     * @param cpf O cpf do comprador
     * @return true se o comprador foi removido, senão false
     */
    public boolean remover(String cpf) {
        List<Comprador> compradores = listar();
        boolean removido = compradores.removeIf(comprador -> comprador.getCpf().equals(cpf));
        if (removido) {
            salvarArquivo(compradores);
        }
        return removido;
    }

    /**
     * Salva a base de dados em um arquivo
     * @param compradores A base de dados, lista de todos os compradores
     */
    private void salvarArquivo(List<Comprador> compradores) {
        Comprador[] arr = new Comprador[compradores.size()];
        compradores.toArray(arr);
        String json = this.gson.toJson(arr);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            bw.write(json);
        } catch (IOException e) {
            System.out.println("Erro ao salvar compradores: " + e.getMessage());
        }
    }
}
