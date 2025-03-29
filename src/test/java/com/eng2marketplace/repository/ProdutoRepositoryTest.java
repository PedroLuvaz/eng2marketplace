package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do repositório de loja.
 */
class ProdutoRepositoryTest {
    private static final Loja LOJA = new Loja("Baratinhos", "mario@mail.net", "meu", "33.333.333/0001-33", "Rua N, S/N");

    @BeforeEach
    void setup() {
        // deleta o repositório de lojas
        File f = new File("./data/produtos.txt");
        if(!f.exists())
            return;
        if(!f.delete())
            throw new RuntimeException();

        // deleta o repositório de lojas
        f = new File("./data/lojas.txt");
        if(!f.exists())
            return;
        if(!f.delete())
            throw new RuntimeException();

        // salva a primeira
        new LojaRepository().salvar(LOJA);
    }

    /**
     * Testa salvar produto
     */
    @Test
    void testSalvar() {
        Produto product = new Produto("Netbook", 750.0, "909123", 15, "Asus", "Netbook atom com 1GB de memória", LOJA);
        ProdutoRepository repo = new ProdutoRepository(new LojaRepository());
        repo.salvar(product);

        List<Produto> result = repo.listar();

        assertEquals(1, result.size());
        assertEquals("Netbook", result.get(0).getNome());
    }

    /**
     * Testa listar produtos em um arquivo vazio
     */
    @Test
    void testListarVazio() {
        ProdutoRepository repo = new ProdutoRepository(new LojaRepository());
        List<Produto> result = repo.listar();

        assertEquals(0, result.size());
    }

    /**
     * Testa listar múltiplos produtos
     */
    @Test
    void testListar() {
        Produto product = new Produto("Netbook", 750.0, "909123", 15, "Asus", "Netbook atom com 1GB de memória", LOJA);
        Produto product2 = new Produto("Celular", 350.0, "909123", 15, "Nokia", "Celular flip modelo Nokia 6101", LOJA);

        ProdutoRepository repo = new ProdutoRepository(new LojaRepository());
        repo.salvar(product);
        repo.salvar(product2);

        List<Produto> result = repo.listar();

        assertEquals(2, result.size());
        assertEquals("Netbook", result.get(0).getNome());
        assertEquals("Celular", result.get(1).getNome());
    }

    /**
     * Testa remover produtos em um arquivo vazio
     */
    @Test
    void testRemoveVazio() {
        ProdutoRepository repo = new ProdutoRepository(new LojaRepository());
        boolean result = repo.remover("0");

        assertFalse(result);
    }

    /**
     * Testa remover produto inexistente
     */
    @Test
    void testRemoverInexistente() {
        Produto product = new Produto("Netbook", 750.0, "909123", 15, "Asus", "Netbook atom com 1GB de memória", LOJA);
        Produto product2 = new Produto("Celular", 350.0, "909123", 15, "Nokia", "Celular flip modelo Nokia 6101", LOJA);

        ProdutoRepository repo = new ProdutoRepository(new LojaRepository());
        repo.salvar(product);
        repo.salvar(product2);

        boolean result = repo.remover("Leitor de floppy disk nvm-e");

        assertFalse(result);
        assertEquals(2, repo.listar().size());
    }

    /**
     * Testa remover produto
     */
    @Test
    void testRemover() {
        Produto product = new Produto("Netbook", 750.0, "909123", 15, "Asus", "Netbook atom com 1GB de memória", LOJA);
        Produto product2 = new Produto("Celular", 350.0, "909123", 15, "Nokia", "Celular flip modelo Nokia 6101", LOJA);

        ProdutoRepository repo = new ProdutoRepository(new LojaRepository());
        repo.salvar(product);
        repo.salvar(product2);

        boolean result = repo.remover(product.getNome());

        assertTrue(result);
        assertEquals(1, repo.listar().size());
    }
}
