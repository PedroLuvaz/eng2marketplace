package com.eng2marketplace.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do comprador.
 */
class CompradorTest {

    /**
     * Testa atribuir e recuperar o nome do comprador
     */
    @Test
    void testNome() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        buyer.setNome("João Maria");
        // BUG: E se o argumento for null?

        assertEquals("João Maria", buyer.getNome());
    }

    /**
     * Testa atribuir e recuperar o email do comprador
     */
    @Test
    void testEmail() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        buyer.setEmail("John@professional.com");

        assertEquals("John@professional.com", buyer.getEmail());
    }

    /**
     * Testa atribuir e recuperar a senha do comprador
     */
    @Test
    void testSenha() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        buyer.setSenha("senha muito segura ninguem consegue adivinhar hehe");

        assertEquals("senha muito segura ninguem consegue adivinhar hehe", buyer.getSenha());
    }

    /**
     * Testa atribuir e recuperar o doc. de identificação do comprador
     */
    @Test
    void testCpf() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        buyer.setCpf("111.222.333-44");

        assertEquals("111.222.333-44", buyer.getCpf());
    }

    /**
     * Testa atribuir e recuperar o endereço do comprador
     */
    @Test
    void testEndereco() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        buyer.setEndereco("Rua Pom Maven, 99A");

        assertEquals("Rua Pom Maven, 99A", buyer.getEndereco());
    }

    /**
     * Testa atribuir e recuperar o carrinho do comprador
     */
    @Test
    void testCarrinho() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        buyer.getCarrinho().put("00000", 1);

        assertFalse(buyer.getCarrinho().isEmpty());

        buyer.setCarrinho(new HashMap<>());

        assertTrue(buyer.getCarrinho().isEmpty());
    }
    /**
     * Testa representação da loja em forma textual.
     */
    @Test
    void testToString() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        String buyerRepr = buyer.toString();

        assertEquals("Comprador{nome='João', email='jao@contact.me', cpf='000.111.222-33', endereço='Rua Teló, S/N'}", buyerRepr);
    }
}
