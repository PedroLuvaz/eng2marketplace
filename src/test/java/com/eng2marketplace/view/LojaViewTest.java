package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

public class LojaViewTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream captureOut;

    private MarketplaceFacade facadeMock;

    /**
     * Reseta a entrada de dados para o padrão e redireciona a saída de dados para uma stream legível
     */
    @BeforeEach
    public void setUp() {
        System.setIn(originalSystemIn);  // Restaura a entrada original
        this.captureOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.captureOut));  // Captura a saída do sistema

        // Criando o mock do MarketplaceFacade para evitar NullPointerException
        facadeMock = mock(MarketplaceFacade.class);
    }

    /**
     * Reseta a saída de dados para o padrão
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalSystemOut);  // Restaura a saída original
        System.setIn(originalSystemIn);    // Restaura a entrada original
    }

    /**
     * Insere o texto como entrada para chamadas de Scanner.next...
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
        return this.captureOut.toString(Charset.defaultCharset());
    }

    /**
     * Checa se a opção de voltar é corretamente selecionada
     */
    @Test
    void menuReturnTest() {
        setInput("0\n");

        LojaView lv = new LojaView(facadeMock);  // Passando o mock para a view
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
        setInput("1\nLoja Teste\n");

        LojaView lv = new LojaView(facadeMock);
        lv.menu();

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

    /**
     * Verifica se a opção de listar lojas é selecionada corretamente
     */
    @Test
    void menuListTest() {
        setInput("2\n");

        LojaView lv = new LojaView(facadeMock);
        lv.menu();

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

    /**
     * Verifica se a opção de remover loja é selecionada corretamente
     */
    @Test
    void menuRemoveTest() {
        setInput("3\n");

        LojaView lv = new LojaView(facadeMock);
        lv.menu();

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

    /**
     * Verifica se rejeita uma opção inválida
     */
    @Test
    void menuInvalidTest() {
        setInput("8\n");

        LojaView lv = new LojaView(facadeMock);
        lv.menu();

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

    /**
     * Verifica se a opção de criar loja lida com entradas inválidas corretamente
     */
    @Test
    void menuAddInvalidNameTest() {
        setInput("1\nA\n");

        LojaView lv = new LojaView(facadeMock);
        lv.menu();

        String text = getOutput();
        String expected = "\n" +
            "--- Gestão de Lojas ---\n" +
            "1. Adicionar Loja\n" +
            "2. Listar Lojas\n" +
            "3. Remover Loja\n" +
            "0. Voltar\n" +
            "Escolha uma opção: Nome (entre 2 e 99 letras): Nome inválido! Tente novamente: ";
        assertEquals(expected, text);
    }
}