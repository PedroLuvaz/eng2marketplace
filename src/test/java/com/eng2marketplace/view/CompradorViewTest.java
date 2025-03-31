package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Comprador;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompradorViewTest {
    private final InputStream is = System.in;
    private final PrintStream os = System.out;
    private final Charset defaultCharset = System.out.charset();
    private ByteArrayOutputStream captureOut;
    private MarketplaceFacade mockFacade;

    @BeforeEach
    public void setUp() {
        System.setIn(is);
        this.captureOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.captureOut));
        this.mockFacade = mock(MarketplaceFacade.class);
    }

    @AfterEach
    public void tearDown() {
        System.out.flush();
        System.setOut(os);
    }

    void setInput(String txt) {
        ByteArrayInputStream input = new ByteArrayInputStream(txt.getBytes());
        System.setIn(input);
    }

    String getOutput() {
        return this.captureOut.toString(defaultCharset);
    }

    @Test
    void menuReturnTest() {
        setInput("0\n");
        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("--- Gestão de Compradores ---"));
        assertTrue(text.contains("1. Cadastrar Comprador"));
        assertTrue(text.contains("2. Listar Compradores"));
        assertTrue(text.contains("3. Buscar Comprador por CPF"));
        assertTrue(text.contains("4. Remover Comprador"));
        assertTrue(text.contains("0. Voltar"));
        assertTrue(text.contains("Voltando ao menu principal..."));
    }

    @Test
    void menuAddTest() {
        setInput("1\nJoão\njoao@teste.com\nsenha123\n123.456.789-09\nRua A\n0\n");
        
        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Nome: "));
        assertTrue(text.contains("Email: "));
        assertTrue(text.contains("Senha: "));
        assertTrue(text.contains("CPF: "));
        assertTrue(text.contains("Endereço: "));
        assertTrue(text.contains("Comprador cadastrado com sucesso!"));
    }

    @Test
    void menuListTest() {
        setInput("2\n0\n");
        when(mockFacade.listarCompradores()).thenReturn(List.of(
            new Comprador("João", "joao@teste.com", "senha", "123.456.789-09", "Rua A")
        ));

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("--- Lista de Compradores ---"));
        assertTrue(text.contains("João - 123.456.789-09 - joao@teste.com"));
    }

    @Test
    void menuListEmptyTest() {
        setInput("2\n0\n");
        when(mockFacade.listarCompradores()).thenReturn(List.of());

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Nenhum comprador cadastrado."));
    }

    @Test
    void menuBuscarPorCpfTest() {
        setInput("3\n123.456.789-09\n0\n");
        when(mockFacade.buscarCompradorPorCpf("123.456.789-09"))
            .thenReturn(new Comprador("João", "joao@teste.com", "senha", "123.456.789-09", "Rua A"));

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Informe o CPF do comprador: "));
        assertTrue(text.contains("--- Dados do Comprador ---"));
        assertTrue(text.contains("Nome: João"));
        assertTrue(text.contains("Email: joao@teste.com"));
        assertTrue(text.contains("CPF: 123.456.789-09"));
        assertTrue(text.contains("Endereço: Rua A"));
    }

    @Test
    void menuBuscarPorCpfNaoEncontradoTest() {
        setInput("3\n000.000.000-00\n0\n");
        when(mockFacade.buscarCompradorPorCpf("000.000.000-00")).thenReturn(null);

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Comprador não encontrado."));
    }

    @Test
    void menuRemoverCompradorTest() {
        setInput("4\n123.456.789-09\n0\n");
        when(mockFacade.removerComprador("123.456.789-09")).thenReturn(true);

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Informe o CPF do comprador a ser removido: "));
        assertTrue(text.contains("Comprador removido com sucesso!"));
    }

    @Test
    void menuRemoverCompradorNaoEncontradoTest() {
        setInput("4\n000.000.000-00\n0\n");
        when(mockFacade.removerComprador("000.000.000-00")).thenReturn(false);

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Comprador não encontrado."));
    }

    @Test
    void menuInvalidOptionTest() {
        setInput("9\n0\n");

        CompradorView cv = new CompradorView(mockFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Opção inválida."));
    }
}