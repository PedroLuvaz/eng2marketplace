package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.repository.CompradorRepository;
import java.util.List;

public class CompradorController {
    private CompradorRepository compradorRepository;

    public CompradorController() {
        this.compradorRepository = new CompradorRepository();
    }

    public void adicionarComprador(String nome, String email, String senha, String cpf, String endereco) {
        Comprador comprador = new Comprador(nome, email, senha, cpf, endereco);
        compradorRepository.salvar(comprador);
    }

    public List<Comprador> listarCompradores() {
        return compradorRepository.listar();
    }

    public boolean removerComprador(String cpf) {
        return compradorRepository.remover(cpf);
    }
}
