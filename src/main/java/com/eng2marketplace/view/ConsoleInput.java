package com.eng2marketplace.view;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Classe responsável por obter e validar a entrada de texto no console.
 */
public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Obtém um texto qualquer com pelo menos um caractere.
     * O texto deve conter no mínimo um caractere.
     * @return Um texto ou null se um texto inválido for informado
     */
    public String getText() {
        return this.scanner.nextLine();
    }

    /**
     * Obtém um texto qualquer que satisfaz uma expressão regular.
     * @param regex Uma expressão regular que será aplicada ao texto
     * @return Um texto ou null se o texto não satisfazer a expressão regular
     */
    public String getText(String regex) {
        String input = this.scanner.nextLine();
        if(Pattern.matches(regex, input))
            return input;
        return null;
    }

    /**
     * Obtém um e-mail digitado pelo usuário no padrão aaa@bbb.ccc
     * Os caracteres aceitos antes da arroba são alfanuméricos, ponto e underscore e hífen.
     * Depois da arroba os caracteres aceitos são alfanuméricos, underscore e hífen.
     * Depois do ponto os caracteres devem ser alfanuméricos.
     * @return Um endereço de email válido ou null se os caracteres forem inválidos.
     */
    public String getMail() {
        String input = this.scanner.nextLine();
        if(Pattern.matches("([A-z0-9_\\.-]+\\@[A-z0-9_-]+\\.[A-z\\.]{2,6})", input))
            return input;
        return null;
    }

    /**
     * Obtém um nome completo.
     * Cada nome pode conter espaços e letras, mas o nome completo não deve terminar com espaço.
     * O nome deve conter no mínimo dois caracteres.
     * @param maxSize O tamanho máximo do nome, no mínimo 2 caracteres.
     * @return Um nome de pessoa ou null se um nome inválido for informado
     */
    public String getName(int maxSize) {
        if (maxSize <= 0)
            throw new IllegalArgumentException("O tamanho máximo é inválido.");

        String input = this.scanner.nextLine();
        if(!Pattern.matches("(?:[A-zÀ-ú]{2,99} ){0,99}[A-zÀ-ú]{2,99}", input))
            return null;
        if(input.length() > maxSize)
            return null;
        return input;
    }

    /**
     * Obtém um número inteiro.
     * @return Um número ou null se um número inválido for informado.
     */
    public Integer getNumber() {
        try {
            return this.scanner.nextInt();
        } catch (InputMismatchException e) { // número veio troncho
            return null;
        }
    }

    /**
     * Obtém um número inteiro dentro de um intervalo (inclusivo).
     * @param min O valor mínimo
     * @param max O valor máximo
     * @return Um número ou null se nenhum número válido for informado.
     */
    public Integer getNumber(int min, int max) {
        if(min > max)
            throw new IllegalArgumentException("O mínimo deve ser menor ou igual ao máximo!");
        int input = this.getNumber();
        if(input < min || input > max)
            return null;
        return input;
    }

    /**
     * Obtém um número fracionário.
     * @return Um número ou null se nenhum número válido for informado
     */
    public Double getValue() {
        try {
            return this.scanner.nextDouble();
        } catch (InputMismatchException e) { // número veio troncho
            return null;
        }
    }

    /**
     * Obtém um número fracionário dentro de um intervalo (inclusivo).
     * @param min O valor mínimo
     * @param max O valor máximo
     * @return Um número ou null se nenhum número válido for informado
     */
    public Double getValue(double min, double max) {
        if(min > max)
            throw new IllegalArgumentException("O mínimo deve ser menor ou igual ao máximo!");
        double input = this.getValue();
        if(input < min || input > max)
            return null;
        return input;
    }

    /**
     * Obtém um número de CPF.
     * Os padrões válidos são XXX.XXX.XXX-XX, composto de números, pontos e hífen,
     * ou XXXXXXXXXXX, composto somente de números
     * @return Um número de CPF ou null caso não for informado um valor válido
     */
    public String getCPF() {
        String input = this.scanner.nextLine();
        if(Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", input))
            return input;
        if(Pattern.matches("\\d{11}", input)) {
            return String.format(
                "%s.%s.%s-%s",
                input.substring(0, 3),
                input.substring(3, 6),
                input.substring(6, 9),
                input.substring(9, 11));
        }
        return null;
    }

    /**
     * Obtém um número de CNPJ.
     * Os padrões aceitos são XX.XXX.XXX/000Y-XX, composto de 10 números, pontos, barra e hífen,
     * no qual o número Y é 1 ou 2.
     * O segundo padrão aceito é XXXXXXXX000YXX, composto de números, no qual o caractere Y é 1 ou 2.
     * @return Um número de CNPJ ou null caso não for informado um valor válido
     */
    public String getCNPJ() {
        String input = this.scanner.nextLine();
        if(Pattern.matches("\\d{2}.\\d{3}.\\d{3}\\/(?:0001|0002)-\\d{2}", input))
            return input;
        if(Pattern.matches("\\d{8}(?:0001|0002)\\d{2}", input)){
            return String.format(
                "%s.%s.%s/%s-%s",
                input.substring(0, 2),
                input.substring(2, 5),
                input.substring(5, 8),
                input.substring(8, 12),
                input.substring(12, 14));
        }
        return null;
    }
}
