package com.eng2marketplace.view;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Classe responsável por obter e validar a entrada de texto no console.
 */
public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleInput() {
    }
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

    public String askText(String prompt, String regex, String invalidMsg) {
        String answer;
        do {
            System.out.print(prompt);
            answer = getText(regex);
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public String askMail(String prompt, String invalidMsg) {
        String answer;
        do {
            System.out.print(prompt);
            answer = getMail();
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public String askName(String prompt, int maxSize, String invalidMsg) {
        String answer;
        do {
            System.out.print(prompt);
            answer = getName(maxSize);
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
    }
    /**
     * Obtém um número inteiro.
     * @return Um número ou null se um número inválido for informado.
     */
    public Integer getNumber() {
        try {
            int result = this.scanner.nextInt();
            this.scanner.nextLine(); // scanner bug workaround
            return result;
        } catch (InputMismatchException e) { // número veio troncho
            return null;
        }
    }

    public int askNumber(String prompt, String invalidMsg) {
        Integer answer;
        do {
            System.out.print(prompt);
            answer = getNumber();
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public int askNumber(String prompt, int min, int max, String invalidMsg) {
        Integer answer;
        do {
            System.out.print(prompt);
            answer = getNumber(min, max);
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public double askValue(String prompt, String invalidMsg) {
        Double answer;
        do {
            System.out.print(prompt);
            answer = getValue();
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public double askValue(String prompt, double min, double max, String invalidMsg) {
        Double answer;
        do {
            System.out.print(prompt);
            answer = getValue(min, max);
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public String askCPF(String prompt, String invalidMsg) {
        String answer;
        do {
            System.out.print(prompt);
            answer = getCPF();
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
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

    public String askCNPJ(String prompt, String invalidMsg) {
        String answer;
        do {
            System.out.print(prompt);
            answer = getCNPJ();
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
    }


    /**
     * Obtém um número de identificação (CPF ou CNPJ).
     * Os padrões de CNPJ aceitos são XX.XXX.XXX/000Y-XX, composto de 10 números, pontos, barra e hífen,
     * no qual o número Y é 1 ou 2.
     * Outro padrão de CNPJ aceito é XXXXXXXX000YXX, composto de números, no qual o caractere Y é 1 ou 2.
     * Os padrões de CPF válidos são XXX.XXX.XXX-XX, compostos de números, pontos e hífen,
     * ou XXXXXXXXXXX, composto somente de números
     * @return Um número de CPF ou CNPJ ou null caso não for informado um valor válido
     */
    public String getCPFCNPJ() {
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


    public String askCPFCNPJ(String prompt, String invalidMsg) {
        String answer;
        do {
            System.out.print(prompt);
            answer = getCPFCNPJ();
            if(answer == null)
                System.out.println(invalidMsg);
        } while (answer == null);

        return answer;
    }
}
