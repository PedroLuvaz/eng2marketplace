package com.eng2marketplace.controller;

import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.repository.AdministradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorControllerTest {

    @BeforeEach
    void setUp() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.limpar();
    }

    @Test
    void testLogin() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Eduardo", "edu@admin.edu", "uma_senha"));

        AdministradorController control = new AdministradorController();
        boolean resultado = control.login("edu@admin.edu", "uma_senha");

        assertTrue(resultado);
    }

    @Test
    void testLoginFail() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Eduardo", "edu@admin.edu", "uma_senha"));

        AdministradorController control = new AdministradorController();
        boolean resultado = control.login("edu@admin.edu", "não_sei");

        assertFalse(resultado);
    }

    @Test
    void testLoginTooManyFail() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Eduardo", "edu@admin.edu", "uma_senha"));

        AdministradorController control = new AdministradorController();
        try {
            for(int i=0; i<10; i++)
                control.login("edu@admin.edu", "esqueci.");
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Número máximo de tentativas excedido", e.getMessage());
        }
    }

    @Test
    void testLogout() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Amanda", "amanda@admin.edu", "senha___"));

        AdministradorController control = new AdministradorController();
        control.login("amanda@admin.edu", "senha___");
        control.logout();

        assertFalse(control.isLoggedIn());
    }

    @Test
    void testIsLoggedIn() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Carlos", "cars@admin.edu", "Senha!!!"));

        AdministradorController control = new AdministradorController();
        control.login("cars@admin.edu", "Senha!!!");

        assertTrue(control.isLoggedIn());
    }

    @Test
    void testAdicionarAdministrador() {
        AdministradorController control = new AdministradorController();

        control.adicionarAdministrador("José Maria", "joma@admin.edu", "||$&ña||");

        AdministradorRepository ar = new AdministradorRepository();
        Optional<Administrador> teste = ar.buscar(adm -> adm.getEmail().equals("joma@admin.edu"));
        assertTrue(teste.isPresent());
        assertEquals("José Maria", teste.get().getNome());
    }

    @Test
    void testAdicionarAdministradorFail() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("José Maria", "joma@admin.edu", "||$&ña||"));

        AdministradorController control = new AdministradorController();
        try {
            control.adicionarAdministrador("José Maria", "joma@admin.edu", "||$&ña||");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Já existe um administrador com este email", e.getMessage());
        }
    }

    @Test
    void testRemoverAdministrador() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Bruno", "bru@admin.edu", "5.3.N.h.A"));

        AdministradorController control = new AdministradorController();
        control.removerAdministrador("bru@admin.edu");

        Optional<Administrador> teste = ar.buscar(adm -> adm.getEmail().equals("bru@admin.edu"));
        assertTrue(teste.isEmpty());
    }

    @Test
    void testBuscarAdministradorPorEmail() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Carolina", "carol@admin.edu", "se, nhá(c)"));

        AdministradorController control = new AdministradorController();
        Administrador resultado = control.buscarAdministradorPorEmail("carol@admin.edu");

        assertNotNull(resultado);
        assertEquals("carol@admin.edu", resultado.getEmail());
        assertEquals("Carolina", resultado.getNome());
    }

    @Test
    void testVerificarPermissao() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Paulo", "plins@admin.edu", "Senha senhA"));

        AdministradorController control = new AdministradorController();
        control.login("plins@admin.edu", "Senha senhA");
        boolean resultado = control.verificarPermissao("Qualquer coisa");

        assertTrue(resultado);
    }

    @Test
    void testVerificarPermissaoFail() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Paulo", "plins@admin.edu", "Senha senhA"));

        AdministradorController control = new AdministradorController();
        try {
            control.verificarPermissao("Qualquer coisa");
        } catch (IllegalStateException e) {
            assertEquals("Nenhum administrador logado", e.getMessage());
        }
    }

    @Test
    void testListarAdministradores() {
        AdministradorRepository ar = new AdministradorRepository();
        ar.salvar(new Administrador("Paulo", "plins@admin.edu", "Senha senhA"));
        ar.salvar(new Administrador("Carolina", "carol@admin.edu", "se, nhá(c)"));
        ar.salvar(new Administrador("Bruno", "bru@admin.edu", "5.3.N.h.A"));
        ar.salvar(new Administrador("José Maria", "joma@admin.edu", "||$&ña||"));
        ar.salvar(new Administrador("Carlos", "cars@admin.edu", "Senha!!!"));
        ar.salvar(new Administrador("Amanda", "amanda@admin.edu", "senha___"));
        ar.salvar(new Administrador("Eduardo", "edu@admin.edu", "uma_senha"));

        AdministradorController control = new AdministradorController();
        List<Administrador> lista = control.listarAdministradores();
        assertEquals(7, lista.size());
    }
}
