package com.eng2marketplace.repository;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável por gerenciar os dados de produtos.
 * Os dados são armazenados em um arquivo JSON.
 */
public class ProdutoRepository extends JSONRepository<Produto> {

    private static final String ARQUIVO_PRODUTOS = "src/main/data/produtos.json"; // Caminho do arquivo JSON

    /**
     * Construtor que inicializa o repositório de produtos.
     *
     * @param lojaRepository Repositório de lojas para reconstruir relações.
     */
    public ProdutoRepository(LojaRepository lojaRepository) {
        super(ARQUIVO_PRODUTOS,
            new TypeToken<>(){},
            new GsonBuilder()
                .registerTypeAdapter(Loja.class, new LojaAdapter(lojaRepository))
        );
    }

    /**
     * Lista todos os produtos de uma loja específica.
     *
     * @param cpfCnpjLoja CPF/CNPJ da loja.
     * @return Lista de produtos da loja.
     */
    public List<Produto> listarPorLoja(String cpfCnpjLoja) {
        return listar().stream()
            .filter(p -> p.getLoja().getCpfCnpj().equals(cpfCnpjLoja))
            .toList();
    }

    /**
     * Remove um produto pelo nome.
     *
     * @param nome Nome do produto.
     * @return True se o produto foi removido, false caso contrário.
     */
    public boolean remover(String nome) {
        return remover(p -> p.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Busca um produto pelo nome.
     *
     * @param nome Nome do produto.
     * @return Produto encontrado, se existir.
     */
    public Optional<Produto> buscarPorNome(String nome) {
        return super.buscar(p -> p.getNome().equalsIgnoreCase(nome));
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id ID do produto.
     * @return Produto encontrado, se existir.
     */
    public Optional<Produto> buscarPorId(String id) {
        return super.buscar(p -> p.getId().equals(id));
    }

    /**
     * Remove um produto pelo ID.
     *
     * @param id ID do produto.
     * @return True se o produto foi removido, false caso contrário.
     */
    public boolean removerPorId(String id) {
        return super.remover(p -> p.getId().equals(id));
    }


    public static class LojaAdapter implements JsonSerializer<Loja>, JsonDeserializer<Loja> {
        LojaRepository lojaRepository;

        public LojaAdapter(LojaRepository lojaRepository) {
            this.lojaRepository = lojaRepository;
        }

        @Override
        public JsonElement serialize(Loja src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getCpfCnpj());
        }

        @Override
        public Loja deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            Optional<Loja> resultado = lojaRepository.buscarPorCpfCnpj(json.getAsString());
            return resultado.orElse(null);
        }
    }
}
