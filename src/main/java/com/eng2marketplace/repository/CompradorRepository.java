package com.eng2marketplace.repository;

import com.eng2marketplace.model.Comprador;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompradorRepository {
    private static final String FILE_NAME = "./data/compradores.txt";

    public void salvar(Comprador comprador) {
        List<Comprador> compradores = listar();
        compradores.add(comprador);
        salvarArquivo(compradores);
    }

    public List<Comprador> listar() {
        List<Comprador> compradores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 4) {
                    compradores.add(new Comprador(dados[0], dados[1], dados[2], dados[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler compradores: " + e.getMessage());
        }
        return compradores;
    }

    public boolean remover(String cpf) {
        List<Comprador> compradores = listar();
        boolean removido = compradores.removeIf(comprador -> comprador.getCpf().equals(cpf));
        if (removido) {
            salvarArquivo(compradores);
        }
        return removido;
    }

    private void salvarArquivo(List<Comprador> compradores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Comprador comprador : compradores) {
                bw.write(comprador.getNome() + ";" + comprador.getEmail() + ";" + comprador.getSenha() + ";" + comprador.getCpf());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar compradores: " + e.getMessage());
        }
    }
}
