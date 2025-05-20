package com.eng2marketplace.repository;

import com.eng2marketplace.model.Avaliacao;
import com.google.gson.reflect.TypeToken;

/**
 * Repositório para gerenciar operações de persistência de dados das avaliacoes de produtos e vendedores.
 * Os dados são armazenados em um arquivo JSON.
 */
public class AvaliacaoRepository extends JSONRepository<Avaliacao> {

    private static final String ARQUIVO_AVALIACOES = "src/main/data/avaliacoes.json";

    /**
     * Construtor. Inicializa o Gson e garante que o arquivo JSON exista.
     */

    public AvaliacaoRepository() {super(ARQUIVO_AVALIACOES, new TypeToken<>(){});
    }
}
