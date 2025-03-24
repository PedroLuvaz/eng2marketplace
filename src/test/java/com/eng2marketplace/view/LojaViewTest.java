package com.eng2marketplace.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

public class LojaViewTest {
    private final InputStream is = System.in;
    private final PrintStream os = System.out;
    private final Charset defaultCharset = System.out.charset();
    private ByteArrayOutputStream captureOut;

    /**
     * Reseta a entrada de dados para o padrão e redireciona a saída de dados para uma stream legível
     */
    @BeforeEach
    public void setUp() {
        System.setIn(is);
        this.captureOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.captureOut));
    }

    /**
     * Reseta a saída de dados para o padrão
     */
    @AfterEach
    public void tearDown() {
        System.out.flush();
        System.setOut(os);
    }

    /**
     * Insere o texto como entrada para chamadas de Scanner.next... .
     * Deve ser chamado antes de chamadas Scanner(System.in).
     * @param txt O texto a ser usado como entrada de dados.
     */
    void setInput(String txt) {
        ByteArrayInputStream input = new ByteArrayInputStream(txt.getBytes());
        System.setIn(input);
    }

    /**
     * Obtém a saída de texto capturada
     * @return A saída de texto capturada do console
     */
    String getOutput() {
        return this.captureOut.toString(defaultCharset);
    }

    /**
     * Checa se a opção de voltar é corretamente selecionada
     */
    @Test
    void menuReturnTest() {
        setInput("0\n");

        LojaView lv = new LojaView(null);
        lv.menu();

        String text = getOutput();
        String expected = "\n" +
            "--- Gestão de Lojas ---\n" +
            "1. Adicionar Loja\n" +
            "2. Listar Lojas\n" +
            "3. Remover Loja\n" +
            "0. Voltar\n" +
            "Escolha uma opção: Voltando ao menu principal...\n";
        assertEquals(expected, text);
    }

    /**
     * Verifica se a opção de criar uma nova loja é selecionada corretamente
     */
    @Test
    void menuAddTest() {
        setInput("1\n");

        LojaView lv = new LojaView(null);
        try {
            lv.menu();
        } catch (NoSuchElementException err) {
            // O menu vai lançar essa exceção por falta de entrada
            // O teste não está interessado no resto do processo, só checar se a opção correta foi chamada
            String text = getOutput();
            String expected = "\n" +
                "--- Gestão de Lojas ---\n" +
                "1. Adicionar Loja\n" +
                "2. Listar Lojas\n" +
                "3. Remover Loja\n" +
                "0. Voltar\n" +
                "Escolha uma opção: Nome (entre 2 e 99 letras): ";
            assertEquals(expected, text);
        }
    }

    /**
     * Verifica se a opção de listar lojas é selecionada corretamente
     */
    @Test
    void menuListTest() {
        setInput("2\n");

        LojaView lv = new LojaView(null);
        try {
            lv.menu();
        } catch (NullPointerException err) {
            // O menu vai lançar essa exceção por não haver um arquivo
            // O teste não está interessado no resto do processo, só checar se a opção correta foi chamada
            String text = getOutput();
            String expected = "\n" +
                "--- Gestão de Lojas ---\n" +
                "1. Adicionar Loja\n" +
                "2. Listar Lojas\n" +
                "3. Remover Loja\n" +
                "0. Voltar\n" +
                "Escolha uma opção: ";
            assertEquals(expected, text);
        }
    }

    /**
     * Verifica se a opção de remover loja é selecionada corretamente
     */
    @Test
    void menuRemoveTest() {
        setInput("3\n");

        LojaView lv = new LojaView(null);
        try {
            lv.menu();
        } catch (NoSuchElementException err) {
            // O menu vai lançar essa exceção por falta de entrada
            // O teste não está interessado no resto do processo, só checar se a opção correta foi chamada
            String text = getOutput();
            String expected = "\n" +
                "--- Gestão de Lojas ---\n" +
                "1. Adicionar Loja\n" +
                "2. Listar Lojas\n" +
                "3. Remover Loja\n" +
                "0. Voltar\n" +
                "Escolha uma opção: Informe o CPF/CNPJ da loja a ser removida (somente números ou com ponto/hífen/barra): ";
            assertEquals(expected, text);
        }
    }

    /**
     * Verifica se rejeita uma opção inválida
     */
    @Test
    void menuInvalidTest() {
        setInput("8\n");

        LojaView lv = new LojaView(null);
        try {
            lv.menu();
        } catch (NoSuchElementException err) {
            // O menu vai lançar essa exceção por falta de entrada
            // O teste não está interessado no resto do processo, só checar se a opção correta foi chamada
            String text = getOutput();
            String expected = "\n" +
                "--- Gestão de Lojas ---\n" +
                "1. Adicionar Loja\n" +
                "2. Listar Lojas\n" +
                "3. Remover Loja\n" +
                "0. Voltar\n" +
                "Escolha uma opção: Opção inválida.\n" +
                "\n" +
                "--- Gestão de Lojas ---\n" +
                "1. Adicionar Loja\n" +
                "2. Listar Lojas\n" +
                "3. Remover Loja\n" +
                "0. Voltar\n" +
                "Escolha uma opção: ";
            assertEquals(expected, text);
        }
    }
}
