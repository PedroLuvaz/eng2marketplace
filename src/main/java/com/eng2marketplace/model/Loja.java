package com.eng2marketplace.model;

import java.util.ArrayList;
import java.util.List;

public class Loja {
    private String nome;
    private String email;
    private String senha;
    private String cpfCnpj;
    private String endereco;
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Loja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpfCnpj = cpfCnpj;
        this.endereco = endereco;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
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
        return "Loja{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpfCnpj='" + cpfCnpj + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
