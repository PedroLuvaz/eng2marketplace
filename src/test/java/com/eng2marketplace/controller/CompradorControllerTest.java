package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de compradores.
 */
class CompradorControllerTest {

    @BeforeEach
    void setup() {
        // deleta o repositório de compradores
        File f = new File("compradores.txt");
        if(!f.exists())
            return;
        if(!f.delete())
            throw new RuntimeException();
    }

    /**
     * Testa persistir instância de comprador
     */
    @Test
    void testSalvar() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03");

        List<Comprador> result = control.listarCompradores();

        assertEquals(1, result.size());
        assertEquals("Joana Ferreira", result.get(0).getNome());
    }

    /**
     * Testa listar compradores a partir de um repositório vazio
     */
    @Test
    void testListarVazio() {
        CompradorController control = new CompradorController();
        List<Comprador> result = control.listarCompradores();

        assertEquals(0, result.size());
    }

    /**
     * Testa listar múltiplos compradores
     */
    @Test
    void testListar() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05");

        List<Comprador> result = control.listarCompradores();

        assertEquals(2, result.size());
        assertEquals("Joana Ferreira", result.get(0).getNome());
        assertEquals("Ana Pontes", result.get(1).getNome());
    }

    /**
     * Testa remover compradores de um repositório vazio
     */
    @Test
    void testRemoveVazio() {
        CompradorController control = new CompradorController();
        boolean result = control.removerComprador("000.000.000-00");

        assertFalse(result);
    }

    /**
     * Testa remover comprador inexistente
     */
    @Test
    void testRemoverInexistente() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05");

        boolean result = control.removerComprador("484.242.121-01");

        assertFalse(result);
        assertEquals(2, control.listarCompradores().size());
    }

    /**
     * Testa remover comprador
     */
    @Test
    void testRemover() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05");

        boolean result = control.removerComprador("074.821.754-03");

        assertTrue(result);
        assertEquals(1, control.listarCompradores().size());
    }
}
