package com.eng2marketplace.view;

import com.eng2marketplace.view.input.ConsoleInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Teste de unidade de entrada de dados
 */
public class ConsoleInputTest {
    private final InputStream is = System.in;

    /**
     * Reseta a entrada de dados para a entrada padrão
     */
    @BeforeEach
    public void setUp() {
        System.setIn(is);
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

    //--| Texto |-------------------------------------------------------------------------------------------------------

    /**
     * Verifica se obtém um texto qualquer.
     */
    @Test
    void testTextOk() {
        String text = "Sample Text";
        setInput(text);

        ConsoleInput io = new ConsoleInput();
        String textGot = io.getText();

        assertEquals(text, textGot);
    }

    /**
     * Verifica se obtém um texto qualquer que segue uma expressão regular.
     */
    @Test
    void testTextRegexOk() {
        String date = "01/01/2001";
        setInput(date);

        ConsoleInput io = new ConsoleInput();
        String dateText = io.getText("[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}"); // Data DD/MM/AAAA. Não considera dia nem mês

        assertEquals(date, dateText);
    }

    /**
     * Verifica se rejeita um texto que não segue uma expressão regular.
     */
    @Test
    void testTextRegexInvalid() {
        String date = "a";
        setInput(date);

        ConsoleInput io = new ConsoleInput();
        String dateText = io.getText("[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}");

        assertNull(dateText);
    }

    //--| Email |-------------------------------------------------------------------------------------------------------

    /**
     * Verifica se um email válido passa o teste.
     */
    @Test
    void testMailOk() {
        String mail = "ecks@gmail.com";
        setInput(mail);

        ConsoleInput io = new ConsoleInput();
        String parsedMail = io.getMail();

        assertEquals(mail, parsedMail);
    }

    /**
     * Verifica se rejeita um email incompleto.
     */
    @Test
    void testMailIncomplete() {
        String mail = "a@b";
        setInput(mail);

        ConsoleInput io = new ConsoleInput();
        String parsedMail = io.getMail();

        assertNull(parsedMail);
    }

    /**
     * Verifica se rejeita um email com caracteres inválidos.
     */
    @Test
    void testMailInvalidChars() {
        String mail = "pepsi@epson coca/cola+brother";
        setInput(mail);

        ConsoleInput io = new ConsoleInput();
        String parsedMail = io.getMail();

        assertNull(parsedMail);
    }

    /**
     * Verifica se rejeita um email vazio.
     */
    @Test
    void testMailEmpty() {
        String mail = "\n";
        setInput(mail);

        ConsoleInput io = new ConsoleInput();
        String parsedMail = io.getMail();

        assertNull(parsedMail);
    }

    //--| Nomes |-------------------------------------------------------------------------------------------------------

    /**
     * Verifica se é possível informar um nome.
     */
    @Test
    void testNameOk() {
        String name = "Michael Jackson Araújo";
        setInput(name);

        ConsoleInput io = new ConsoleInput();
        String parsedName = io.getName(50);

        assertEquals(name, parsedName);
    }

    /**
     * Verifica se é possível informar um nome com o mínimo de caracteres .
     */
    @Test
    void testNameOkMinimumLength() {
        String name = "Lu";
        setInput(name);

        ConsoleInput io = new ConsoleInput();
        String parsedName = io.getName(50);

        assertEquals(name, parsedName);
    }

    /**
     * Verifica se rejeita um nome com mais caracteres que o permitido.
     */
    @Test
    void testNameOverMaximumLength() {
        String name = "Maria de Jesus Conceição Pinto Sousa do Nascimento Ferreira Souto Lira Santos Targino";
        setInput(name);

        ConsoleInput io = new ConsoleInput();
        String parsedName = io.getName(50);

        assertNull(parsedName);
    }

    /**
     * Verifica se rejeita um nome com caracteres inválidos.
     */
    @Test
    void testNameInvalidChars() {
        String name = "Ronaldinho? Gaúcho?";
        setInput(name);

        ConsoleInput io = new ConsoleInput();
        String parsedName = io.getName(50);

        assertNull(parsedName);
    }

    /**
     * Verifica se rejeita um nome contendo números.
     */
    @Test
    void testNameInvalidNumbers() {
        String name = "José da Silva 2o";
        setInput(name);

        ConsoleInput io = new ConsoleInput();
        String parsedName = io.getName(50);

        assertNull(parsedName);
    }

    //--| Números inteiros |--------------------------------------------------------------------------------------------

    /**
     * Verifica se é possível informar um número inteiro.
     */
    @Test
    void testNumberOk() {
        String number = "7\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber();

        assertEquals(7, parsedNumber);
    }

    /**
     * Verifica se é possível informar um número inteiro em um intervalo.
     */
    @Test
    void testNumberOkInRange() {
        String number = "7\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber(0, 50);

        assertEquals(7, parsedNumber);
    }
    /**
     * Verifica se é possível informar um número inteiro negativo.
     */
    @Test
    void testNumberOkNeg() {
        String number = "-7\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber();

        assertEquals(-7, parsedNumber);
    }

    /**
     * Verifica se é possível informar um número inteiro no limite máximo do intervalo.
     */
    @Test
    void testNumberOkMax() {
        String number = "50\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber(0, 50);

        assertEquals(50, parsedNumber);
    }

    /**
     * Verifica se é possível informar um número inteiro no limite mínimo do intervalo.
     */
    @Test
    void testNumberOkMin() {
        String number = "0\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber(0, 50);

        assertEquals(0, parsedNumber);
    }

    /**
     * Verifica se rejeita caracteres inválidos como número inteiro.
     */
    @Test
    void testNumberInvalid() {
        String number = "7k\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber();

        assertNull(parsedNumber);
    }

    /**
     * Verifica se rejeita número inteiro acima do limite.
     */
    @Test
    void testNumberOverMax() {
        String number = "51\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber(0, 50);

        assertNull(parsedNumber);
    }

    /**
     * Verifica se rejeita número inteiro acima do limite.
     */
    @Test
    void testNumberUnderMin() {
        String number = "-1\n";
        setInput(number);

        ConsoleInput io = new ConsoleInput();
        Integer parsedNumber = io.getNumber(0, 50);

        assertNull(parsedNumber);
    }

    //--| Números de ponto flutuante |----------------------------------------------------------------------------------

    /**
     * Verifica se é possível informar um número de ponto flutuante.
     */
    @Test
    void testValueOk() {
        String value = "1.50";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue();

        assertEquals(1.5, parsedValue);
    }

    /**
     * Verifica se é possível informar um número de ponto flutuante dentro de um intervalo.
     */
    @Test
    void testValueOkInRange() {
        String value = "1.50";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue(0, 50);

        assertEquals(1.5, parsedValue);
    }

    /**
     * Verifica se é possível informar um número de ponto flutuante negativo.
     */
    @Test
    void testValueOkNeg() {
        String value = "-5.00";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue();

        assertEquals(-5.0, parsedValue);
    }

    /**
     * Verifica se é possível informar um número de ponto flutuante máximo.
     */
    @Test
    void testValueOkMax() {
        String value = "50.15";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue(0, 50.15);

        assertEquals(50.15, parsedValue);
    }

    /**
     * Verifica se é possível informar um número de ponto flutuante mínimo.
     */
    @Test
    void testValueOkMin() {
        String value = "0";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue(0, 50);

        assertEquals(0, parsedValue);
    }

    /**
     * Verifica se rejeita caracteres inválidos como número de ponto flutuante.
     */
    @Test
    void testValueInvalid() {
        String value = "7n31";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue();

        assertNull(parsedValue);
    }

    /**
     * Verifica se rejeita número de ponto flutuante acima do limite.
     */
    @Test
    void testValueOverMax() {
        String value = "10.01";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue(0, 10);

        assertNull(parsedValue);
    }

    /**
     * Verifica se rejeita número de ponto flutuante acima do limite.
     */
    @Test
    void testValueUnderMin() {
        String value = "-0.01";
        setInput(value);

        ConsoleInput io = new ConsoleInput();
        Double parsedValue = io.getValue(0, 50);

        assertNull(parsedValue);
    }

    //--| CPF |---------------------------------------------------------------------------------------------------------

    /**
     * Verifica se um CPF válido passa o teste.
     */
    @Test
    void testCPFOk() {
        String doc = "074.981.394-55";
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCPF();

        assertEquals(doc, parsedDoc);
    }

    /**
     * Verifica se um CPF alternativo passa o teste.
     */
    @Test
    void testCPFAltOk() {
        String doc = "07498139455";
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCPF();

        assertEquals("074.981.394-55", parsedDoc);
    }

    /**
     * Verifica se rejeita CPF com caracteres inválidos.
     */
    @Test
    void testCPFWrong() {
        String doc = "a";
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCPF();

        assertNull(parsedDoc);
    }

    /**
     * Verifica se rejeita CPF com caracteres inválidos.
     */
    @Test
    void testCPFInvalidChars() {
        String doc = "074.981,394-55"; // vírgula antes do 394-55
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCPF();

        assertNull(parsedDoc);
    }

    //--| CNPJ |--------------------------------------------------------------------------------------------------------

    /**
     * Verifica se um CNPJ válido passa o teste.
     */
    @Test
    void testCNPJOk() {
        String doc = "12.671.814/0001-37";
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCNPJ();

        assertEquals(doc, parsedDoc);
    }

    /**
     * Verifica se um CNPJ alternativo passa o teste.
     */
    @Test
    void testCNPJAltOk() {
        String doc = "12671814000137";
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCNPJ();

        assertEquals("12.671.814/0001-37", parsedDoc);
    }

    /**
     * Verifica se rejeita CNPJ com caracteres inválidos.
     */
    @Test
    void testCNPJWrong() {
        String doc = "b";
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCNPJ();

        assertNull(parsedDoc);
    }

    /**
     * Verifica se rejeita CNPJ com caracteres inválidos.
     */
    @Test
    void testCNPJInvalidChars() {
        String doc = "12.671.814\\0001-37"; // contra-barra antes do 0001-37
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCNPJ();

        assertNull(parsedDoc);
    }

    /**
     * Verifica se rejeita CNPJ com dígito errado.
     */
    @Test
    void testCNPJInvalidDigits() {
        String doc = "12.671.814/0008-37"; // /0008-37 não deve ser aceito
        setInput(doc);

        ConsoleInput io = new ConsoleInput();
        String parsedDoc = io.getCNPJ();

        assertNull(parsedDoc);
    }
}
