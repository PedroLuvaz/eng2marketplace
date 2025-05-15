package com.eng2marketplace.model;

public class Avaliacao {
    private final String compradorCpf;
    private final int nota; // 1 a 5
    private final String comentario;

    public Avaliacao(String compradorCpf, int nota, String comentario) {
        if (nota < 1 || nota > 5) throw new IllegalArgumentException("Nota deve ser de 1 a 5");
        this.compradorCpf = compradorCpf;
        this.nota = nota;
        this.comentario = comentario;
    }

    public int getNota() { return nota; }
    public String getComentario() { return comentario; }
    public String getCompradorCpf() { return compradorCpf; }
}
