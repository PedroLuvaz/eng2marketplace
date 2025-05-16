package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.LojaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de lojas.
 */
class LojaControllerTest {
    private static final String TEST_FILE_PATH = "src/main/data/lojas.json";

    @BeforeEach
    @AfterEach
    void cleanUp() {
        LojaRepository lr = new LojaRepository();
        lr.limpar();
    }

    /**
     * Testa persistir instância de loja
     */
    @Test
    void testSalvar() {
        LojaController control = new LojaController();
        control.adicionarLoja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200");

        List<Loja> result = control.listarLojas();

        assertEquals(1, result.size());
        Loja loja = result.get(0);
        assertEquals("Pou pet store", loja.getNome());
        assertEquals("pou@animals.at", loja.getEmail());
        assertEquals("07480173403", loja.getCpfCnpj());
    }

    /**
     * Testa listar lojas em um arquivo vazio
     */
    @Test
    void testListarVazio() {
        LojaController control = new LojaController();
        List<Loja> result = control.listarLojas();

        assertTrue(result.isEmpty());
    }

    /**
     * Testa listar múltiplas lojas
     */
    @Test
    void testListar() {
        LojaController control = new LojaController();
        control.adicionarLoja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200");
        control.adicionarLoja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201");

        List<Loja> result = control.listarLojas();

        assertEquals(2, result.size());
        assertEquals("Pou pet store", result.get(0).getNome());
        assertEquals("Restaurante da Penha", result.get(1).getNome());

        // Verifica se os CPFs/CNPJs estão corretos
        assertEquals("07480173403", result.get(0).getCpfCnpj());
        assertEquals("07293166518", result.get(1).getCpfCnpj());
    }

    /**
     * Testa remover lojas em um arquivo vazio
     */
    @Test
    void testRemoveVazio() {
        LojaController control = new LojaController();
        boolean result = control.removerLoja("000.000.000-00");

        assertFalse(result);
        assertTrue(control.listarLojas().isEmpty());
    }

    /**
     * Testa remover loja inexistente
     */
    @Test
    void testRemoverInexistente() {
        LojaController control = new LojaController();
        control.adicionarLoja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200");
        control.adicionarLoja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201");

        boolean result = control.removerLoja("484.242.121-01");

        assertFalse(result);
        assertEquals(2, control.listarLojas().size());
    }

    /**
     * Testa remover loja
     */
    @Test
    void testRemover() {
        LojaController control = new LojaController();
        control.adicionarLoja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200");
        control.adicionarLoja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201");

        boolean result = control.removerLoja("074.801.734-03");

        assertTrue(result);
        List<Loja> lojas = control.listarLojas();
        assertEquals(1, lojas.size());
        assertEquals("Restaurante da Penha", lojas.get(0).getNome());
    }

    /**
     * Testa adicionar loja com CPF/CNPJ duplicado
     */
    @Test
    void testAdicionarLojaComCpfCnpjDuplicado() {
        LojaController control = new LojaController();
        control.adicionarLoja("Petshop", "petshop@email.com", "senha123", "12.345.678/0001-99", "Rua das Lojas, 123");

        // Tenta adicionar outra loja com mesmo CPF/CNPJ
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            control.adicionarLoja("Pet Center", "pet@email.com", "outrasenha", "12345678000199", "Av. Principal, 456");
        });

        assertEquals("Já existe uma loja cadastrada com este CPF/CNPJ", exception.getMessage());

        List<Loja> lojas = control.listarLojas();
        assertEquals(1, lojas.size()); // Deve manter apenas a primeira
        assertEquals("Petshop", lojas.get(0).getNome());
    }

    /**
     * Testa adicionar loja com CPF/CNPJ com documento inválido
     */
    @Test
    void testAdicionarLojaComCpfCnpjInvalido() {
        LojaController control = new LojaController();
        try {
            control.adicionarLoja("Pet Center", "pet@email.com", "outrasenha", "1", "Av. Principal, 456");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Testa buscar loja por CPF/CNPJ
     */
    @Test
    void testBuscarPorCpfCnpj() {
        LojaController control = new LojaController();
        control.adicionarLoja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200");
        control.adicionarLoja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201");

        Loja encontrada = control.buscarLojaPorCpfCnpj("072.931.665-18");
        assertNotNull(encontrada);
        assertEquals("Restaurante da Penha", encontrada.getNome());

        Loja naoEncontrada = control.buscarLojaPorCpfCnpj("000.000.000-00");
        assertNull(naoEncontrada);
    }

    /**
     * Testa buscar loja por CPF/CNPJ
     */
    @Test
    void testBuscarPorCpfCnpjInvalido() {
        LojaController control = new LojaController();
        control.adicionarLoja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200");
        control.adicionarLoja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201");

        try {
            control.buscarLojaPorCpfCnpj(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true); // ok
        }

        try {
            control.buscarLojaPorCpfCnpj("");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true); // ok
        }
    }

    @Test
    void fazerLogin() {
        LojaRepository repo = new LojaRepository();
        repo.salvar(new Loja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200"));
        repo.salvar(new Loja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201"));

        LojaController control = new LojaController();
        Loja result = control.fazerLogin("074.801.734-03", "0000");

        assertEquals("pou@animals.at", result.getEmail());
    }

    @Test
    void fazerLoginFail() {
        LojaRepository repo = new LojaRepository();
        repo.salvar(new Loja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200"));
        repo.salvar(new Loja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201"));

        LojaController control = new LojaController();
        try {
            control.fazerLogin("074.801.734-03", "abc"); // senha errada
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            control.fazerLogin("000.000.000-00", "0000"); // usuário errada
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void fazerLoginFailTooMuch() {
        LojaRepository repo = new LojaRepository();
        repo.salvar(new Loja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200"));
        repo.salvar(new Loja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201"));

        LojaController control = new LojaController();
        try {
            for(int i=0; i<10; i++) {
                try {
                    control.fazerLogin("074.801.734-03", "abc"); // senha errada
                } catch (IllegalArgumentException e) { }
            }
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void logout() {
        LojaRepository repo = new LojaRepository();
        repo.salvar(new Loja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200"));
        repo.salvar(new Loja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201"));

        LojaController control = new LojaController();
        control.fazerLogin("074.801.734-03", "0000");
        assertTrue(control.isLoggedIn());
        control.logout();

        assertFalse(control.isLoggedIn());
    }

    @Test
    void isLoggedIn() {
        LojaRepository repo = new LojaRepository();
        repo.salvar(new Loja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200"));
        repo.salvar(new Loja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201"));

        LojaController control = new LojaController();
        assertFalse(control.isLoggedIn());
        control.fazerLogin("074.801.734-03", "0000");

        assertTrue(control.isLoggedIn());
    }

    @Test
    void getLojaLogada() {
        LojaRepository repo = new LojaRepository();
        repo.salvar(new Loja("Pou pet store", "pou@animals.at", "0000", "074.801.734-03", "Rua José da Silva Jr., No 200"));
        repo.salvar(new Loja("Restaurante da Penha", "penha123@a-mail.com", "13011990", "072.931.665-18", "Rua José da Silva Jr., No 201"));
        LojaController control = new LojaController();
        control.fazerLogin("074.801.734-03", "0000");
        assertTrue(control.isLoggedIn());

        Loja result = control.getLojaLogada();

        assertEquals("pou@animals.at", result.getEmail());
    }
}
