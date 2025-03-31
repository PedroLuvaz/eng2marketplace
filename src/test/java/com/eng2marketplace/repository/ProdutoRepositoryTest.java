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
    private static final String LOJAS_DB = "./data/lojas.json";
    private static final String PRODUTOS_DB = "./data/produtos.json";
    private static final Loja LOJA = new Loja("Baratinhos", "mario@mail.net", "meu", "33.333.333/0001-33", "Rua N, S/N");

    @BeforeEach
    void setup() {
        // deleta o repositório de produtos
        File f = new File(PRODUTOS_DB);
        f.delete();

        // deleta o repositório de lojas
        f = new File(LOJAS_DB);
        f.delete();

        // salva a primeira loja
        new LojaRepository(LOJAS_DB).salvar(LOJA);
    }

    /**
     * Testa salvar produto
     */
    @Test
    void testSalvar() {
        Produto product = new Produto("Netbook", 750.0, "909123", 15, "Asus", "Netbook atom com 1GB de memória", LOJA);
        ProdutoRepository repo = new ProdutoRepository(PRODUTOS_DB, new LojaRepository(LOJAS_DB));
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
        ProdutoRepository repo = new ProdutoRepository(PRODUTOS_DB, new LojaRepository(LOJAS_DB));
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

        ProdutoRepository repo = new ProdutoRepository(PRODUTOS_DB, new LojaRepository(LOJAS_DB));
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
        ProdutoRepository repo = new ProdutoRepository(PRODUTOS_DB, new LojaRepository(LOJAS_DB));
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

        ProdutoRepository repo = new ProdutoRepository(PRODUTOS_DB, new LojaRepository(LOJAS_DB));
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

        ProdutoRepository repo = new ProdutoRepository(PRODUTOS_DB, new LojaRepository(LOJAS_DB));
        repo.salvar(product);
        repo.salvar(product2);

        boolean result = repo.remover(product.getNome());

        assertTrue(result);
        assertEquals(1, repo.listar().size());
    }
}
