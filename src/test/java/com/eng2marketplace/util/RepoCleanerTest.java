package com.eng2marketplace.util;

import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.repository.AdministradorRepository;
import com.eng2marketplace.repository.CompradorRepository;
import com.eng2marketplace.repository.LojaRepository;
import com.eng2marketplace.repository.PedidoRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.eng2marketplace.util.RepoCleaner.cleanRepos;
import static org.junit.jupiter.api.Assertions.assertTrue;


public final class RepoCleanerTest {

    @Test
    void cleanReposTest() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("teste", "latabot@ronaldinho.io", "bipbopbip"));

        CompradorRepository cr = new CompradorRepository();
        cr.salvar(new Comprador("Mario Mario", "super@bros.two", "goomba", "000.000.000-00", "Pipe M8"));

        LojaRepository lr = new LojaRepository();
        lr.salvar(new Loja("Equipamentos Industriais", "beto1986@mail.su", "Não", "123.132.321-31", "Avenida Três de Dezembro, 45"));

        Map<String, Integer> carrinho = new HashMap<>();
        carrinho.put("Ovo de Codorna", 150);
        PedidoRepository pr = new PedidoRepository();
        pr.salvar(new Pedido("123.123.123-33", carrinho, 50.0));

        cleanRepos();
        assertTrue(ar.listar().isEmpty());
        assertTrue(cr.listar().isEmpty());
        assertTrue(lr.listar().isEmpty());
        assertTrue(pr.listarTodos().isEmpty());
    }
}
