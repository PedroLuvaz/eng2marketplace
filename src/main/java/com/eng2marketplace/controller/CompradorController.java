package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.repository.CompradorRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class CompradorController {
    private CompradorRepository compradorRepository;

    public CompradorController() {
        this.compradorRepository = new CompradorRepository();
    }

    public void adicionarComprador(String nome, String email, String senha, String cpf, String endereco) {
        // Primeiro valida se já existe comprador com este CPF
        if (compradorRepository.buscarPorCpf(cpf).isPresent()) {
            throw new IllegalArgumentException("Já existe um comprador cadastrado com este CPF");
        }
        
        // Cria e salva o novo comprador
        Comprador novoComprador = new Comprador(nome, email, senha, cpf, endereco);
        compradorRepository.salvar(novoComprador);
    }


    public List<Comprador> listarCompradores() {
        return compradorRepository.listar();
    }

    public boolean removerComprador(String cpf) {
        return compradorRepository.remover(cpf);
    }

   /**
 * Busca um comprador pelo CPF
 * @param cpf CPF do comprador no formato 000.000.000-00 ou 00000000000
 * @return O comprador encontrado ou null se não existir
 * @throws IllegalArgumentException se o CPF for inválido
 */
public Comprador buscarCompradorPorCpf(String cpf) {
    // Validação rigorosa do CPF
    if (cpf == null || cpf.trim().isEmpty()) {
        throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
    }
    
    // Normaliza o CPF (remove formatação)
    String cpfNumerico = cpf.replaceAll("[^0-9]", "");
    
    if (cpfNumerico.length() != 11) {
        throw new IllegalArgumentException("CPF deve conter 11 dígitos");
    }
    
    // Busca no repositório com tratamento de caso não encontrado
    return compradorRepository.buscarPorCpf(cpfNumerico)
            .orElseThrow(() -> new NoSuchElementException("Comprador não encontrado para CPF: " + cpf));
}


}
