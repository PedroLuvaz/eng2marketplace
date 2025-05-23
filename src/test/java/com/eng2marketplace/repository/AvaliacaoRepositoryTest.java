package com.eng2marketplace.repository;

import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.model.Avaliacao;
import com.eng2marketplace.repository.AvaliacaoRepository;
import com.eng2marketplace.repository.AvaliacaoRepository;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do repositório de avaliações.
 */
public class AvaliacaoRepositoryTest {

    @BeforeEach
    void setUp() {
        // limpa todos os repositórios
        AvaliacaoRepository ar = new AvaliacaoRepository();
        ar.limpar();
    }


    /**
     * Testa salvar instância de avaliações.
     */
    @Test
    public void testSalvar() {
        Avaliacao avaliacao = new Avaliacao("123456789", 5, "Muito bom");
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(avaliacao);
        List<Avaliacao> avaliacoes = ar.listar();

        assertEquals(1, avaliacoes.size());
        assertEquals("Muito bom", avaliacoes.get(0).getComentario());
        assertEquals(5, avaliacoes.get(0).getNota());
    }

    /**
     * Testa remover uma instãncia de avaliação atraves do CPF.
     */
    @Test
    public void testRemover() {
        Avaliacao avaliacao = new Avaliacao("987654321", 2, "Ruim");
        Avaliacao avaliacao2 = new Avaliacao("987654312", 1, "Horrivel");
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(avaliacao);
        ar.salvar(avaliacao2);
        boolean result = ar.remover(a -> a.getCompradorCpf().equalsIgnoreCase("987654312"));

        assertTrue(result);
        assertEquals(1, ar.listar().size());
    }

    /**
     * Testa remover uma avaliação com um CPF inválido.
     */
    @Test
    void testRemoverPorCPFInvalido() {
        Avaliacao av = new Avaliacao("123456", 2, "||||||||");
        Avaliacao av2 = new Avaliacao("654321", 5, "%%%%%%%%"); // nota corrigida para valor válido
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(av);
        ar.salvar(av2);

        boolean result = ar.remover(a -> a.getCompradorCpf().equalsIgnoreCase("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        assertFalse(result);
        assertEquals(2, ar.listar().size());
    }

    /**
     * Testa listar avaliações em um arquivo vazio.
     */
    @Test
    void testarListarVazio() {
        AvaliacaoRepository ar = new AvaliacaoRepository();
        List<Avaliacao> avaliacoes = ar.listar();

        assertTrue(avaliacoes.isEmpty());
    }

    /**
     * Testa listar avaliações.
     */
    @Test
    void testListar() {
        Avaliacao av = new Avaliacao("123456789", 5, "Bom");
        Avaliacao av2 = new Avaliacao("987654321", 4, "Gostei");
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(av);
        ar.salvar(av2);

        List<Avaliacao> avaliacoes = ar.listar();

        assertEquals(2, avaliacoes.size());
        assertEquals(av.getCompradorCpf(), avaliacoes.get(0).getCompradorCpf());
        assertEquals(av.getNota(), avaliacoes.get(0).getNota());
        assertEquals(av.getComentario(), avaliacoes.get(0).getComentario());
        assertEquals(av2.getCompradorCpf(), avaliacoes.get(1).getCompradorCpf());
        assertEquals(av2.getNota(), avaliacoes.get(1).getNota());
        assertEquals(av2.getComentario(), avaliacoes.get(1).getComentario());
    }

    /**
     * Testa buscar avaliações por CPF.
     */
    @Test
    void testBuscarPorCpf() {
        Avaliacao av = new Avaliacao("123", 4, "Ruim");
        Avaliacao av2 = new Avaliacao("321", 1, "Péssimo");
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(av);
        ar.salvar(av2);

        Optional<Avaliacao> result = ar.buscar(a -> a.getCompradorCpf().equalsIgnoreCase("123"));

        assertTrue(result.isPresent());
        assertEquals(result.get().getCompradorCpf(), av.getCompradorCpf());
    }

    /**
     * Testa buscar um CPF não registrado.
     */
    @Test
    void testBuscarInvalido() {
        Avaliacao av = new Avaliacao("123", 5, "********");
        Avaliacao av2 = new Avaliacao("567", 4, "--------");
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(av);
        ar.salvar(av2);
        Optional<Avaliacao> result = ar.buscar(
            a -> a.getCompradorCpf().equalsIgnoreCase("999"));

        assertFalse(result.isPresent());
    }


    /**
     * Testa atualizar uma avaliação.
     */
    @Test
    void testAtualizar() {
        Avaliacao original = new Avaliacao("cpfX", 2, "Ruim");
        Avaliacao nova = new Avaliacao("cpfX", 4, "Melhorou");
        AvaliacaoRepository ar = new AvaliacaoRepository();


        ar.salvar(original);
        boolean atualizado = ar.atualizar(nova, a -> a.getCompradorCpf().equals("cpfX"));

        assertTrue(atualizado);

        List<Avaliacao> lista = ar.listar();
        assertEquals(1, lista.size());
        assertEquals(4, lista.get(0).getNota());
        assertEquals("Melhorou", lista.get(0).getComentario());
    }

    /**
     * Testa atualizar uma avaliação usando um email inválído
     */
    @Test
    void testAtualizarInvalido() {
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(new Avaliacao("88888", 5, "////////"));
        ar.salvar(new Avaliacao("12345", 4, "########"));

        boolean result = ar.atualizar(new Avaliacao("1010101", 3,"++++++++"));
        assertFalse(result);

        assertEquals("88888", ar.listar().getFirst().getCompradorCpf());
        assertEquals("12345", ar.listar().getLast().getCompradorCpf());
    }

    /**
     * Testa limpar todas as avaliações.
     */
    @Test
    void testLimpar() {
        AvaliacaoRepository ar = new AvaliacaoRepository();

        ar.salvar(new Avaliacao("999", 3, "Ruim"));
        ar.salvar(new Avaliacao("888", 4, "Mediano"));
        ar.salvar(new Avaliacao("777", 5, "Bom"));


        assertEquals(3, ar.listar().size());
        ar.limpar();

        assertTrue(ar.listar().isEmpty());
    }


}
