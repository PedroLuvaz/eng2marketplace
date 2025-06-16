package com.eng2marketplace.model;

import java.util.HashMap;
import java.util.Map;

public class Comprador {
    private String nome;
    private String email;
    private String senha;  // Na prática, isso deveria ser um hash da senha
    private String cpf;
    private String endereco;
    private Map<String, Integer> carrinho;  // Lista de IDs ou códigos dos produtos
    private int pontuacao;// Atributo para armazenar a pontuação do comprador

    public Comprador(String nome, String email, String senha, String cpf, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.endereco = endereco;
        this.carrinho = new HashMap<>();
        this.pontuacao = 0;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public int getPontuacao() { return pontuacao; }

    // Métodos de Pontuação
    public void adicionarPontos(int pontos) {
        if (pontos < 0) throw new IllegalArgumentException("Não é possível adicionar pontos negativos.");
        this.pontuacao += pontos;
    }

    public boolean usarPontos(int pontos) {
        if (pontos <= 0) return false;
        if (this.pontuacao >= pontos) {
            this.pontuacao -= pontos;
            return true;
        }
        return false;
    }

    public Map<String, Integer> getCarrinho() {
        return carrinho;
    }
    public void setCarrinho(Map<String, Integer> carrinho) {
        this.carrinho = carrinho;
    }


    @Override
    public String toString() {
        return String.format("Comprador{nome='%s', email='%s', cpf='%s', endereço='%s'}",
            this.nome, this.email, this.cpf, this.endereco);
    }
}
