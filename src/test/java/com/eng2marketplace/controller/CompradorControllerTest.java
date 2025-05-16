package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.repository.CompradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de unidade do controlador de compradores.
 */
class CompradorControllerTest {

    @BeforeEach
    void cleanUp() {
        CompradorRepository repo = new CompradorRepository();
        repo.limpar();
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
        Comprador comprador = result.getFirst();
        assertEquals("Joana Ferreira", comprador.getNome());
        assertEquals("jfjf@mail.ko", comprador.getEmail());
        assertEquals("07482175403", comprador.getCpf());
    }
    /**
     * Testa persistir instância de comprador com cpf inválido
     */
    @Test
    void testSalvarCPFInvalido() {
        CompradorController control = new CompradorController();

        try {
            control.adicionarComprador("Joana Ferreira", "jfjf@mail.ko", "321321", "3", "Rua Margaridas, Número 123");
            fail();
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
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
        assertEquals("07482175403", result.get(0).getCpf());
        assertEquals("07289193905", result.get(1).getCpf());
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

        boolean result = control.removerComprador("07482175403");

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
            controller.buscarCompradorPorCpf(""); // CPF vazio
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.buscarCompradorPorCpf(null); // CPF nulo
        });
    }

    @Test
    void testLogin() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Elisabeth", "lisa@email.com", "tudocerto", "410.883.267-31", "Rua M, 90"));

        CompradorController controller = new CompradorController();
        boolean loginOk = controller.login("410.883.267-31", "tudocerto");

        assertTrue(loginOk);
    }

    @Test
    void testLoginFailSenha() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Elisabeth", "lisa@email.com", "tudocerto", "410.883.267-31", "Rua M, 90"));

        CompradorController controller = new CompradorController();
        boolean loginOk = controller.login("410.883.267-31", "");

        assertFalse(loginOk);
    }

    @Test
    void testLoginFailCPF() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Elisabeth", "lisa@email.com", "tudocerto", "410.883.267-31", "Rua M, 90"));

        CompradorController controller = new CompradorController();
        boolean loginOk = controller.login("000.000.000-00", "tudocerto");

        assertFalse(loginOk);
    }

    @Test
    void testLoginFailTooMuch() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Elisabeth", "lisa@email.com", "tudocerto", "410.883.267-31", "Rua M, 90"));

        CompradorController controller = new CompradorController();
        try {
            for(int i=0; i<10; i++)
                controller.login("410.883.267-31", "");
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void testLogout() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Vanessa", "nessa2@email.com", "1111111", "723.863.667-04", "Rua M, 90"));
        CompradorController controller = new CompradorController();
        controller.login("723.863.667-04", "1111111");

        controller.logout();

        assertFalse(controller.isLoggedIn());
    }

    @Test
    void testIsLoggedIn() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Natália", "tantan@email.com", "çáááááá", "514.332.689-27", "Rua M, 90"));
        CompradorController controller = new CompradorController();
        controller.login("514.332.689-27", "çáááááá");

        boolean resultado = controller.isLoggedIn();

        assertTrue(resultado);
    }

    @Test
    void testGetCompradorLogado() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Jocimar", "jcmr@email.com", "ehehehehe", "143.326.890-92", "Rua M, 33"));
        CompradorController controller = new CompradorController();
        controller.login("143.326.890-92", "ehehehehe");

        Comprador resultado = controller.getCompradorLogado();

        assertEquals("143.326.890-92", resultado.getCpf());
    }

    @Test
    void testGetCompradorLogadoSemLogar() {
        CompradorController controller = new CompradorController();

        Comprador resultado = controller.getCompradorLogado();

        assertNull(resultado);
    }

    @Test
    void testGetCarrinho() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");
        controller.adicionarAoCarrinho("1111", 5);

        Map<String, Integer> result = controller.getCarrinho();

        assertFalse(result.isEmpty());
        assertEquals(5, result.get("1111"));
    }

    @Test
    void testGetCarrinhoSemLogar() {
        CompradorController controller = new CompradorController();

        try {
            controller.getCarrinho();
            fail();
        } catch(IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void testAdicionarAoCarrinho() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");

        boolean ok = controller.adicionarAoCarrinho("1111", 5);

        assertTrue(ok);
        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertEquals(1, teste.getCarrinho().size());
    }

    @Test
    void testAdicionarAoCarrinhoQuantidadeInvalida() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");

        boolean ok = controller.adicionarAoCarrinho("1111", 0);

        assertFalse(ok);
        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertEquals(0, teste.getCarrinho().size());
    }

    @Test
    void testAdicionarAoCarrinhoSemLogar() {
        CompradorController controller = new CompradorController();

        try {
            controller.adicionarAoCarrinho("1111", 5);
            fail();
        } catch(IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void testRemoverDoCarrinho() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");
        controller.adicionarAoCarrinho("1111", 5);
        controller.adicionarAoCarrinho("2222", 5);

        controller.removerDoCarrinho("2222");

        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertEquals(1, teste.getCarrinho().size());
    }

    @Test
    void testRemoverItemInexistenteDoCarrinho() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");
        controller.adicionarAoCarrinho("1111", 5);
        controller.adicionarAoCarrinho("2222", 5);

        boolean ok = controller.removerDoCarrinho("3333");

        assertFalse(ok);
        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertEquals(2, teste.getCarrinho().size());
    }

    @Test
    void testRemoverAoCarrinhoSemLogar() {
        CompradorController controller = new CompradorController();

        try {
            controller.removerDoCarrinho("1111");
            fail();
        } catch(IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void testLimparCarrinho() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");
        controller.adicionarAoCarrinho("1111", 5);
        controller.adicionarAoCarrinho("2222", 5);

        controller.limparCarrinho();

        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertTrue(teste.getCarrinho().isEmpty());
    }

    @Test
    void testLimparCarrinhoSemLogar() {
        CompradorController controller = new CompradorController();

        try {
            controller.limparCarrinho();
            fail();
        } catch(IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void testAlterarQuantidadeCarrinho() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");
        controller.adicionarAoCarrinho("1111", 5);
        controller.adicionarAoCarrinho("2222", 5);

        controller.alterarQuantidadeCarrinho("1111", 10);

        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertEquals(10, teste.getCarrinho().get("1111"));
    }

    @Test
    void testAlterarQuantidadeCarrinhoInvalida() {
        CompradorRepository repo = new CompradorRepository();
        repo.salvar(new Comprador("Adriano", "diano@email.com", "21071998", "430.269.900-21", "Rua M, 45"));
        CompradorController controller = new CompradorController();
        controller.login("430.269.900-21", "21071998");
        controller.adicionarAoCarrinho("1111", 5);
        controller.adicionarAoCarrinho("2222", 5);

        boolean ok = controller.alterarQuantidadeCarrinho("1111", 0);

        assertFalse(ok);
        Comprador teste = repo.buscarPorCpf("430.269.900-21").orElseThrow();
        assertEquals(5, teste.getCarrinho().get("1111"));
    }

    @Test
    void testAlterarQuantidadeCarrinhoSemLogar() {
        CompradorController controller = new CompradorController();

        try {
            controller.alterarQuantidadeCarrinho("1111", 120);
            fail();
        } catch(IllegalStateException e) {
            assertTrue(true);
        }
    }
}
