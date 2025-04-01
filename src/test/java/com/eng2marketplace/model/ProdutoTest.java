package com.eng2marketplace.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Teste de unidade do produto.
 */
class ProdutoTest {

    private static final Loja LOJA = new Loja("ABC Variedades", "abc@def.ghi", "12345678", "11.111.111/0001-11", "Avenida Passos, S/N");

    /**
     * Testa atribuir e recuperar o nome do produto
     */
    @Test
    void testNome() {
        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setNome("Vitamina B2");
        // BUG: E se o argumento for null?

        assertEquals("Vitamina B2", prod.getNome());
    }


    /**
     * Testa atribuir e recuperar o valor do produto
     */
    @Test
    void testValor() {
        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setValor(149.90);

        assertEquals(149.9, prod.getValor());
    }

    /**
     * Testa atribuir e recuperar o tipo do produto
     */
    @Test
    void testTipo() {
        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setTipo("00000");

        assertEquals("00000", prod.getTipo());
    }

    /**
     * Testa atribuir e recuperar a quantidade do produto
     */
    @Test
    void testQuantidade() {
        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setQuantidade(10);

        assertEquals(10, prod.getQuantidade());
    }

    /**
     * Testa atribuir e recuperar a marca do produto
     */
    @Test
    void testMarca() {
        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setMarca("NutriSal");

        assertEquals("NutriSal", prod.getMarca());
    }

    /**
     * Testa atribuir e recuperar a loja do produto
     */
    @Test
    void testDescricao() {
        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setDescricao("Multi-vitamínico");

        assertEquals("Multi-vitamínico", prod.getDescricao());
    }

    /**
     * Testa atribuir e recuperar a descrição do produto
     */
    @Test
    void testLoja() {
        Loja loja = new Loja("ABC Variedades", "abc@def.ghi", "12345678", "11.111.111/0001-11", "Avenida Passos, S/N");

        Produto prod = new Produto("B2", 150.0, "12345", 1, "Sal", "Sem descrição", LOJA);
        prod.setLoja(loja);

        assertEquals(loja, prod.getLoja());
    }

    /**
     * Testa representação da loja em forma textual.
     */
}
