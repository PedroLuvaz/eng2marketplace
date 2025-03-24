package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LojaRepository {
    private static final String FILE_NAME = "lojas.txt";

    public void salvar(Loja loja) {
        List<Loja> lojas = listar();
        lojas.add(loja);
        salvarArquivo(lojas);
    }

    public List<Loja> listar() {
        List<Loja> lojas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    lojas.add(new Loja(dados[0], dados[1], dados[2], dados[3], dados[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler lojas: " + e.getMessage());
        }
        return lojas;
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Loja loja : lojas) {
                bw.write(loja.getNome() + ";" + loja.getEmail() + ";" + loja.getSenha() + ";" + loja.getCpfCnpj() + ";" + loja.getEndereco());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar lojas: " + e.getMessage());
        }
    }
}
