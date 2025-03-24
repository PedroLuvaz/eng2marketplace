package com.eng2marketplace.view.input;

import java.util.InputMismatchException;
import java.util.Scanner;

public class IntRangeGetter implements InputValidator<Integer> {
    private boolean isInvalid = false;
    private int value;
    private final int min;
    private final int max;

    public IntRangeGetter(Scanner scanner, int min, int max) {
        try {
            this.value = scanner.nextInt();
            scanner.nextLine(); // Bug do java. Adicione '\n' aos testes
        } catch (InputMismatchException e) {
            this.isInvalid = true;
        }

        this.min = min;
        this.max = max;
    }

    public boolean validate() {
        if(this.isInvalid)
            return false;
        return value >= min && value <= max;
    }

    public Integer getValue() {
        return this.value;
    }

    public static Integer get(Scanner scanner, int min, int max) {
        IntRangeGetter getter = new IntRangeGetter(scanner, min, max);
        return getter.validate() ? getter.value: null;
    }
}
