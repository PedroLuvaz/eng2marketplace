package com.eng2marketplace.repository;

import com.eng2marketplace.model.Comprador;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para gerenciar operações de persistência de dados de compradores.
 * Os dados são armazenados em um arquivo JSON.
 */
public class CompradorRepository extends JSONRepository<Comprador> {

    private static final String ARQUIVO_COMPRADORES = "src/main/data/compradores.json"; // Caminho do arquivo JSON

    /**
     * Construtor da classe. Inicializa o Gson e garante que o arquivo JSON exista.
     */
    public CompradorRepository() {
        super(ARQUIVO_COMPRADORES, new TypeToken<>(){});
    }

    /**
     * Remove um comprador pelo CPF.
     *
     * @param cpf CPF do comprador a ser removido.
     * @return true se o comprador foi removido, false caso contrário.
     */
    public boolean remover(String cpf) {
        return super.remover(comprador -> comprador.getCpf().equals(cpf));
    }

    /**
     * Busca um comprador pelo CPF.
     *
     * @param cpf CPF do comprador a ser buscado.
     * @return Optional contendo o comprador, se encontrado.
     */
    public Optional<Comprador> buscarPorCpf(String cpf) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", ""); // Remove formatação do CPF
        return super.buscar(comprador -> {
            String cpfComprador = comprador.getCpf().replaceAll("[^0-9]", "");
            return cpfComprador.equals(cpfNumerico);
        });
    }

    /**
     * Atualiza os dados de um comprador existente.
     *
     * @param comprador Comprador com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizar(Comprador comprador) {
        return super.atualizar(comprador, c -> c.getCpf().equals(comprador.getCpf())); // Remove o comprador antigo
    }
}
