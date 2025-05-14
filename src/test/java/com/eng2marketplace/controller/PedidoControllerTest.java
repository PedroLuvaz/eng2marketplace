package com.eng2marketplace.controller;

import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoControllerTest {

    @BeforeEach
    void setUp() {
        PedidoRepository pr = new PedidoRepository();
        pr.limpar();
    }

    @Test
    void criarPedido() {
        PedidoController control = new PedidoController();
        HashMap<String, Integer> carrinho = new HashMap<>();
        carrinho.put("0000-0", 2);

        control.criarPedido("012.021.120.102-20", carrinho, 999);

        PedidoRepository repo = new PedidoRepository();
        Optional<Pedido> test = repo.buscar(
            pd -> pd.getCompradorCpf().equals("012.021.120.102-20") &&
                pd.getValorTotal() == 999
        );

        assertEquals(1, repo.listar().size());
        assertTrue(test.isPresent());
    }

    @Test
    void listarPedidosPorComprador() {
        PedidoRepository repo = new PedidoRepository();
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999));
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999));

        PedidoController control = new PedidoController();
        List<Pedido> pedidos = control.listarPedidosPorComprador("012.021.120.102-20");

        assertEquals(2, pedidos.size());
    }

    @Test
    void listarPedidosPorCompradorNaoListado() {
        PedidoRepository repo = new PedidoRepository();
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999));
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999));

        PedidoController control = new PedidoController();
        List<Pedido> pedidos = control.listarPedidosPorComprador("036.063.360.306-60");

        assertEquals(0, pedidos.size());
    }

    @Test
    void atualizarStatusPedido() {
        PedidoRepository repo = new PedidoRepository();
        Pedido p = new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999);
        repo.salvar(p);
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999));

        PedidoController control = new PedidoController();
        boolean ok = control.atualizarStatusPedido(p.getId(), "CANCELADO");

        assertTrue(ok);
        Optional<Pedido> test = repo.buscar(x->x.getId().equals(p.getId()));
        assertTrue(test.isPresent());
        assertEquals("CANCELADO", test.get().getStatus());
    }

    @Test
    void atualizarStatusPedidoInexistente() {
        PedidoRepository repo = new PedidoRepository();
        Pedido p = new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999);
        repo.salvar(p);
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999));

        PedidoController control = new PedidoController();
        boolean ok = control.atualizarStatusPedido("a", "CANCELADO");

        assertFalse(ok);
    }
}
