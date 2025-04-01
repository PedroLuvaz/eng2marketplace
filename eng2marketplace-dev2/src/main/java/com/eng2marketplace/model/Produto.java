package com.eng2marketplace.model;

public class Produto {
    private String nome;
    private double valor;
    private String tipo;
    private int quantidade;
    private String marca;
    private String descricao;
    private Loja loja;  // Relacionamento com a Loja

    public Produto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, Loja loja) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.marca = marca;
        this.descricao = descricao;
        this.loja = loja;
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

    @Override
    public String toString() {
        return "Produto{" +
                "nome='" + nome + '\'' +
                ", valor=" + valor +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", marca='" + marca + '\'' +
                ", descricao='" + descricao + '\'' +
                ", loja=" + loja.getNome() +
                '}';
    }
}
