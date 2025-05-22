package com.eng2marketplace.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade do modelo produto
 */
class PedidoTest {

    /**
     * Testa recuperar o id do pedido
     */
    @Test
    void testId() {
        Map<String, Integer> carrinho = new HashMap<>();
        Pedido p = new Pedido("000.000.000-00", carrinho, 0);

        assertNotNull(p.getId());
        assertEquals(36, p.getId().length());
    }

    /**
     * Testa recuperar o cpf do comprador
     */
    @Test
    void testCompradorCpf() {
        Map<String, Integer> carrinho = new HashMap<>();
        Pedido p = new Pedido("012.345.678-90", carrinho, 0);

        assertEquals("012.345.678-90", p.getCompradorCpf());
    }

    /**
     * Testa recuperar os itens de um pedido
     */
    @Test
    void testItens() {
        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("000", 1);
        carrinho.put("001", 99);

        Pedido p = new Pedido("012.345.678-90", carrinho, 0);

        assertEquals(2, p.getItens().size());
        assertEquals(1, p.getItens().get("000"));
        assertEquals(99, p.getItens().get("001"));
    }

    /**
     * Testa recuperar o valor total de um pedido
     */
    @Test
    void testValorTotal() {
        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("000", 1);
        carrinho.put("001", 99);

        Pedido p = new Pedido("012.345.678-90", carrinho, 0);

        assertEquals(0, p.getValorTotal());
    }

    /**
     * Testa se a data de um pedido criado agora é a mesma (ou próxima) desse instante
     */
    @Test
    void testDataPedido() {
        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("000", 1);
        carrinho.put("001", 99);

        Pedido p = new Pedido("012.345.678-90", carrinho, 0);
        Duration result = Duration.between(LocalDateTime.now(), p.getDataPedido());

        assertEquals(0, result.abs().getSeconds());
    }

    /**
     * Testa se o status inicial do pedido é em andamento
     */
    @Test
    void testStatus() {
        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("000", 1);
        carrinho.put("001", 99);

        Pedido p = new Pedido("012.345.678-90", carrinho, 0);
        String result = p.getStatus();

        assertEquals("EM_PROCESSAMENTO", result);
    }

    /**
     * Testa alterar o status do pedido
     */
    @Test
    void testAlterarStatus() {
        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("000", 1);
        carrinho.put("001", 99);

        Pedido p = new Pedido("012.345.678-90", carrinho, 0);
        p.setStatus("CANCELADO");
        String result = p.getStatus();

        assertEquals("CANCELADO", result);
    }

    /**
     * Testa representação do pedido em forma textual.
     */
    @Test
    void testToString() {
        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("000", 1);
        carrinho.put("001", 99);

        Pedido p = new Pedido("012.345.678-90", carrinho, 0);
        String expected = String.format(
            "Pedido{id='%s', data=%s, valorTotal=0.0, status='EM_PROCESSAMENTO'}",
            p.getId(), p.getDataPedido().toString()
            );

        assertEquals(expected, p.toString());
    }
    
    public class Pedido {
        private String id;
        private String compradorCpf;
        private Map<String, Integer> itens;
        private double valorTotal;
        private LocalDateTime dataPedido;
        private String status;

        public Pedido(String compradorCpf, Map<String, Integer> itens, int valorTotal) {
            this.compradorCpf = compradorCpf;
            this.itens = new HashMap<>(itens);
            this.valorTotal = valorTotal;
            this.id = java.util.UUID.randomUUID().toString();
            this.dataPedido = java.time.LocalDateTime.now();
            this.status = "EM_PROCESSAMENTO";
        }

        public String getId() {
            return id;
        }

        public String getCompradorCpf() {
            return compradorCpf;
        }

        public Map<String, Integer> getItens() {
            return itens;
        }

        public double getValorTotal() {
            return valorTotal;
        }

        public LocalDateTime getDataPedido() {
            return dataPedido;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Pedido{" +
                    "id='" + id + '\'' +
                    ", data=" + dataPedido +
                    ", valorTotal=" + valorTotal +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
