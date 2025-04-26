package com.eng2marketplace.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do modelo usuário administrador
 */
class AdministradorTest {

    /**
     * Testa recuperar o nome
     */
    @Test
    void testNome() {
        Administrador a = new Administrador("João Santos", "jao@mail.net", "01234567890");
        assertEquals("João Santos", a.getNome());
    }

    /**
     * Testa recuperar o email
     */
    @Test
    void testEmail() {
        Administrador a = new Administrador("Fernanda Ferreira", "nandaf@personalmail.br", "abcdefgh");
        assertEquals("nandaf@personalmail.br", a.getEmail());
    }

    /**
     * Testa recuperar a senha
     */
    @Test
    void testSenha() {
        Administrador a = new Administrador("José Maria", "jm@local.com", "11111111");
        assertEquals("11111111", a.getSenha());
    }

    /**
     * Testa se o administrador tem permissões
     */
    @Test
    void temPermissao() {
        Administrador a = new Administrador("João Santos", "jao@mail.net", "01234567890");
        assertTrue(a.temPermissao("Qualquer coisa"));
    }
}
