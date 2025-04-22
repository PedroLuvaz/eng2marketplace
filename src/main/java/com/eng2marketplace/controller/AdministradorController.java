package com.eng2marketplace.controller;

import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.repository.AdministradorRepository;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos administradores.
 */
public class AdministradorController {
    private final AdministradorRepository administradorRepository;
    private Administrador administradorLogado; // Administrador logado
    private int tentativasLogin; // Contador de tentativas de login

    /**
     * Construtor da classe AdministradorController.
     */
    public AdministradorController() {
        this.administradorRepository = new AdministradorRepository();
        this.administradorLogado = null;
        this.tentativasLogin = 0;
    }

    /**
     * Realiza o login de um administrador.
     *
     * @param email Email do administrador.
     * @param senha Senha do administrador.
     * @return true se o login foi bem-sucedido, false caso contrário.
     */
    public boolean login(String email, String senha) {
        try {
            Administrador administrador = administradorRepository
                .buscarPorEmail(email)
                .orElseThrow(); // Lança NoSuchElementException se não encontrar
    
            if (administrador.getSenha().equals(senha)) {
                administradorLogado = administrador;
                tentativasLogin = 0;
                return true;
            }
        } catch (NoSuchElementException e) {
            // Ignorado propositalmente, será tratado abaixo
        }
    
        tentativasLogin++;
        if (tentativasLogin >= 5) {
            tentativasLogin = 0;
            throw new IllegalStateException("Número máximo de tentativas excedido");
        }
    
        return false;
    }

    /**
     * Realiza o logout do administrador logado.
     */
    public void logout() {
        administradorLogado = null;
    }

    /**
     * Verifica se há um administrador logado.
     *
     * @return true se há um administrador logado, false caso contrário.
     */
    public boolean isLoggedIn() {
        return administradorLogado != null;
    }

    /**
     * Adiciona um novo administrador ao sistema.
     *
     * @param nome  Nome do administrador.
     * @param email Email do administrador.
     * @param senha Senha do administrador.
     */
    public void adicionarAdministrador(String nome, String email, String senha) {
        if (administradorRepository.buscarPorEmail(email).isPresent()) {
            throw new IllegalArgumentException("Já existe um administrador com este email");
        }
        administradorRepository.salvar(new Administrador(nome, email, senha));
    }

    /**
     * Remove um administrador pelo email.
     *
     * @param email Email do administrador.
     * @return true se o administrador foi removido, false caso contrário.
     */
    public boolean removerAdministrador(String email) {
        return administradorRepository.removerPorEmail(email);
    }

    /**
     * Busca um administrador pelo email.
     *
     * @param email Email do administrador.
     * @return O administrador encontrado.
     * @throws NoSuchElementException Se o administrador não for encontrado.
     */
    public Administrador buscarAdministradorPorEmail(String email) {
        return administradorRepository.buscarPorEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Administrador não encontrado: " + email));
    }

    /**
     * Verifica se o administrador logado tem permissões para uma operação.
     *
     * @param operacao Operação a ser verificada.
     * @return true se o administrador tem permissão, false caso contrário.
     */
    public boolean verificarPermissao(String operacao) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Nenhum administrador logado");
        }
        return administradorLogado.temPermissao(operacao);
    }

    public List<Administrador> listarAdministradores() {
    return administradorRepository.listar();  // Assumindo que o repositório tem esse método
    }
}