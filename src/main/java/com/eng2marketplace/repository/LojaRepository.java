package com.eng2marketplace.repository;

import com.eng2marketplace.model.Loja;
import com.google.gson.reflect.TypeToken;

import java.util.Optional;

/**
 * Repositório responsável por gerenciar as operações de persistência de lojas.
 */
public class LojaRepository extends JSONRepository<Loja> {

    private static final String ARQUIVO_LOJAS = "src/main/data/lojas.json"; // Caminho do arquivo JSON

    /**
     * Construtor que inicializa o Gson e garante que o arquivo JSON exista.
     */
    public LojaRepository() {
        super(ARQUIVO_LOJAS, new TypeToken<>(){});
    }

    /**
     * Remove uma loja com base no CPF/CNPJ.
     *
     * @param cpfCnpj CPF ou CNPJ da loja a ser removida
     * @return true se a loja foi removida, false caso contrário
     */
    public boolean remover(String cpfCnpj) {
        return super.remover(loja -> loja.getCpfCnpj().equals(cpfCnpj));
    }

    /**
     * Busca uma loja pelo CPF/CNPJ.
     *
     * @param cpfCnpj CPF ou CNPJ da loja
     * @return Optional contendo a loja, se encontrada
     */
    public Optional<Loja> buscarPorCpfCnpj(String cpfCnpj) {
        // Remove formatação para comparação
        String cpfCnpjNumerico = cpfCnpj.replaceAll("[^0-9]", "");
        return super.buscar(loja -> {
            String docLoja = loja.getCpfCnpj().replaceAll("[^0-9]", "");
            return docLoja.equals(cpfCnpjNumerico);
        });
    }

    /**
     * Atualiza os dados de uma loja existente.
     *
     * @param loja Loja com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Loja loja) {
        return super.atualizar(loja, l -> l.getCpfCnpj().equals(loja.getCpfCnpj()));
    }
}
