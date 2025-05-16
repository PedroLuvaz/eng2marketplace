package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.repository.LojaRepository;
import com.eng2marketplace.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de Produtos.
 */
class ProdutoControllerTest {

    private static final Loja LOJA = new Loja("Pets&Pets&Pets", "abc@xyz.cc", "12345678", "12.345.678/0001-90", "Rua Nova, No 1");

    @BeforeEach
    void setup() {
        LojaRepository lr = new LojaRepository();
        lr.limpar();
        lr.salvar(LOJA);

        ProdutoRepository pr = new ProdutoRepository(lr);
        pr.limpar();
    }

    /**
     * Testa persistir instância de produto
     */
    @Test
    void testSalvar() {
        ProdutoController control = new ProdutoController();
        control.adicionarProduto("Ração", 1000.00, "0000", 9191, "Nutrifods", "Ração universal", LOJA);

        List<Produto> result = control.listarProdutos();

        assertEquals(1, result.size());
        assertEquals("Ração", result.get(0).getNome());
    }

    /**
     * Testa listar produtos em um arquivo vazio
     */
    @Test
    void testListarVazio() {
        ProdutoController control = new ProdutoController();
        List<Produto> result = control.listarProdutos();

        assertEquals(0, result.size());
    }

    /**
     * Testa listar múltiplos Produtos
     */
    @Test
    void testListar() {
        ProdutoController control = new ProdutoController();
        control.adicionarProduto("Ração", 1000.00, "0000", 9191, "Nutrifods", "Ração universal", LOJA);
        control.adicionarProduto("Poção", 999.99, "0001", 1919, "Nutrifods", "Poção de vida, remédio universal", LOJA);

        List<Produto> result = control.listarProdutos();

        assertEquals(2, result.size());
        assertEquals("Ração", result.get(0).getNome());
        assertEquals("Poção", result.get(1).getNome());
    }

    /**
     * Testa remover Produtos em um arquivo vazio
     */
    @Test
    void testRemoveVazio() {
        ProdutoController control = new ProdutoController();
        boolean result = control.removerProduto("Carne de unicórnio");

        assertFalse(result);
    }

    /**
     * Testa remover Produto inexistente
     */
    @Test
    void testRemoverInexistente() {
        ProdutoController control = new ProdutoController();
        control.adicionarProduto("Ração", 1000.00, "0000", 9191, "Nutrifods", "Ração universal", LOJA);
        control.adicionarProduto("Poção", 999.99, "0001", 1919, "Nutrifods", "Poção de vida, remédio universal", LOJA);

        boolean result = control.removerProduto("Absurdo em pó");

        assertFalse(result);
        assertEquals(2, control.listarProdutos().size());
    }

    /**
     * Testa remover Produto
     */
    @Test
    void testRemover() {
        ProdutoController control = new ProdutoController();
        control.adicionarProduto("Ração", 1000.00, "0000", 9191, "Nutrifods", "Ração universal", LOJA);
        control.adicionarProduto("Poção", 999.99, "0001", 1919, "Nutrifods", "Poção de vida, remédio universal", LOJA);

        boolean result = control.removerProduto("Ração");

        assertTrue(result);
        assertEquals(1, control.listarProdutos().size());
    }

    /**
     * Testa listar Produtos por loja
     */
    @Test
    void testListarPorLoja() {
        Loja pets = new Loja("Pets&Pets&Pets", "abc@xyz.cc", "12345678", "12.345.678/0001-90", "Rua Nova, No 1");
        Loja sports = new Loja("S(a)ports", "s@por.ts", "0000-0000", "93.333.999/0001-39", "Rua Nova, No 2");
        LojaRepository lr = new LojaRepository();
        lr.salvar(sports);
        lr.salvar(pets);

        ProdutoController control = new ProdutoController();
        control.adicionarProduto("Ração", 1000.00, "0000", 9191, "Nutrifods", "Ração universal", pets);
        control.adicionarProduto("Poção", 999.99, "0001", 1919, "Nutrifods", "Poção de vida, remédio universal", pets);
        control.adicionarProduto("Chapéu", 1000.00, "0000", 9191, "SuperM", "Chapéu estilo vaquejada para futebol", sports);
        control.adicionarProduto("Raquete", 999.99, "0001", 1919, "E-Quipments", "Raquete 3 em 1: ping-pong, tênis e mata-mosquito", sports);

        List<Produto> result = control.listarProdutosPorLoja("93.333.999/0001-39");

        assertEquals(2, result.size());
        assertEquals("Chapéu", result.get(0).getNome());
        assertEquals("Raquete", result.get(1).getNome());
    }

    /**
     * Testa listar Produtos de uma loja inexistente
     */
    @Test
    void testListarPorLojaInexistente() {
        Loja pets = new Loja("Pets&Pets&Pets", "abc@xyz.cc", "12345678", "12.345.678/0001-90", "Rua Nova, No 1");
        Loja sports = new Loja("S(a)ports", "s@por.ts", "0000-0000", "93.333.999/0001-39", "Rua Nova, No 2");
        LojaRepository lr = new LojaRepository();
        lr.salvar(sports);
        lr.salvar(pets);

        ProdutoController control = new ProdutoController();
        control.adicionarProduto("Ração", 1000.00, "0000", 9191, "Nutrifods", "Ração universal", pets);
        control.adicionarProduto("Poção", 999.99, "0001", 1919, "Nutrifods", "Poção de vida, remédio universal", pets);
        control.adicionarProduto("Chapéu", 1000.00, "0000", 9191, "SuperM", "Chapéu estilo vaquejada para futebol", sports);
        control.adicionarProduto("Raquete", 999.99, "0001", 1919, "E-Quipments", "Raquete 3 em 1: ping-pong, tênis e mata-mosquito", sports);

        List<Produto> result = control.listarProdutosPorLoja("071.412.665-01");

        assertEquals(0, result.size());
    }

    @Test
    void testBuscarProdutoPorId() {
        Loja pets = new Loja("Pets&Pets&Pets", "abc@xyz.cc", "12345678", "12.345.678/0001-90", "Rua Nova, No 1");
        Loja sports = new Loja("S(a)ports", "s@por.ts", "0000-0000", "93.333.999/0001-39", "Rua Nova, No 2");
        LojaRepository lr = new LojaRepository();
        lr.salvar(sports);
        lr.salvar(pets);
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto p = new Produto("Colher", 1.50, "00001", 300, "AluminoKitchen", "Colher de aço inox", sports);
        repo.salvar(p);

        ProdutoController control = new ProdutoController();
        Produto result = control.buscarProdutoPorId(p.getId());

        assertNotNull(result);
        assertEquals(p.getId(), result.getId());
    }

    @Test
    void testRemoverProdutoPorId() {
        Loja pets = new Loja("Pets&Pets&Pets", "abc@xyz.cc", "12345678", "12.345.678/0001-90", "Rua Nova, No 1");
        Loja sports = new Loja("S(a)ports", "s@por.ts", "0000-0000", "93.333.999/0001-39", "Rua Nova, No 2");
        LojaRepository lr = new LojaRepository();
        lr.salvar(sports);
        lr.salvar(pets);
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto p = new Produto("Copo Shake", 15.00, "00001", 300, "PlastiKitchen", "Copo 800ml", sports);
        repo.salvar(p);

        ProdutoController control = new ProdutoController();
        boolean result = control.removerProdutoPorId(p.getId());

        assertTrue(result);
        Optional<Produto> test = repo.buscarPorId(p.getId());
        assertTrue(test.isEmpty());
    }
}
