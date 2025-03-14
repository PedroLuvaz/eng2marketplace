package com.eng2marketplace.controller;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.repository.LojaRepository;
import java.util.List;

public class LojaController {
    private LojaRepository lojaRepository;

    public LojaController() {
        this.lojaRepository = new LojaRepository();
    }

    public void adicionarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        Loja loja = new Loja(nome, email, senha, cpfCnpj, endereco);
        lojaRepository.salvar(loja);
    }

    public List<Loja> listarLojas() {
        return lojaRepository.listar();
    }

    public boolean removerLoja(String cpfCnpj) {
        return lojaRepository.remover(cpfCnpj);
    }
}
