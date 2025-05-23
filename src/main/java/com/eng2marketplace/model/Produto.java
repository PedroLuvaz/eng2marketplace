package com.eng2marketplace.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Produto {
    private String id;
    private String nome;
    private double valor;
    private String tipo;
    private int quantidade;
    private String marca;
    private String descricao;
    private Loja loja;
    private List<Avaliacao> avaliacoes ;

    public Produto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, Loja loja) {
        this.id = UUID.randomUUID().toString(); // Gera ID único
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.marca = marca;
        this.descricao = descricao;
        this.loja = loja;
        this.avaliacoes = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Loja getLoja() { return loja; }
    public void setLoja(Loja loja) { this.loja = loja; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<Avaliacao> getAvaliacoes() {
        if (this.avaliacoes == null) {
            this.avaliacoes = new ArrayList<>();
        }
        return this.avaliacoes;
    }

    public double getMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) return 0.0;
        return avaliacoes.stream().mapToInt(Avaliacao::getNota).average().orElse(0.0);
    }

    public String getConceito() {
        double media = getMediaAvaliacoes();
        if (media == 0.0) return "Sem avaliações";
        if (media < 2.5) return "Ruim";
        if (media < 3.5) return "Médio";
        if (media < 4.5) return "Bom";
        return "Excelente";
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", valor=" + valor +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", marca='" + marca + '\'' +
                ", descricao='" + descricao + '\'' +
                ", loja=" + loja.getNome() +
                '}';
    }
}
