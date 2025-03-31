package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LojaRepository {

    private final String fileName;
    private final Gson gson;

    public LojaRepository(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    public void salvar(Loja loja) {
        List<Loja> lojas = listar();
        lojas.add(loja);
        salvarArquivo(lojas);
    }

    public List<Loja> listar() {
        try (FileReader fr = new FileReader(this.fileName)) {
            Loja[] lojas = gson.fromJson(fr, Loja[].class);
            return new ArrayList<>(Arrays.asList(lojas));
        } catch (IOException e) {
            System.out.println("Erro ao ler lojas: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean remover(String cpfCnpj) {
        List<Loja> lojas = listar();
        boolean removido = lojas.removeIf(loja -> loja.getCpfCnpj().equals(cpfCnpj));
        if (removido) {
            salvarArquivo(lojas);
        }
        return removido;
    }

    private void salvarArquivo(List<Loja> lojas) {
        Loja[] arr = new Loja[lojas.size()];
        lojas.toArray(arr);
        String json = this.gson.toJson(arr);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            bw.write(json);
        } catch (IOException e) {
            System.out.println("Erro ao salvar lojas: " + e.getMessage());
        }
    }
}
