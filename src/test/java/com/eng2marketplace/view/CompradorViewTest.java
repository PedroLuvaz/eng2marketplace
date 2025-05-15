package com.eng2marketplace.view;

import static org.junit.jupiter.api.Assertions.*;
import com.eng2marketplace.Facade.MarketplaceFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class CompradorViewTest {
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream captureOut;
    private MarketplaceFacade marketplaceFacade;

    @BeforeEach
    public void setUp() {
        captureOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captureOut, true, StandardCharsets.UTF_8));
        marketplaceFacade = new MarketplaceFacade();
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    private void setInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
    }

    private String getOutput() {
        return captureOut.toString(StandardCharsets.UTF_8);
    }

    @Test
    void menuReturnTest() {
        setInput("0\n");
        CompradorView cv = new CompradorView(marketplaceFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("--- Menu do Comprador ---"));
        assertTrue(text.contains("1. Logout"));
        assertTrue(text.contains("2. Menu do Carrinho"));
        assertTrue(text.contains("3. Finalizar Compra"));
        assertTrue(text.contains("4. Ver Histórico de Pedidos"));
        assertTrue(text.contains("0. Voltar"));
        assertTrue(text.contains("Voltando ao menu principal..."));
    }

    @Test
    void menuInvalidOptionTest() {
        setInput("9\n0\n");

        CompradorView cv = new CompradorView(marketplaceFacade);
        cv.menu();

        String text = getOutput();
        assertTrue(text.contains("Opção inválida."));
    }
}
