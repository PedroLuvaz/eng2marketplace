package com.eng2marketplace.repository;

import com.eng2marketplace.model.Administrador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


// dia que o mockito não dar problema vai chover dinheiro
// mock(Administrador.class) seta todos os atributos para null


/**
 * Teste de unidade do repositório de administradores
 */
class AdministradorRepositoryTest {

    @BeforeEach
    void setUp() {
        // limpa todos os repositórios
        AdministradorRepository ar = new AdministradorRepository();
        ar.limpar();
    }

    /**
     * Testa salvar instância de administrador
     */
    @Test
    void testSalvar() {
        Administrador adm = new Administrador("Tavares", "vares31@sipwith.us", "........");
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(adm);
        List<Administrador> admins = ar.listar();
        Administrador admGot = admins.getFirst();

        assertEquals(1, admins.size());
        assertEquals(adm.getEmail(), admGot.getEmail());
        assertEquals(adm.getNome(), admGot.getNome());
    }

    /**
     * Testa listar administradores a partir de um arquivo vazio
     */
    @Test
    void testListarVazio() {
        AdministradorRepository ar = new AdministradorRepository();
        List<Administrador> admins = ar.listar();

        assertTrue(admins.isEmpty());
    }

    /**
     * Testa listar administradores
     */
    @Test
    void testListar() {
        Administrador adm = new Administrador("Pereira", "prpr@scm.kr", "''''''''");
        Administrador adm2 = new Administrador("Silva", "ldsilva@tuta.io", "        ");
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(adm);
        ar.salvar(adm2);

        List<Administrador> admins = ar.listar();

        assertEquals(2, admins.size());
        assertEquals(adm.getEmail(), admins.get(0).getEmail());
        assertEquals(adm.getNome(), admins.get(0).getNome());
        assertEquals(adm2.getEmail(), admins.get(1).getEmail());
        assertEquals(adm2.getNome(), admins.get(1).getNome());
    }

    /**
     * Testa recuperar um administrador buscando por email
     */
    @Test
    void testBuscar() {
        Administrador adm = new Administrador("Santos", "breja@full.no", "********");
        Administrador adm2 = new Administrador("Lourenço", "ttk@fish.br", "--------");
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(adm);
        ar.salvar(adm2);

        Optional<Administrador> result = ar.buscar(
            a -> a.getEmail().equalsIgnoreCase("breja@full.no"));

        assertTrue(result.isPresent());
        assertEquals(result.get().getEmail(), adm.getEmail());
        assertEquals(result.get().getNome(), adm.getNome());
    }

    /**
     * Testa recuperar um administrador informando um email inválido.
     */
    @Test
    void testBuscarInvalido() {
        Administrador adm = new Administrador("Santos", "breja@full.no", "********");
        Administrador adm2 = new Administrador("Lourenço", "ttk@fish.br", "--------");
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(adm);
        ar.salvar(adm2);
        Optional<Administrador> result = ar.buscar(
            a -> a.getEmail().equalsIgnoreCase("random@rng.num"));

        assertFalse(result.isPresent());
    }

    /**
     * Testa remover um administrador usando o email
     */
    @Test
    void testRemover() {
        Administrador adm = new Administrador("Carlos", "carlos.mengo@courier.bat", "||||||||");
        Administrador adm2 = new Administrador("Vizeu", "zivizeu@default.html", "%%%%%%%%");
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(adm);
        ar.salvar(adm2);

        boolean result = ar.remover(a -> a.getEmail().equalsIgnoreCase("zivizeu@default.html"));

        assertTrue(result);
        assertEquals(1, ar.listar().size());
    }

    /**
     * Testa remover um administrador usando um emila inválido
     */
    @Test
    void testRemoverPorEmailInvalido() {
        Administrador adm = new Administrador("Carlos", "carlos.mengo@courier.bat", "||||||||");
        Administrador adm2 = new Administrador("Vizeu", "zivizeu@default.html", "%%%%%%%%");
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(adm);
        ar.salvar(adm2);

        boolean result = ar.remover(a -> a.getEmail().equalsIgnoreCase("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        assertFalse(result);
        assertEquals(2, ar.listar().size());
    }

    /**
     * Testa remover todos os administradores de uma vez só
     */
    @Test
    void testLimpar() {
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(new Administrador("Carlos", "carlos.mengo@courier.bat", "||||||||"));
        ar.salvar(new Administrador("Vizeu", "zivizeu@default.html", "%%%%%%%%"));
        ar.salvar(new Administrador("Lourenço", "ttk@fish.br", "--------"));
        ar.salvar(new Administrador("Santos", "breja@full.no", "********"));
        ar.salvar(new Administrador("Silva", "ldsilva@tuta.io", "        "));
        ar.salvar(new Administrador("Pereira", "prpr@scm.kr", "''''''''"));
        ar.salvar(new Administrador("Tavares", "vares31@sipwith.us", "........"));

        assertEquals(7, ar.listar().size());
        ar.limpar();

        assertTrue(ar.listar().isEmpty());
    }

    /**
     * Testa atualizar as informações de um administrador
     */
    @Test
    void testAtualizar() {
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(new Administrador("Carioca", "itaquera@dude.mi", "////////"));
        ar.salvar(new Administrador("Givaldo", "sosiu@peek.lu", "########"));

        boolean result = ar.atualizar(
            new Administrador("Xavier", "itaquera@dude.mi", "////////"));
        assertTrue(result);

        assertEquals("Xavier", ar.listar().getLast().getNome());
        assertEquals("itaquera@dude.mi", ar.listar().getLast().getEmail());
    }

    /**
     * Testa atualizar as informações de um administrador usando um email invalido
     */
    @Test
    void testAtualizarInvalido() {
        AdministradorRepository ar = new AdministradorRepository();

        ar.salvar(new Administrador("Carioca", "itaquera@dude.mi", "////////"));
        ar.salvar(new Administrador("Givaldo", "sosiu@peek.lu", "########"));

        boolean result = ar.atualizar(
            new Administrador("Jânio", "gol2000@car.gs", "++++++++"));
        assertFalse(result);

        assertEquals("Carioca", ar.listar().getFirst().getNome());
        assertEquals("Givaldo", ar.listar().getLast().getNome());
    }
}
