package com.eng2marketplace.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pedido {
    private String id;
    private String compradorCpf;
    private Map<String, Integer> itens; // produtoId -> quantidade
    private double valorTotal;
    private LocalDateTime dataPedido;
    private String status; // "EM_PROCESSAMENTO", "ENVIADO", "ENTREGUE", "CANCELADO"
    private Loja loja;

    public Pedido(String compradorCpf, Map<String, Integer> carrinho, double valorTotal, Loja loja) {
        this.id = UUID.randomUUID().toString();
        this.compradorCpf = compradorCpf;
        this.itens = new HashMap<>(carrinho);
        this.valorTotal = valorTotal;
        this.dataPedido = LocalDateTime.now();
        this.status = "EM_PROCESSAMENTO";
        this.loja = loja;
    }

    // Getters
    public String getId() { return id; }
    public String getCompradorCpf() { return compradorCpf; }
    public Map<String, Integer> getItens() { return new HashMap<>(itens); }
    public double getValorTotal() { return valorTotal; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public String getStatus() { return status; }
    public Loja getLoja() { return loja; }

    // Setters
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