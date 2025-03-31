package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.repository.CompradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de compradores.
 */
class CompradorControllerTest {

    private static final String FILE_NAME = "./data/compradores.json";
    private CompradorRepository repo;

    @BeforeEach
    void setup() {
        // deleta o repositório de lojas
        File f = new File(FILE_NAME);
        f.delete();

        repo = new CompradorRepository(FILE_NAME);
    }


    /**
     * Testa persistir instância de comprador
     */
    @Test
    void testSalvar() {
        CompradorController control = new CompradorController(repo);
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Rua Margaridas, Número 123");

        List<Comprador> result = control.listarCompradores();

        assertEquals(1, result.size());
        assertEquals("Joana Ferreira", result.get(0).getNome());
    }

    /**
     * Testa listar compradores a partir de um repositório vazio
     */
    @Test
    void testListarVazio() {
        CompradorController control = new CompradorController(repo);
        List<Comprador> result = control.listarCompradores();

        assertEquals(0, result.size());
    }

    /**
     * Testa listar múltiplos compradores
     */
    @Test
    void testListar() {
        CompradorController control = new CompradorController(repo);
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05", "Rua Marimbondo Caboclo, 3001");

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
        CompradorController control = new CompradorController(repo);
        boolean result = control.removerComprador("000.000.000-00");

        assertFalse(result);
    }

    /**
     * Testa remover comprador inexistente
     */
    @Test
    void testRemoverInexistente() {
        CompradorController control = new CompradorController(repo);
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05", "Rua Marimbondo Caboclo, 3001");

        boolean result = control.removerComprador("484.242.121-01");

        assertFalse(result);
        assertEquals(2, control.listarCompradores().size());
    }

    /**
     * Testa remover comprador
     */
    @Test
    void testRemover() {
        CompradorController control = new CompradorController(repo);
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05", "Rua Marimbondo Caboclo, 3001");

        boolean result = control.removerComprador("074.821.754-03");

        assertTrue(result);
        assertEquals(1, control.listarCompradores().size());
    }
}
