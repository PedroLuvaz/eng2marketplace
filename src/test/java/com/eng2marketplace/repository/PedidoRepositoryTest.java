package com.eng2marketplace.repository;

import com.eng2marketplace.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eng2marketplace.util.RepoCleaner.cleanRepos;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade de repositório de pedidos
 */
class PedidoRepositoryTest {

    private Map<String, Integer> carrinho;

    @BeforeEach
    void setUp() {
        PedidoRepository pr = new PedidoRepository();
        pr.limpar();
        carrinho = new HashMap<>();
    }

    /**
     * Testa salvar uma instância de pedido
     */
    @Test
    void testSalvar() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);
        Pedido p = new Pedido("012.345.678-90", carrinho, 999, null);
        pr.salvar(p);

        assertEquals(1, pr.listar().size());
        assertEquals(p.getId(), pr.listar().getFirst().getId());
    }

    /**
     * Testa remover um pedido do carrinho
     */
    @Test
    void testRemover() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);
        Pedido p = new Pedido("012.345.678-90", carrinho, 999, null);
        pr.salvar(p);

        boolean result = pr.remover(p.getId());

        assertTrue(result);
        assertTrue(pr.listar().isEmpty());
    }

    /**
     * Testa remover um pedido do carrinho usando id inválido
     */
    @Test
    void testRemoverInvalido() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);
        Pedido p = new Pedido("012.345.678-90", carrinho, 999, null);
        pr.salvar(p);

        boolean result = pr.remover("a");

        assertFalse(result);
        assertFalse(pr.listar().isEmpty());
    }

    /**
     * Testa listar todos pedidos
     */
    @Test
    void testListar() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);
        pr.salvar(new Pedido("012.345.678-90", carrinho, 999, null));
        pr.salvar(new Pedido("012.345.678-90", carrinho, 123, null));
        pr.salvar(new Pedido("012.345.678-90", carrinho, 456, null));

        List<Pedido> pedidos = pr.listar();

        assertEquals(3, pedidos.size());
    }

    /**
     * Testa listar todos pedidos sem ter inserido nenhum
     */
    @Test
    void testListarVazio() {
        PedidoRepository pr = new PedidoRepository();

        List<Pedido> pedidos = pr.listar();

        assertTrue(pedidos.isEmpty());
    }

    /**
     * Testa obter todos os pedidos de um comprador
     */
    @Test
    void testListarPorComprador() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);
        pr.salvar(new Pedido("098.765.432-10", carrinho, 999, null));
        pr.salvar(new Pedido("098.765.432-10", carrinho, 1, null));
        pr.salvar(new Pedido("555.555.555-55", carrinho, 123, null));
        pr.salvar(new Pedido("012.345.678-90", carrinho, 456, null));

        List<Pedido> pedidos = pr.listarPorComprador("098.765.432-10");

        assertEquals(2, pedidos.size());
    }

    /**
     * Testa listar todos os pedidos de um comprador, informando um cpf inválido
     */
    @Test
    void testListarPorCompradorInvalido() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);
        pr.salvar(new Pedido("098.765.432-10", carrinho, 999, null));
        pr.salvar(new Pedido("098.765.432-10", carrinho, 1, null));
        pr.salvar(new Pedido("555.555.555-55", carrinho, 123, null));
        pr.salvar(new Pedido("012.345.678-90", carrinho, 456, null));

        List<Pedido> pedidos = pr.listarPorComprador("313.131.313-31");

        assertTrue(pedidos.isEmpty());
    }

    /**
     * Testa atualizar o status de um produto
     */
    @Test
    void testAtualizarStatus() {
        PedidoRepository pr = new PedidoRepository();
        carrinho.put("00000", 2);

        Pedido p = new Pedido("192.168.000-01", carrinho, 456, null);
        pr.salvar(p);
        pr.salvar(new Pedido("090.909.090-90", carrinho, 999, null));
        pr.salvar(new Pedido("300.000.000-03", carrinho, 1, null));
        pr.salvar(new Pedido("201.420.152-01", carrinho, 123, null));

        pr.atualizarStatus(p.getId(), "CANCELADO");

        assertEquals("CANCELADO", pr.listarPorComprador("192.168.000-01").getFirst().getStatus());
    }
}
