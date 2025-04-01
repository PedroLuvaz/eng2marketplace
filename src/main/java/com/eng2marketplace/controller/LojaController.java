package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.LojaRepository;
import java.util.List;

public class LojaController {
    private LojaRepository lojaRepository;

    public LojaController() {
        this.lojaRepository = new LojaRepository();
    }

    public void adicionarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        // Valida se já existe loja com este CPF/CNPJ
        if (lojaRepository.buscarPorCpfCnpj(cpfCnpj).isPresent()) {
            throw new IllegalArgumentException("Já existe uma loja cadastrada com este CPF/CNPJ");
        }
        
        // Cria e salva a nova loja
        Loja novaLoja = new Loja(nome, email, senha, cpfCnpj, endereco);
        lojaRepository.salvar(novaLoja);
    }

    public List<Loja> listarLojas() {
        return lojaRepository.listar();
    }

    public boolean removerLoja(String cpfCnpj) {
        return lojaRepository.remover(cpfCnpj);
    }

   /**
 * Busca uma loja pelo CPF/CNPJ
 * @param cpfCnpj CPF ou CNPJ da loja a ser buscada
 * @return A loja encontrada ou null se não existir
 */
public Loja buscarLojaPorCpfCnpj(String cpfCnpj) {
    if (cpfCnpj == null || cpfCnpj.trim().isEmpty()) {
        throw new IllegalArgumentException("CPF/CNPJ não pode ser nulo ou vazio");
    }
    
    // Remove formatação para busca
    String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");
    
    return lojaRepository.buscarPorCpfCnpj(cpfCnpj).orElse(null);
}
}
