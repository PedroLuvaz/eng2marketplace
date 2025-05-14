package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do repositório de produto.
 */
class ProdutoRepositoryTest {
    LojaRepository lr;
    private final static Loja loja = new Loja("Jack Poções", "jackpot@mail.co", "nomnomnom", "999.666.333-00", "Floresta Negra, Árvore Gigantesca, Cabine 3");;
    private final static Loja mercado = new Loja("Mercadinho da Duda", "dudaa@bmail.li", "eueuea", "888.888.888-11", "Rua São Paulo, 1B");
    private final static Loja posto = new Loja("Epsilon Conveniência", "eps@gas.tu", "friday in cali", "626.300.303-01", "Barro Da Pedra, 20");

    @BeforeEach
    void setup() {
        lr = new LojaRepository();
        lr.limpar();

        ProdutoRepository pr = new ProdutoRepository(lr);
        pr.limpar();

        lr.salvar(loja);
        lr.salvar(mercado);
        lr.salvar(posto);
    }

    /**
     * Testa salvar loja
     */
    @Test
    void testSalvar() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto p = new Produto("Queijo 100g", 0.00, "Alimentos", 110, "Super Laticínios", "Queijo parmesão fresco", mercado);

        repo.salvar(p);

        assertEquals(1, repo.listar().size());
        Produto result = repo.listar().get(0);
        assertEquals("Queijo 100g", result.getNome());
        assertEquals(p.getId(), result.getId());
        assertEquals(mercado.getCpfCnpj(), result.getLoja().getCpfCnpj());
    }

    @Test
    void removerPorNome() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto p = new Produto("Café 250g", 15.00, "Alimentos", 100, "Santo Bráz", "Café extra forte", mercado);
        repo.salvar(p);
        repo.salvar(new Produto("Motul 20w50 1L", 40.00, "Suprimentos", 100, "Motul", "Óleo lubrificante motores 4 tempos", posto));

        boolean result = repo.remover("Motul 20w50 1L");

        assertTrue(result);
        assertEquals(1, repo.listar().size());
        Produto restante = repo.listar().get(0);
        assertEquals("Café 250g", restante.getNome());
        assertEquals(p.getId(), restante.getId());
        assertEquals(mercado.getCpfCnpj(), restante.getLoja().getCpfCnpj());
    }

    @Test
    void removerPorNomeInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Café 250g", 15.00, "Alimentos", 100, "Santo Bráz", "Café extra forte", mercado));
        repo.salvar(new Produto("Motul 20w50 1L", 40.00, "Suprimentos", 100, "Motul", "Óleo lubrificante motores 4 tempos", posto));

        boolean result = repo.remover("Lubrax 15w40 3L");

        assertFalse(result);
        assertEquals(2, repo.listar().size());
        List<Produto> restante = repo.listar();
        assertEquals("Café 250g", restante.get(0).getNome());
        assertEquals("Motul 20w50 1L", restante.get(1).getNome());
    }

    @Test
    void buscar() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Frasco de Regeneração", 100.00, "Poções de recuperação", 15, "Bruxa Keka", "Frasco de poção de vida inferior", loja));
        repo.salvar(new Produto("Cachaça Corote 250mL", 5.50, "Conveniência", 30, "Corote", "Barrigudinha Corote 250mL", posto));

        Optional<Produto> result = repo.buscar(
            p -> p.getValor() == 5.50);

        assertFalse(result.isEmpty());
        assertEquals("Cachaça Corote 250mL", result.get().getNome());
    }

    @Test
    void buscarInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Nutella 3Kg", 300.00, "Suprimentos", 15, "Ferrero", "Creme de avelã 3Kg", loja));
        repo.salvar(new Produto("Caneta Bic", 2.50, "Conveniência", 30, "Bic", "Caneta Bic azul", posto));

        Optional<Produto> result = repo.buscar(
            p -> p.getTipo().equals("Bicicletas"));

        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorNome() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Sardinha Fresca Kg", 18.00, "Pescados", 16, "MesMar", "Sardinha sem cabeça congelada 1Kg", mercado));
        repo.salvar(new Produto("Guarda-Chuva", 22.90, "Conveniência", 5, "FangDream", "Guarda chuva unidade", posto));

        Optional<Produto> result = repo.buscarPorNome("Sardinha Fresca Kg");

        assertFalse(result.isEmpty());
        assertEquals("Sardinha Fresca Kg", result.get().getNome());
    }

    @Test
    void buscarPorNomeInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Leite de Égua 1L", 43.00, "Laticínios", 12, "FarmFarm", "Leite de égua 1L", mercado));

        Optional<Produto> result = repo.buscarPorNome("Chuteira Rainha");

        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorId() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto p = new Produto("Havaianas 39/40", 18.00, "Calçados", 7, "Havaianas", "Par de chinelo marrom", mercado);
        repo.salvar(p);
        repo.salvar(new Produto("Diesel l", 5.90, "Combustível", 1000, "Petrobras", "Óleo diesel S500", posto));

        Optional<Produto> result = repo.buscarPorId(p.getId());

        assertFalse(result.isEmpty());
        assertEquals("Havaianas 39/40", result.get().getNome());
    }

    @Test
    void buscarPorIdInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Playstation 2", 110.00, "Miscelâneos", 2, "Sony", "PS2 com 2 controles", posto));

        Optional<Produto> result = repo.buscarPorId("assuma que alguma coisa deu errado");

        assertTrue(result.isEmpty());
    }

    @Test
    void listar() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Ovo de dragão", 4500.00, "Ovos", 3, "Coletor", "Ovo de dragão para criação.", loja));
        repo.salvar(new Produto("Kingston ValueRAM", 850.00, "Conveniência", 10, "Kingston", "DDR4 32GB 2400MT/S", posto));

        List<Produto> resultado = repo.listar();

        assertEquals(2, resultado.size());
        assertEquals("Ovo de dragão", resultado.get(0).getNome());
        assertEquals("Kingston ValueRAM", resultado.get(1).getNome());
    }

    @Test
    void listarVazio() {
        ProdutoRepository repo = new ProdutoRepository(lr);

        List<Produto> resultado = repo.listar();

        assertEquals(0, resultado.size());
    }

    @Test
    void listarPorLoja() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Papel Explosivo", 1.50, "Amuletos", 3000, "Konoha", "Papel explosivo unidade", loja));
        repo.salvar(new Produto("Kunai", 9.50, "Ferramentas", 10, "Konoha", "Faca pontuda unidade", loja));
        repo.salvar(new Produto("Fralda pacote", 48.00, "Bebê", 50, "Johnson & Johnson", "Fralda tamanho GG pacote com 20 unidades", mercado));

        List<Produto> resultado = repo.listarPorLoja(loja.getCpfCnpj());

        assertEquals(2, resultado.size());
        assertEquals("Papel Explosivo", resultado.get(0).getNome());
        assertEquals("Kunai", resultado.get(1).getNome());
    }

    @Test
    void limpar() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Chapéu sem fundo", 9.50, "Equipamento", 15, "CDD", "Porta itens, pode ser usado como chapéu", loja));

        repo.limpar();

        assertTrue(repo.listar().isEmpty());
    }

    @Test
    void atualizar() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto bastao = new Produto("Batsao migco", 99.90, "Equipamento", 9, "CDD", "Bastão de madeira de comprimento variável", loja);
        repo.salvar(bastao);

        bastao.setNome("Bastão mágico");

        boolean resultado = repo.atualizar(bastao, p -> p.getId().equals(bastao.getId()));

        assertTrue(resultado);
        assertTrue(repo.buscarPorNome("Bastão mágico").isPresent());
        assertTrue(repo.buscarPorNome("Batsao migco").isEmpty());
    }

    @Test
    void atualizarInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto bastao = new Produto("Batsao migco", 99.90, "Equipamento", 9, "CDD", "Bastão de madeira de comprimento variável", loja);
        repo.salvar(bastao);

        bastao.setNome("Bastão mágico");

        boolean resultado = repo.atualizar(bastao, p -> p.getId().equals("bastao.getId()"));

        assertFalse(resultado);
        assertTrue(repo.buscarPorNome("Bastão mágico").isEmpty());
        assertTrue(repo.buscarPorNome("Batsao migco").isPresent());
    }

    @Test
    void testRemover() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto pico = new Produto("Raspberry Pi Pico", 49.95, "Eletrônicos", 5, "Adafruit", "Placa Raspberry Pi Pico", posto);
        repo.salvar(pico);

        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isPresent());
        boolean resultado = repo.remover(p -> p.getId().equals(pico.getId()));

        assertTrue(resultado);
        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isEmpty());
        assertTrue(repo.listar().isEmpty());
    }

    @Test
    void testRemoverInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto pico = new Produto("Raspberry Pi Pico", 49.95, "Eletrônicos", 5, "Adafruit", "Placa Raspberry Pi Pico", posto);
        repo.salvar(pico);

        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isPresent());
        boolean resultado = repo.remover("Arduino Mega");

        assertFalse(resultado);
        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isPresent());
        assertEquals(1, repo.listar().size());
    }
    @Test
    void removerPorId() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto pico = new Produto("Raspberry Pi Pico", 49.95, "Eletrônicos", 5, "Adafruit", "Placa Raspberry Pi Pico", posto);
        repo.salvar(pico);

        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isPresent());
        boolean resultado = repo.removerPorId(pico.getId());

        assertTrue(resultado);
        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isEmpty());
        assertTrue(repo.listar().isEmpty());
    }
    @Test
    void testRemoverPorIdInvalido() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        Produto pico = new Produto("Raspberry Pi Pico", 49.95, "Eletrônicos", 5, "Adafruit", "Placa Raspberry Pi Pico", posto);
        repo.salvar(pico);

        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isPresent());
        boolean resultado = repo.removerPorId("a");

        assertFalse(resultado);
        assertTrue(repo.buscarPorNome("Raspberry Pi Pico").isPresent());
        assertEquals(1, repo.listar().size());
    }

    @Test
    void testLimpar() {
        ProdutoRepository repo = new ProdutoRepository(lr);
        repo.salvar(new Produto("Raspberry Pi Pico", 49.95, "Eletrônicos", 5, "Adafruit", "Placa Raspberry Pi Pico", posto));
        repo.salvar(new Produto("STM32", 59.95, "Eletrônicos", 3, "ST", "Placa de desenvolvimento STM32", posto));
        repo.salvar(new Produto("Jetson Nano", 4999.95, "Eletrônicos", 5, "Nvidia", "Placa de desenvolvimento Nvidia Jetson Nano 4GB", posto));
        repo.salvar(new Produto("ESP32", 129.95, "Eletrônicos", 5, "EspressIf", "Placa ESP32-D0WD", posto));
        repo.salvar(new Produto("Arduino Nano", 59.95, "Eletrônicos", 5, "Arduino", "Placa Arduino Nano Atmega328", posto));

        assertEquals(5, repo.listar().size());
        repo.limpar();

        assertTrue(repo.listar().isEmpty());
    }

}
