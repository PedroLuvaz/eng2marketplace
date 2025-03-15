package com.eng2marketplace.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Teste de unidade de loja.
 */
class LojaTest {

    /**
     * Testa atribuir e recuperar o nome da loja
     */
    @Test
    void testNome() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        store.setNome("Games2Games");
        // BUG: E se o argumento for null?

        assertEquals("Games2Games", store.getNome());
    }

    /**
     * Testa atribuir e recuperar o email da loja
     */
    @Test
    void testEmail() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        store.setEmail("games-games-games@e-mail.to");

        assertEquals("games-games-games@e-mail.to", store.getEmail());
    }

    /**
     * Testa atribuir e recuperar a senha da loja
     */
    @Test
    void testSenha() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        store.setSenha("senha mais segura que ninguem pode adivinhar");

        assertEquals("senha mais segura que ninguem pode adivinhar", store.getSenha());
    }

    /**
     * Testa atribuir e recuperar o doc. de identificação da loja
     */
    @Test
    void testCpfCnpj() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        store.setCpfCnpj("111.222.333-44");

        assertEquals("111.222.333-44", store.getCpfCnpj());
    }

    /**
     * Testa atribuir e recuperar o endereço da loja
     */
    @Test
    void testEndereco() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        store.setEndereco("Avenida sem saída, 100");

        assertEquals("Avenida sem saída, 100", store.getEndereco());
    }

    /**
     * Testa representação da loja em forma textual.
     */
    @Test
    void testToString() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");

        String storeRepr = store.toString();

        assertEquals("Loja{nome='Brookdale Equipamentos', email='brkdl@mail.net', cpfCnpj='012.345.678-90', endereco='Rua dos Mafagafos, 1'}", storeRepr);
    }
}
