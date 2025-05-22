package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
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
        // Limpa o repositório antes de cada teste
        new PedidoRepository().limpar();
    }

    @Test
    void criarPedido() {
        PedidoRepository repo = new PedidoRepository();
        repo.limpar();

        PedidoController control = new PedidoController();
        HashMap<String, Integer> carrinho = new HashMap<>();
        carrinho.put("0000-0", 2);

        // Certifique-se de que Loja tem construtor padrão
        control.criarPedido("012.021.120.102-20", carrinho, 999.0, new Loja());

        Optional<Pedido> test = repo.buscar(
            pd -> pd.getCompradorCpf().equals("012.021.120.102-20") &&
                Math.abs(pd.getValorTotal() - 999.0) < 0.001
        );

        assertEquals(1, repo.listar().size());
        assertTrue(test.isPresent());
    }

    @Test
    void listarPedidosPorComprador() {
        PedidoRepository repo = new PedidoRepository();
        repo.limpar();
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999.0));
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999.0));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999.0));

        PedidoController control = new PedidoController();
        List<Pedido> pedidos = control.listarPedidosPorComprador("012.021.120.102-20");

        assertEquals(2, pedidos.size());
    }

    @Test
    void listarPedidosPorCompradorNaoListado() {
        PedidoRepository repo = new PedidoRepository();
        repo.limpar();
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999.0));
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999.0));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999.0));

        PedidoController control = new PedidoController();
        List<Pedido> pedidos = control.listarPedidosPorComprador("036.063.360.306-60");

        assertEquals(0, pedidos.size());
    }

    @Test
    void atualizarStatusPedido() {
        PedidoRepository repo = new PedidoRepository();
        repo.limpar();
        Pedido p = new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999.0);
        repo.salvar(p);
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999.0));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999.0));

        PedidoController control = new PedidoController();
        boolean ok = control.atualizarStatusPedido(p.getId(), "CANCELADO");

        assertTrue(ok);
        Optional<Pedido> test = repo.buscar(x -> x.getId().equals(p.getId()));
        assertTrue(test.isPresent());
        assertEquals("CANCELADO", test.get().getStatus());
    }

    @Test
    void atualizarStatusPedidoInexistente() {
        PedidoRepository repo = new PedidoRepository();
        repo.limpar();
        Pedido p = new Pedido("012.021.120.102-20", Map.of("0000", 1, "0001", 2), 999.0);
        repo.salvar(p);
        repo.salvar(new Pedido("012.021.120.102-20", Map.of("0010", 40, "0100", 11), 999.0));
        repo.salvar(new Pedido("024.042.240.204-40", Map.of("1000", 1, "0001", 99), 999.0));

        PedidoController control = new PedidoController();
        boolean ok = control.atualizarStatusPedido("a", "CANCELADO");

        assertFalse(ok);
    }
}
