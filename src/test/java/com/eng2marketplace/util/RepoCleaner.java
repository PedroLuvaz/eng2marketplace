package com.eng2marketplace.util;

import com.eng2marketplace.model.*;
import com.eng2marketplace.repository.*;


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

        ProdutoRepository pd = new ProdutoRepository(lr);
        for(Produto item: pd.listar())
            pd.remover(item.getNome());
    }

}
