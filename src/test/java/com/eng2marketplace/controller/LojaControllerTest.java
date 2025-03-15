package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.LojaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de lojas.
 */
class LojaControllerTest {

    @BeforeEach
    void setup() {
        // deleta o repositório de lojas
        File f = new File("lojas.txt");
        if(!f.exists())
            return;
        if(!f.delete())
            throw new RuntimeException();
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
        assertEquals("Pou pet store", result.get(0).getNome());
    }

    /**
     * Testa listar lojas em um arquivo vazio
     */
    @Test
    void testListarVazio() {
        LojaController control = new LojaController();
        List<Loja> result = control.listarLojas();

        assertEquals(0, result.size());
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
    }

    /**
     * Testa remover lojas em um arquivo vazio
     */
    @Test
    void testRemoveVazio() {
        LojaController control = new LojaController();
        boolean result = control.removerLoja("000.000.000-00");

        assertFalse(result);
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
        assertEquals(1, control.listarLojas().size());
    }
}
