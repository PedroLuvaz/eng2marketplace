package com.eng2marketplace.view.input;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DoubleRangeGetter implements InputValidator<Double> {
    private boolean isInvalid = false;
    private double value;
    private final double min;
    private final double max;

    public DoubleRangeGetter(Scanner scanner, double min, double max) {
        try {
            this.value = scanner.nextDouble();
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

    public Double getValue() {
        return this.value;
    }

    public static Double get(Scanner scanner, double min, double max) {
        DoubleRangeGetter getter = new DoubleRangeGetter(scanner, min, max);
        return getter.validate() ? getter.value : null;
    }
}
