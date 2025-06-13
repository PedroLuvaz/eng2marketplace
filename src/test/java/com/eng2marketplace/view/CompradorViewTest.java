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

}
