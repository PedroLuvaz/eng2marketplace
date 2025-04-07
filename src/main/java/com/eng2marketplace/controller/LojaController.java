package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.LojaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável por gerenciar as operações relacionadas às lojas.
 */
public class LojaController {

    private LojaRepository lojaRepository;
    private Loja lojaLogada;
    private int tentativasLogin = 0;

    /**
     * Construtor padrão que inicializa o repositório de lojas.
     */
    public LojaController() {
        this.lojaRepository = new LojaRepository();
    }

    /**
     * Adiciona uma nova loja ao sistema.
     *
     * @param nome     Nome da loja.
     * @param email    Email da loja.
     * @param senha    Senha da loja.
     * @param cpfCnpj  CPF ou CNPJ da loja.
     * @param endereco Endereço da loja.
     */
    public void adicionarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        // Remove formatação do CPF/CNPJ
        String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");

        // Validação básica do CPF/CNPJ
        if (cpfCnpjNumerico.length() != 11 && cpfCnpjNumerico.length() != 14) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos ou CNPJ deve ter 14 dígitos");
        }

        // Verifica se já existe uma loja com o mesmo CPF/CNPJ
        if (lojaRepository.buscarPorCpfCnpj(cpfCnpjNumerico).isPresent()) {
            throw new IllegalArgumentException("Já existe uma loja cadastrada com este CPF/CNPJ");
        }

        // Cria e salva a nova loja
        Loja novaLoja = new Loja(nome, email, senha, cpfCnpjNumerico, endereco);
        lojaRepository.salvar(novaLoja);
    }

    /**
     * Lista todas as lojas cadastradas.
     *
     * @return Lista de lojas.
     */
    public List<Loja> listarLojas() {
        return lojaRepository.listar();
    }

    /**
     * Remove uma loja com base no CPF/CNPJ.
     *
     * @param cpfCnpj CPF ou CNPJ da loja a ser removida.
     * @return true se a loja foi removida, false caso contrário.
     */
    public boolean removerLoja(String cpfCnpj) {
        return lojaRepository.remover(cpfCnpj);
    }

    /**
     * Busca uma loja pelo CPF/CNPJ.
     *
     * @param cpfCnpj CPF ou CNPJ da loja a ser buscada.
     * @return A loja encontrada ou null se não existir.
     */
    public Loja buscarLojaPorCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF/CNPJ não pode ser nulo ou vazio");
        }

        // Remove formatação para busca
        String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");

        return lojaRepository.buscarPorCpfCnpj(cpfCnpjNumerico).orElse(null);
    }

    /**
     * Realiza o login de uma loja com base no CPF/CNPJ e senha.
     *
     * @param cpfCnpj CPF ou CNPJ da loja.
     * @param senha   Senha da loja.
     * @return A loja logada.
     */
    public Loja fazerLogin(String cpfCnpj, String senha) {
        // Remove formatação do CPF/CNPJ
        String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");

        // Verifica o número de tentativas de login
        if (tentativasLogin >= 5) {
            throw new IllegalStateException("Número máximo de tentativas excedido. Voltando ao menu principal.");
        }

        // Busca a loja no repositório
        Optional<Loja> lojaOpt = lojaRepository.buscarPorCpfCnpj(cpfCnpjNumerico);

        // Verifica se a loja existe e se a senha está correta
        if (lojaOpt.isPresent() && lojaOpt.get().getSenha().equals(senha)) {
            this.lojaLogada = lojaOpt.get();
            this.tentativasLogin = 0; // Reseta o contador de tentativas
            return lojaLogada;
        } else {
            tentativasLogin++;
            throw new IllegalArgumentException("CPF/CNPJ ou senha incorretos. Tentativas restantes: " + (5 - tentativasLogin));
        }
    }

    /**
     * Realiza o logout da loja logada.
     */
    public void logout() {
        this.lojaLogada = null;
    }

    /**
     * Verifica se há uma loja logada no sistema.
     *
     * @return true se há uma loja logada, false caso contrário.
     */
    public boolean isLoggedIn() {
        return this.lojaLogada != null;
    }

    /**
     * Retorna a loja atualmente logada.
     *
     * @return A loja logada ou null se não houver nenhuma loja logada.
     */
    public Loja getLojaLogada() {
        return this.lojaLogada;
    }
}
