package com.eng2marketplace.view.input;

import java.util.Scanner;
import java.util.regex.Pattern;

public class TextGetter implements InputValidator<String> {
    private final String value;
    private String regex;
    private int maxLength;

    public TextGetter(Scanner scanner, String regex, int maxLength) {
        this.value = scanner.nextLine();
        this.regex = regex;
        this.maxLength = maxLength;
    }

    public boolean validate() {
        if (maxLength > 0)
            if(this.value.length() > maxLength)
                return false;
        return Pattern.matches(regex, value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public static String get(Scanner scanner, String regex, int maxLength) {
        TextGetter getter = new TextGetter(scanner, regex, maxLength);
        return getter.validate() ? getter.value: null;
    }
}
