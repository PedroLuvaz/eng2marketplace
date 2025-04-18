package com.eng2marketplace.controller;

import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.repository.PedidoRepository;

import java.util.List;
import java.util.Map;

public class PedidoController {
    private final PedidoRepository pedidoRepository;

    public PedidoController() {
        this.pedidoRepository = new PedidoRepository();
    }

    public Pedido criarPedido(String compradorCpf, Map<String, Integer> carrinho, double valorTotal) {
        Pedido pedido = new Pedido(compradorCpf, carrinho, valorTotal);
        pedidoRepository.salvar(pedido);
        return pedido;
    }

    public List<Pedido> listarPedidosPorComprador(String compradorCpf) {
        return pedidoRepository.listarPorComprador(compradorCpf);
    }

    public boolean atualizarStatusPedido(String pedidoId, String novoStatus) {
        return pedidoRepository.atualizarStatus(pedidoId, novoStatus);
    }
}