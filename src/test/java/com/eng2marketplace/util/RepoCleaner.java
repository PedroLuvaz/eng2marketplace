package com.eng2marketplace.util;

import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.repository.AdministradorRepository;
import com.eng2marketplace.repository.CompradorRepository;
import com.eng2marketplace.repository.LojaRepository;
import com.eng2marketplace.repository.PedidoRepository;


public final class RepoCleaner {

    /**
     * Limpa os repositórios, removendo todas as entradas
     */
    public static void cleanRepos() {
        AdministradorRepository ar = new AdministradorRepository();
        for(Administrador item: ar.listar())
            ar.removerPorEmail(item.getEmail());

        CompradorRepository cr = new CompradorRepository();
        for(Comprador item: cr.listar())
            cr.remover(item.getCpf());

        LojaRepository lr = new LojaRepository();
        for(Loja item: lr.listar())
            lr.remover(item.getCpfCnpj());

        PedidoRepository pr = new PedidoRepository();
        for(Pedido item: pr.listarTodos())
            pr.remover(item.getId());
    }

}
