package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do repositório de loja.
 */
class LojaRepositoryTest {

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
     * Testa salvar loja
     */
    @Test
    void testSalvar() {
        Loja store = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        LojaRepository repo = new LojaRepository();
        repo.salvar(store);

        List<Loja> result = repo.listar();

        assertEquals(1, result.size());
        assertEquals("Brookdale Equipamentos", result.get(0).getNome());
    }

    /**
     * Testa listar lojas em um arquivo vazio
     */
    @Test
    void testListarVazio() {
        LojaRepository repo = new LojaRepository();
        List<Loja> result = repo.listar();

        assertEquals(0, result.size());
    }

    /**
     * Testa listar múltiplas lojas
     */
    @Test
    void testListar() {
        Loja store1 = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        Loja store2 = new Loja("Audio&Video", "a-v@contact.me", "010101", "013.345.678-00", "Rua dos Mafagafos, 2");

        LojaRepository repo = new LojaRepository();
        repo.salvar(store1);
        repo.salvar(store2);

        List<Loja> result = repo.listar();

        assertEquals(2, result.size());
        assertEquals("Brookdale Equipamentos", result.get(0).getNome());
        assertEquals("Audio&Video", result.get(1).getNome());
    }

    /**
     * Testa remover lojas em um arquivo vazio
     */
    @Test
    void testRemoveVazio() {
        LojaRepository repo = new LojaRepository();
        boolean result = repo.remover("0");

        assertFalse(result);
    }

    /**
     * Testa remover loja inexistente
     */
    @Test
    void testRemoverInexistente() {
        Loja store1 = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        Loja store2 = new Loja("Audio&Video", "a-v@contact.me", "010101", "013.345.678-00", "Rua dos Mafagafos, 2");

        LojaRepository repo = new LojaRepository();
        repo.salvar(store1);
        repo.salvar(store2);

        boolean result = repo.remover("313.131.313-31");

        assertFalse(result);
        assertEquals(2, repo.listar().size());
    }

    /**
     * Testa remover loja
     */
    @Test
    void testRemover() {
        Loja store1 = new Loja("Brookdale Equipamentos", "brkdl@mail.net", "12345", "012.345.678-90", "Rua dos Mafagafos, 1");
        Loja store2 = new Loja("Audio&Video", "a-v@contact.me", "010101", "013.345.678-00", "Rua dos Mafagafos, 2");

        LojaRepository repo = new LojaRepository();
        repo.salvar(store1);
        repo.salvar(store2);

        boolean result = repo.remover("013.345.678-00");

        assertTrue(result);
        assertEquals(1, repo.listar().size());
    }
}
