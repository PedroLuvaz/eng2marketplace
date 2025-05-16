package com.eng2marketplace.model;

public class Administrador {
    private String nome;
    private String email;
    private String senha;

    public Administrador(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public boolean temPermissao(String operacao) {
        return true; 
    }
}
