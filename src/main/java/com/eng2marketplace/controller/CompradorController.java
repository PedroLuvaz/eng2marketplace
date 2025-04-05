package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.repository.CompradorRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class CompradorController {
    private CompradorRepository compradorRepository;
    private Comprador compradorLogado;  // Guarda a referência do comprador logado

    public CompradorController() {
        this.compradorRepository = new CompradorRepository();
        this.compradorLogado = null;
    }

    public void adicionarComprador(String nome, String email, String senha, String cpf, String endereco) {
        // Normaliza o CPF (remove formatação)
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        
        // Validação do CPF
        if (cpfNumerico.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
        
        // Verifica se já existe comprador com este CPF
        if (compradorRepository.buscarPorCpf(cpfNumerico).isPresent()) {
            throw new IllegalArgumentException("Já existe um comprador cadastrado com este CPF");
        }
        
        // Cria e salva o novo comprador com CPF normalizado (sem formatação)
        Comprador novoComprador = new Comprador(nome, email, senha, cpfNumerico, endereco);
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

public boolean login(String cpf, String senha) {
    try {
        Comprador comprador = buscarCompradorPorCpf(cpf);
        if (comprador != null && comprador.getSenha().equals(senha)) {
            this.compradorLogado = comprador;
            return true;
        }
        return false;
    } catch (NoSuchElementException e) {
        return false;
    }
}

/**
 * Realiza o logout do comprador
 */
public void logout() {
    this.compradorLogado = null;
}

/**
 * Verifica se há um comprador logado
 * @return true se há um comprador logado, false caso contrário
 */
public boolean isLoggedIn() {
    return this.compradorLogado != null;
}

/**
 * Obtém o comprador atualmente logado
 * @return O comprador logado ou null se não houver ninguém logado
 */
public Comprador getCompradorLogado() {
    return this.compradorLogado;
}

/**
 * Adiciona um produto ao carrinho do comprador logado
 * @param produtoId ID ou código do produto
 * @return true se o produto foi adicionado com sucesso, false caso contrário
 */
public boolean adicionarAoCarrinho(String produtoId, int quantidade) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Nenhum comprador logado");
        }
        
        // Verifica se a quantidade é válida (> 0)
        if (quantidade <= 0) {
            return false;
        }
        
        // Atualiza o carrinho
        int quantidadeAtual = compradorLogado.getCarrinho().getOrDefault(produtoId, 0);
        compradorLogado.getCarrinho().put(produtoId, quantidadeAtual + quantidade);
        
        atualizarCompradorNoRepositorio();
        return true; // Retorna boolean indicando sucesso
    }

/**
 * Remove um produto do carrinho do comprador logado
 * @param produtoId ID ou código do produto
 * @return true se o produto foi removido com sucesso, false caso contrário
 */
public boolean removerDoCarrinho(String produtoId) {
    if (!isLoggedIn()) {
        throw new IllegalStateException("Nenhum comprador logado");
    }
    
    // Verifica se o produto estava no carrinho antes de remover
    boolean existiaNoCarrinho = compradorLogado.getCarrinho().containsKey(produtoId);
    
    if (existiaNoCarrinho) {
        compradorLogado.getCarrinho().remove(produtoId);
        atualizarCompradorNoRepositorio();
    }
    
    return existiaNoCarrinho;
}

/**
 * Limpa o carrinho do comprador logado
 */
public void limparCarrinho() {
    if (!isLoggedIn()) {
        throw new IllegalStateException("Nenhum comprador logado");
    }
    
    compradorLogado.getCarrinho().clear();
    atualizarCompradorNoRepositorio();
}

/**
 * Obtém os itens do carrinho do comprador logado
 * @return Lista de IDs ou códigos dos produtos no carrinho
 */
public Map<String, Integer> getCarrinho() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Nenhum comprador logado");
        }
        return compradorLogado.getCarrinho();
    }

/**
 * Atualiza os dados do comprador logado no repositório
 */
private void atualizarCompradorNoRepositorio() {
    if (compradorLogado != null) {
        compradorRepository.remover(compradorLogado.getCpf());
        compradorRepository.salvar(compradorLogado);
    }
}

public boolean alterarQuantidadeCarrinho(String produtoId, int novaQuantidade) {
    if (!isLoggedIn()) {
        throw new IllegalStateException("Nenhum comprador logado");
    }
    
    if (novaQuantidade <= 0) {
        return false;
    }
    
    if (compradorLogado.getCarrinho().containsKey(produtoId)) {
        compradorLogado.getCarrinho().put(produtoId, novaQuantidade);
        atualizarCompradorNoRepositorio();
        return true;
    }
    
    return false;
}


}
