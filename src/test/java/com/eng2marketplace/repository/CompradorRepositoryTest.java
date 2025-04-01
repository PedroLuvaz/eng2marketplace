package com.eng2marketplace.repository;

import com.eng2marketplace.model.Comprador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do repositório de compradores.
 */
class CompradorRepositoryTest {

    @BeforeEach
    void setup() {
        // deleta o repositório de compradores
        File f = new File("src/main/data/compradores.json");
        if(!f.exists())
            return;
        if(!f.delete())
            throw new RuntimeException();
    }

    /**
     * Testa salvar instância de comprador
     */
    @Test
    void testSalvar() {
        Comprador buyer = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(buyer);

        List<Comprador> result = repo.listar();

        assertEquals(1, result.size());
        assertEquals("João", result.get(0).getNome());
    }

    /**
     * Testa listar compradores a partir um arquivo vazio
     */
    @Test
    void testListarVazio() {
        CompradorRepository repo = new CompradorRepository();
        List<Comprador> result = repo.listar();

        assertEquals(0, result.size());
    }

    /**
     * Testa listar múltiplas lojas
     */
    @Test
    void testListar() {
        Comprador buyer1 = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        Comprador buyer2 = new Comprador("Maria", "maria@correio.br", "54321", "999.888.777-66", "Loteamento Portal, 987");

        CompradorRepository repo = new CompradorRepository();
        repo.salvar(buyer1);
        repo.salvar(buyer2);

        List<Comprador> result = repo.listar();

        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getNome());
        assertEquals("Maria", result.get(1).getNome());
    }

    /**
     * Testa remover compradores a partir de um repositório vazio
     */
    @Test
    void testRemoveVazio() {
        CompradorRepository repo = new CompradorRepository();
        boolean result = repo.remover("0");

        assertFalse(result);
    }

    /**
     * Testa remover comprador inexistente
     */
    @Test
    void testRemoverInexistente() {
        Comprador buyer1 = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        Comprador buyer2 = new Comprador("Maria", "maria@correio.br", "54321", "999.888.777-66", "Loteamento Portal, 987");

        CompradorRepository repo = new CompradorRepository();
        repo.salvar(buyer1);
        repo.salvar(buyer2);

        boolean result = repo.remover("313.131.313-31");

        assertFalse(result);
        assertEquals(2, repo.listar().size());
    }

    /**
     * Testa remover comprador
     */
    @Test
    void testRemover() {
        Comprador buyer1 = new Comprador("João", "jao@contact.me", "12345", "000.111.222-33", "Rua Teló, S/N");
        Comprador buyer2 = new Comprador("Maria", "maria@correio.br", "54321", "999.888.777-66", "Loteamento Portal, 987");

        CompradorRepository repo = new CompradorRepository();
        repo.salvar(buyer1);
        repo.salvar(buyer2);

        boolean result = repo.remover("000.111.222-33");

        assertTrue(result);
        assertEquals(1, repo.listar().size());
    }
}
