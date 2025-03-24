package com.eng2marketplace.view.input;

public interface InputValidator<T> {
    public boolean validate();
    public T getValue();
}
