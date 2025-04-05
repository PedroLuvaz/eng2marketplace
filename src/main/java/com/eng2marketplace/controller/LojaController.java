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
        // Normaliza o CPF/CNPJ (remove formatação)
        String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");
        
        // Validação básica do CPF/CNPJ
        if (cpfCnpjNumerico.length() != 11 && cpfCnpjNumerico.length() != 14) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos");
        }
        
        // Verifica se já existe loja com este CPF/CNPJ
        if (lojaRepository.buscarPorCpfCnpj(cpfCnpjNumerico).isPresent()) {
            throw new IllegalArgumentException("Já existe uma loja cadastrada com este CPF/CNPJ");
        }
        
        // Cria e salva a nova loja com CPF/CNPJ normalizado (sem formatação)
        Loja novaLoja = new Loja(nome, email, senha, cpfCnpjNumerico, endereco);
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
