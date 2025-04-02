package com.eng2marketplace.view.input;

import java.util.Scanner;

/**
 * Classe responsável por obter entrada validada de texto a partir do console.
 */
public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Obtém um texto qualquer
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
        return TextGetter.get(this.scanner, regex, 0);
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
        return TextGetter.get(this.scanner, "([A-z0-9_\\.-]+\\@[A-z0-9_-]+\\.[A-z\\.]{2,6})", 0);
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
        if (maxSize < 0)
            throw new IllegalArgumentException("O tamanho máximo é inválido.");
        return TextGetter.get(this.scanner, "(?:[A-zÀ-ú]{2,99} ){0,99}[A-zÀ-ú]{2,99}", maxSize);
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
        return IntRangeGetter.get(this.scanner, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
        return IntRangeGetter.get(this.scanner, min, max);
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
        return DoubleRangeGetter.get(this.scanner, -Double.MAX_VALUE, Double.MAX_VALUE);
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
        return DoubleRangeGetter.get(this.scanner, min, max);
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
        TextGetter getter = new TextGetter(this.scanner, "\\d{11}", 11);
        if (getter.validate()) {
            String value = getter.getValue();
            return String.format(
                "%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11));
        }

        // Tente de novo com outro padrão
        getter.setRegex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        getter.setMaxLength(14);
        return getter.validate() ? getter.getValue() : null;
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
        TextGetter getter = new TextGetter(this.scanner, "\\d{8}(?:0001|0002)\\d{2}", 14);
        if(getter.validate()) {
            String value = getter.getValue();
            return String.format(
                "%s.%s.%s/%s-%s",
                value.substring(0, 2),
                value.substring(2, 5),
                value.substring(5, 8),
                value.substring(8, 12),
                value.substring(12, 14));
        }

        // Tentando com 2o padrao
        getter.setRegex("\\d{2}.\\d{3}.\\d{3}\\/(?:0001|0002)-\\d{2}");
        getter.setMaxLength(18);
        return getter.validate() ? getter.getValue() : null;
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
        TextGetter getter = new TextGetter(this.scanner, "\\d{8}(?:0001|0002)\\d{2}", 14);
        if(getter.validate()) {
            String value = getter.getValue();
            return String.format(
                "%s.%s.%s/%s-%s",
                value.substring(0, 2),
                value.substring(2, 5),
                value.substring(5, 8),
                value.substring(8, 12),
                value.substring(12, 14));
        }

        // Tente 2o padrao de CNPJ
        getter.setRegex("\\d{2}.\\d{3}.\\d{3}\\/(?:0001|0002)-\\d{2}");
        getter.setMaxLength(18);
        if(getter.validate())
            return getter.getValue();

        // Tente primeiro padrão de CPF
        getter.setRegex("\\d{11}");
        getter.setMaxLength(11);
        if (getter.validate()) {
            String value = getter.getValue();
            return String.format(
                "%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11));
        }

        // Tente segundo padrão de CPF
        getter.setRegex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        getter.setMaxLength(14);
        return getter.validate() ? getter.getValue() : null;
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

    public Double askDouble(String prompt, double min, double max, String errorMessage) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println(errorMessage);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    public Integer askInteger(String prompt, int min, int max, String errorMessage) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println(errorMessage);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    public Long askLong(String string, int i, long maxValue, String string2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'askLong'");
    }
}
