package com.eng2marketplace.repository;

import com.eng2marketplace.model.Administrador;
import com.google.gson.reflect.TypeToken;


/**
 * Repositório para gerenciar operações de persistência de dados dos administradores.
 * Os dados são armazenados em um arquivo JSON.
 */
public class AdministradorRepository extends JSONRepository<Administrador> {

    private static final String ARQUIVO_ADMINISTRADORES = "src/main/data/administradores.json";

    /**
     * Construtor. Inicializa o Gson e garante que o arquivo JSON exista.
     */
    public AdministradorRepository() {
        super(ARQUIVO_ADMINISTRADORES, new TypeToken<>() {
        });
    }

    public boolean atualizar(Administrador administrador) {
        return super.atualizar(administrador,
            a -> a.getEmail().equalsIgnoreCase(administrador.getEmail())
        );
    }
}
