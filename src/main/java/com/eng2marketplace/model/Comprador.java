package com.eng2marketplace.model;

public class Comprador {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String endereco;


    public Comprador(String nome, String email, String senha, String cpf, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.endereco = endereco;
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

    @Override
    public String toString() {
        return String.format("Comprador{nome='%s', email='%s', cpf='%s', endereço='%s'}",
            this.nome, this.email, this.cpf, this.endereco);
    }
}
