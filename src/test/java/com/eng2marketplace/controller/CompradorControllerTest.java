package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de compradores.
 */
class CompradorControllerTest {
    private static final String TEST_FILE_PATH = "src/main/data/compradores.json";

    @BeforeEach
    @AfterEach
    void cleanUp() {
        // Garante que o arquivo de teste seja deletado antes e depois de cada teste
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        
        // Garante que o diretório data existe
        new File("./data").mkdirs();
    }

    /**
     * Testa persistir instância de comprador
     */
    @Test
    void testSalvar() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Rua Margaridas, Número 123");

        List<Comprador> result = control.listarCompradores();

        assertEquals(1, result.size());
        Comprador comprador = result.get(0);
        assertEquals("Joana Ferreira", comprador.getNome());
        assertEquals("jfjf@mail.ko", comprador.getEmail());
        assertEquals("074.821.754-03", comprador.getCpf());
    }

    /**
     * Testa listar compradores a partir de um repositório vazio
     */
    @Test
    void testListarVazio() {
        CompradorController control = new CompradorController();
        List<Comprador> result = control.listarCompradores();

        assertTrue(result.isEmpty());
    }

    /**
     * Testa listar múltiplos compradores
     */
    @Test
    void testListar() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05", "Rua Marimbondo Caboclo, 3001");

        List<Comprador> result = control.listarCompradores();

        assertEquals(2, result.size());
        assertEquals("Joana Ferreira", result.get(0).getNome());
        assertEquals("Ana Pontes", result.get(1).getNome());
        
        // Verifica se os CPFs estão corretos
        assertEquals("074.821.754-03", result.get(0).getCpf());
        assertEquals("072.891.939-05", result.get(1).getCpf());
    }

    /**
     * Testa remover compradores de um repositório vazio
     */
    @Test
    void testRemoveVazio() {
        CompradorController control = new CompradorController();
        boolean result = control.removerComprador("000.000.000-00");

        assertFalse(result);
        assertTrue(control.listarCompradores().isEmpty());
    }

    /**
     * Testa remover comprador inexistente
     */
    @Test
    void testRemoverInexistente() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05", "Rua Marimbondo Caboclo, 3001");

        boolean result = control.removerComprador("484.242.121-01");

        assertFalse(result);
        assertEquals(2, control.listarCompradores().size());
    }

    /**
     * Testa remover comprador
     */
    @Test
    void testRemover() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        control.adicionarComprador("Ana Pontes", "anabanana@mail.ko", "123123", "072.891.939-05", "Rua Marimbondo Caboclo, 3001");

        boolean result = control.removerComprador("074.821.754-03");

        assertTrue(result);
        List<Comprador> compradores = control.listarCompradores();
        assertEquals(1, compradores.size());
        assertEquals("Ana Pontes", compradores.get(0).getNome());
    }

    /**
     * Testa adicionar comprador com CPF duplicado
     */
    @Test
    void testAdicionarCompradorComCpfDuplicado() {
        CompradorController control = new CompradorController();
        control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "074.821.754-03", "Avenida Predial 955/B");
        
        // Verifica se lança exceção ao tentar adicionar duplicado
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            control.adicionarComprador("Joana Silva", "jf2@mail.ko", "654321", "074.821.754-03", "Outro endereço");
        });
        
        assertEquals("Já existe um comprador cadastrado com este CPF", exception.getMessage());
        
        List<Comprador> compradores = control.listarCompradores();
        assertEquals(1, compradores.size()); // Deve manter apenas o primeiro
        assertEquals("Joana Ferreira", compradores.get(0).getNome());
    }

    /**
     * Testa buscar comprador por CPF
     */
    @Test
void testBuscarPorCpf_Encontrado() {
    CompradorController controller = new CompradorController();
    controller.adicionarComprador("João Silva", "joao@email.com", "senha123", "123.456.789-09", "Rua A, 123");
    
    Comprador encontrado = controller.buscarCompradorPorCpf("123.456.789-09");
    
    assertNotNull(encontrado);
    assertEquals("João Silva", encontrado.getNome());
}



@Test
void testBuscarPorCpf_FormatoInvalido() {
    CompradorController controller = new CompradorController();
    
    assertThrows(IllegalArgumentException.class, () -> {
        controller.buscarCompradorPorCpf("123"); // CPF muito curto
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
        controller.buscarCompradorPorCpf(null); // CPF nulo
    });
}
}