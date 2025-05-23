package com.eng2marketplace.Facade;

import com.eng2marketplace.controller.AdministradorController;
import com.eng2marketplace.controller.CompradorController;
import com.eng2marketplace.controller.LojaController;
import com.eng2marketplace.controller.ProdutoController;
import com.eng2marketplace.model.Administrador;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.eng2marketplace.util.RepoCleaner.cleanRepos;
import static org.junit.jupiter.api.Assertions.*;

class MarketplaceFacadeTest {

    @BeforeEach
    void setUp() {
        cleanRepos();
    }

    /**
     * Verifica se o administrador padrão pode logar
     */
    @Test
    void testLoginAdministrador() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.verificarOuCriarAdminPadrao();

        AdministradorController ac = facade.getAdministradorController();
        assertTrue(facade.loginAdministrador("admin@marketplace.com", "admin123"));
        assertTrue(ac.isLoggedIn());
    }

    /**
     * Verifica se o administrador padrão pode encerrar sessão
     */
    @Test
    void testLogoutAdministrador() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.verificarOuCriarAdminPadrao();
        facade.loginAdministrador("admin@marketplace.com", "admin123");
        facade.logoutAdministrador();

        AdministradorController ac = facade.getAdministradorController();
        assertFalse(ac.isLoggedIn());
    }

    /**
     * Verifica se é possível adicionar um administrador extra
     */
    @Test
    void testAdicionarAdministrador() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.verificarOuCriarAdminPadrao();

        facade.adicionarAdministrador("Adriano", "chute.de.cabeca@mail.social", "12345");

        AdministradorController ac = facade.getAdministradorController();
        Administrador result = ac.listarAdministradores().getLast();
        assertEquals("Adriano", result.getNome());
        assertEquals("chute.de.cabeca@mail.social", result.getEmail());
        assertEquals("12345", result.getSenha());
    }

    /**
     * Checa se é possível remover um administrador
     */
    @Test
    void testRemoverAdministrador() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.verificarOuCriarAdminPadrao();

        facade.adicionarAdministrador("Kelly", "bazar.chq@talk.me", "paralelepípedo");
        assertTrue(facade.removerAdministrador("bazar.chq@talk.me"));

        // único admin deve ser o admin padrão
        AdministradorController ac = facade.getAdministradorController();
        List<Administrador> admins = ac.listarAdministradores();
        assertEquals(1, admins.size());
        Administrador result = admins.getLast();
        assertEquals("Administrador Padrão", result.getNome());
    }

    /**
     * Verifica se o administrador logado tem permissão (deve ter por padrão)
     */
    @Test
    void testAdministradorLogadoTemPermissao() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.verificarOuCriarAdminPadrao();

        facade.adicionarAdministrador("Catarina", "catpetshop@contact.us", "pinschermtonervoso");
        facade.loginAdministrador("catpetshop@contact.us", "pinschermtonervoso");
        boolean permissaoAdd = facade.administradorLogadoTemPermissao("Adicionar");

        assertTrue(permissaoAdd);
    }

    /**
     * Verifica se o facade aponta corretamente se um administrador está logado
     */
    @Test
    void testIsAdministradorLogado() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.verificarOuCriarAdminPadrao();

        assertFalse(facade.isAdministradorLogado());

        facade.adicionarAdministrador("Lili", "liriliana@music.shop", "LaRaLaIaaaaa");
        facade.loginAdministrador("liriliana@music.shop", "LaRaLaIaaaaa");

        assertTrue(facade.isAdministradorLogado());
    }

    /**
     * Testa se lista corretamente os administradores
     */
    @Test
    void testListarAdministradores() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.verificarOuCriarAdminPadrao();
        facade.adicionarAdministrador("Lili", "liriliana@music.shop", "LaRaLaIaaaaa");
        facade.adicionarAdministrador("Catarina", "catpetshop@contact.us", "pinschermtonervoso");
        facade.adicionarAdministrador("Kelly", "bazar.chq@talk.me", "paralelepípedo");
        facade.adicionarAdministrador("Adriano", "chute.de.cabeca@mail.social", "12345");

        assertEquals(5, facade.listarAdministradores().size());
    }

    /**
     * Testa se lista corretamente os administradores quando não foi criado nenhum
     */
    @Test
    void testListarAdministradoresVazio() {
        MarketplaceFacade facade = new MarketplaceFacade();
        assertTrue(facade.listarAdministradores().isEmpty());
    }

    /**
     * Verifica se o administrador padrão é criado corretamente
     */
    @Test
    void testVerificarOuCriarAdminPadrao() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.verificarOuCriarAdminPadrao();

        AdministradorController ac = facade.getAdministradorController();
        assertEquals(1, ac.listarAdministradores().size());
        Administrador result = ac.listarAdministradores().getLast();
        assertEquals("Administrador Padrão", result.getNome());
        assertEquals("admin@marketplace.com", result.getEmail());
        assertEquals("admin123", result.getSenha());
    }

    /**
     * Verifica se uma loja é adicionada corretamente
     */
    @Test
    void testAdicionarLoja() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.adicionarLoja("Paris Lanches",
            "pari.petit-dejeuner@food.place",
            "ZeRo1dOiS3",
            "11.111.111/0001-11",
            "Rodoviária Santos Dumont, quiosque 33");

        LojaController lc = new LojaController();
        assertEquals(1, lc.listarLojas().size());

        Loja result = lc.listarLojas().getFirst();
        assertEquals("Paris Lanches", result.getNome());
        assertEquals("11111111000111", result.getCpfCnpj());
    }

    /**
     * Verifica se lojas são listadas corretamente
     */
    @Test
    void testListarLojas() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.adicionarLoja("Zip Açaí",
            "zip@zipmail.ac",
            "cW?4a-32",
            "01.234.567/0001-89",
            "Centro Comercial ABC, No3");
        facade.adicionarLoja("Los Gatos Burger",
            "burger@ml.cat",
            "senha",
            "12.123.123/0001-12",
            "Centro Comercial ABC, No4");

        LojaController lc = new LojaController();
        assertEquals(2, lc.listarLojas().size());

        Loja result = lc.listarLojas().getFirst();
        assertEquals("Zip Açaí", result.getNome());
        assertEquals("01234567000189", result.getCpfCnpj());

        result = lc.listarLojas().getLast();
        assertEquals("Los Gatos Burger", result.getNome());
        assertEquals("12123123000112", result.getCpfCnpj());
    }

    /**
     * Verifica se uma loja é removida corretamente
     */
    @Test
    void testRemoverLoja() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.adicionarLoja("Zip Açaí",
            "zip@zipmail.ac",
            "cW?4a-32",
            "01.234.567/0001-89",
            "Centro Comercial ABC, No3");
        facade.adicionarLoja("Los Gatos Burger",
            "burger@ml.cat",
            "senha",
            "12.123.123/0001-12",
            "Centro Comercial ABC, No4");

        assertTrue(facade.removerLoja("01.234.567/0001-89"));

        LojaController lc = new LojaController();
        assertEquals(1, lc.listarLojas().size());

        Loja result = lc.listarLojas().getLast();
        assertEquals("Los Gatos Burger", result.getNome());
        assertEquals("12123123000112", result.getCpfCnpj());
    }

    /**
     * Verifica a busca de lojas por cpf/cpnj
     */
    @Test
    void testBuscarLojaPorCpfCnpj() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.adicionarLoja("Kilson Sports",
            "ksp@lepmail.mn",
            "mnbvcxz",
            "03.333.333/0002-33",
            "Centro Comercial ABC, No21");
        facade.adicionarLoja("Los Gatos Burger",
            "burger@ml.cat",
            "senha",
            "12.123.123/0001-12",
            "Centro Comercial ABC, No4");

        Loja result = facade.buscarLojaPorCpfCnpj("03.333.333/0002-33");

        assertEquals("Kilson Sports", result.getNome());
        assertEquals("03333333000233", result.getCpfCnpj());
    }

    @Test
    void testAdicionarProduto() {
        MarketplaceFacade facade = new MarketplaceFacade();
        LojaController lc = new LojaController();
        lc.adicionarLoja("Mercadinho do Josa",
            "jooosa@fmail.com",
            "cocoricó",
            "123.123.123-12",
            "Travessa de Plástico, 300");
        Loja loja = lc.buscarLojaPorCpfCnpj("123.123.123-12");

        facade.adicionarProduto(
            "Mucilon 500g",
            12.85,
            "Alimentos",
            50,
            "Nestlè",
            "Mucilon 500g multicereais",
            loja
        );

        ProdutoController pc = new ProdutoController();
        assertEquals(1, pc.listarProdutos().size());
        assertEquals("Mucilon 500g", pc.listarProdutos().getFirst().getNome());
    }

    @Test
    void testListarProdutos() {
        MarketplaceFacade facade = new MarketplaceFacade();
        LojaController lc = new LojaController();
        lc.adicionarLoja("Mercadinho do Josa",
            "jooosa@fmail.com",
            "cocoricó",
            "123.123.123-12",
            "Travessa de Plástico, 300");
        Loja loja = lc.buscarLojaPorCpfCnpj("123.123.123-12");

        facade.adicionarProduto(
            "Café 250g",
            100.00, // café tá caro
            "Alimentos",
            200,
            "São Braz",
            "Café São Braz tradicional 250g (o mais barato)",
            loja
        );
        facade.adicionarProduto(
            "Filé mignon kg",
            80.00,
            "Alimentos",
            100,
            "Azil Carnes",
            "Filé mignon corte premium - Azil Carnes",
            loja
        );

        ProdutoController pc = new ProdutoController();
        List<Produto> produtos = pc.listarProdutos();
        assertEquals(2, produtos.size());
        assertEquals("Café 250g", produtos.get(0).getNome());
        assertEquals("Filé mignon kg", produtos.get(1).getNome());
    }

    @Test
    void testRemoverProduto() {
        MarketplaceFacade facade = new MarketplaceFacade();
        LojaController lc = new LojaController();
        lc.adicionarLoja("Mercadinho do Josa",
            "jooosa@fmail.com",
            "cocoricó",
            "123.123.123-12",
            "Travessa de Plástico, 300");
        Loja loja = lc.buscarLojaPorCpfCnpj("123.123.123-12");

        facade.adicionarProduto(
            "Café Instantâneo 50g",
            150.00, // café tá caro
            "Alimentos",
            200,
            "São Braz",
            "Café São Braz instantâneo 50g (o mais barato)",
            loja
        );
        facade.adicionarProduto(
            "Skol Pilsen pack",
            80.00,
            "Alimentos",
            100,
            "Skol",
            "Skol lata pack com 15",
            loja
        );
        facade.removerProduto("Café Instantâneo 50g");

        ProdutoController pc = new ProdutoController();
        List<Produto> produtos = pc.listarProdutos();
        assertEquals(1, produtos.size());
        assertEquals("Skol Pilsen pack", produtos.getFirst().getNome());
    }

    @Test
    void testRemoverPorId() {
        MarketplaceFacade facade = new MarketplaceFacade();
        LojaController lc = new LojaController();
        lc.adicionarLoja("Mercadinho do Josa",
            "jooosa@fmail.com",
            "cocoricó",
            "123.123.123-12",
            "Travessa de Plástico, 300");
        Loja loja = lc.buscarLojaPorCpfCnpj("123.123.123-12");

        facade.adicionarProduto(
            "Café Cápsula",
            200.00, // café tá caro
            "Alimentos",
            200,
            "Nescafé",
            "Café cápsula expresso",
            loja
        );
        ProdutoController pc = new ProdutoController();
        List<Produto> produtos = pc.listarProdutos();
        assertEquals(1, produtos.size());
        String id = produtos.getFirst().getId();
        facade.removerPorId(id);

        assertTrue(pc.listarProdutos().isEmpty());
    }

    @Test
    void testListarProdutosPorLoja() {
        MarketplaceFacade facade = new MarketplaceFacade();
        LojaController lc = new LojaController();
        lc.adicionarLoja("Mercadinho do Josa",
            "jooosa@fmail.com",
            "cocoricó",
            "123.123.123-12",
            "Travessa de Plástico, 300");
        lc.adicionarLoja("Conveniência Leno",
            "oleno@hmail.com",
            "miau",
            "321.321.321-21",
            "Travessa de Plástico, 301");
        Loja mercadinho = lc.buscarLojaPorCpfCnpj("123.123.123-12");
        Loja conveniencia = lc.buscarLojaPorCpfCnpj("321.321.321-21");

        facade.adicionarProduto(
            "Café Em Grãos 1Kg",
            1000.00, // café tá caro
            "Alimentos",
            200,
            "Pilão",
            "Café em grãos torrados",
            mercadinho
        );
        facade.adicionarProduto(
            "Óleo lubrificante 20W50",
            49.90, // café tá caro
            "Lubrificantes",
            50,
            "Motul",
            "Óleo Sintético para motores quatro tempos",
            conveniencia
        );
        facade.adicionarProduto(
            "Arroz 1Kg",
            5.00,
            "Alimentos",
            200,
            "Boa Nova",
            "Arroz parbolizado 1Kg",
            mercadinho
        );

        ProdutoController pc = new ProdutoController();
        List<Produto> produtos = pc.listarProdutosPorLoja("12312312312");
        assertEquals(2, produtos.size());
        assertEquals("Café Em Grãos 1Kg", produtos.get(0).getNome());
        assertEquals("Arroz 1Kg", produtos.get(1).getNome());
    }

    @Test
    void testCadastrarComprador() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.cadastrarComprador("Mário Mário", "jklm@nint.com", "yahoo!", "555-555-555.55", "Rainbow Road, 99");

        CompradorController cc = new CompradorController();
        List<Comprador> compradores = cc.listarCompradores();
        assertEquals(1, compradores.size());
        assertEquals("Mário Mário", compradores.getFirst().getNome());
    }

    @Test
    void testListarCompradores() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.cadastrarComprador("Luigi Mário", "nopq@nint.com", "yahoo!", "555-555-555.56", "Rainbow Road, 99");
        facade.cadastrarComprador("Mário Mário", "jklm@nint.com", "yahoo!", "555-555-555.55", "Rainbow Road, 99");

        CompradorController cc = new CompradorController();
        List<Comprador> compradores = cc.listarCompradores();
        assertEquals(2, compradores.size());
        assertEquals("Luigi Mário", compradores.get(0).getNome());
        assertEquals("Mário Mário", compradores.get(1).getNome());
    }

    @Test
    void testRemoverComprador() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.cadastrarComprador("Luigi Mário", "nopq@nint.com", "yahoo!", "555-555-555.56", "Rainbow Road, 99");
        facade.cadastrarComprador("Mário Mário", "jklm@nint.com", "yahoo!", "555-555-555.55", "Rainbow Road, 99");
        boolean removido = facade.removerComprador("55555555556");

        assertTrue(removido);
        CompradorController cc = new CompradorController();
        List<Comprador> compradores = cc.listarCompradores();
        assertEquals(1, compradores.size());
        assertEquals("Mário Mário", compradores.getFirst().getNome());
    }

    @Test
    void testBuscarCompradorPorCpf() {
        MarketplaceFacade facade = new MarketplaceFacade();

        facade.cadastrarComprador("Luigi Mário", "nopq@nint.com", "yahoo!", "555-555-555.56", "Rainbow Road, 99");
        facade.cadastrarComprador("Mário Mário", "jklm@nint.com", "yahoo!", "555-555-555.55", "Rainbow Road, 99");
        Comprador resultado = facade.buscarCompradorPorCpf("55555555555");

        assertEquals("Mário Mário", resultado.getNome());
    }

    @Test
    void testLogoutLoja() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        facade.loginLoja("123.456.789-00", "senha");
        facade.logoutLoja();
        LojaController lc = new LojaController();
        assertNull(lc.getLojaLogada());
    }

    @Test
    void testLogoutComprador() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.logoutComprador();
        CompradorController cc = new CompradorController();
        assertNull(cc.getCompradorLogado());
    }

    @Test
    void testIsCompradorLogado() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        assertFalse(facade.isCompradorLogado());
        facade.loginComprador("111.222.333-44", "senha");
        assertTrue(facade.isCompradorLogado());
    }

    @Test
    void testGetCompradorLogado() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        Comprador logado = facade.getCompradorLogado();
        assertNotNull(logado);
        assertEquals("Comprador Teste", logado.getNome());
    }

    @Test
    void testAdicionarAoCarrinho() {
        MarketplaceFacade facade = new MarketplaceFacade();
        // Cria loja e produto
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        // Cria comprador e faz login
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        boolean adicionado = facade.adicionarAoCarrinho(produto.getId(), 2);
        assertTrue(adicionado);
        assertEquals(2, facade.getCarrinho().get(produto.getId()));
    }

    @Test
    void testRemoverDoCarrinho() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.adicionarAoCarrinho(produto.getId(), 2);
        boolean removido = facade.removerDoCarrinho(produto.getId());
        assertTrue(removido);
        assertFalse(facade.getCarrinho().containsKey(produto.getId()));
    }

    @Test
    void testLimparCarrinho() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.adicionarAoCarrinho(produto.getId(), 2);
        facade.limparCarrinho();
        assertTrue(facade.getCarrinho().isEmpty());
    }

    @Test
    void testGetCarrinho() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.adicionarAoCarrinho(produto.getId(), 2);
        assertEquals(1, facade.getCarrinho().size());
        assertEquals(2, facade.getCarrinho().get(produto.getId()));
    }

    @Test
    void testAlterarQuantidadeCarrinho() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.adicionarAoCarrinho(produto.getId(), 2);
        boolean alterado = facade.alterarQuantidadeCarrinho(produto.getId(), 4);
        assertTrue(alterado);
        assertEquals(4, facade.getCarrinho().get(produto.getId()));
    }

    @Test
    void testLoginLoja() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.loginLoja("123.456.789-00", "senha");
        assertNotNull(loja);
        assertEquals("Loja Teste", loja.getNome());
    }

    @Test
    void testLoginComprador() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        boolean logado = facade.loginComprador("111.222.333-44", "senha");
        assertTrue(logado);
        assertTrue(facade.isCompradorLogado());
    }

    @Test
    void testGetLojaLogada() {
        MarketplaceFacade facade = new MarketplaceFacade();
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        facade.loginLoja("123.456.789-00", "senha");
        Loja loja = facade.getLojaLogada();
        assertNotNull(loja);
        assertEquals("Loja Teste", loja.getNome());
    }

    @Test
    void testFinalizarCompra() {
        MarketplaceFacade facade = new MarketplaceFacade();
        // Cria loja e produto
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        // Cria comprador, login e adiciona ao carrinho
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.adicionarAoCarrinho(produto.getId(), 2);
        // Finaliza compra
        var pedido = facade.finalizarCompra("11122233344");
        assertNotNull(pedido);
        assertEquals(20.0, pedido.getValorTotal(), 0.01);
        assertEquals("11122233344", pedido.getCompradorCpf());
        assertEquals(loja.getCpfCnpj(), pedido.getLoja().getCpfCnpj());
        assertTrue(facade.getCarrinho().isEmpty());
    }

    @Test
    void testListarHistoricoCompras() {
        MarketplaceFacade facade = new MarketplaceFacade();
        // Cria loja e produto
        facade.adicionarLoja("Loja Teste", "loja@teste.com", "senha", "123.456.789-00", "Rua Teste, 1");
        Loja loja = facade.buscarLojaPorCpfCnpj("123.456.789-00");
        facade.adicionarProduto("Produto Teste", 10.0, "Tipo", 5, "Marca", "Descrição", loja);
        Produto produto = facade.listarProdutos().getFirst();
        // Cria comprador, login e adiciona ao carrinho
        facade.cadastrarComprador("Comprador Teste", "comprador@teste.com", "senha", "111.222.333-44", "Rua Teste, 2");
        facade.loginComprador("111.222.333-44", "senha");
        facade.adicionarAoCarrinho(produto.getId(), 2);
        facade.finalizarCompra("11122233344");
        var historico = facade.listarHistoricoCompras("11122233344");
        assertEquals(1, historico.size());
        assertEquals(20.0, historico.getFirst().getValorTotal(), 0.01);
    }
}
