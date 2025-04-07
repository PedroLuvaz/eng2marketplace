package com.eng2marketplace.repository;

import com.eng2marketplace.model.Produto;
import com.eng2marketplace.model.Loja;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável por gerenciar os dados de produtos.
 * Os dados são armazenados em um arquivo JSON.
 */
public class ProdutoRepository {

    private static final String ARQUIVO_PRODUTOS = "src/main/data/produtos.json"; // Caminho do arquivo JSON
    private final LojaRepository lojaRepository; // Dependência para gerenciar lojas
    private final Gson gson; // Instância do Gson para manipulação de JSON
    private final Type listType = new TypeToken<ArrayList<Produto>>() {}.getType(); // Tipo genérico para lista de produtos

    /**
     * Construtor que inicializa o repositório de produtos.
     *
     * @param lojaRepository Repositório de lojas para reconstruir relações.
     */
    public ProdutoRepository(LojaRepository lojaRepository) {
        this.lojaRepository = lojaRepository;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        criarArquivoSeNaoExistir();
    }

    /**
     * Cria o arquivo JSON de produtos caso ele não exista.
     */
    private void criarArquivoSeNaoExistir() {
        try {
            File file = new File(ARQUIVO_PRODUTOS);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Cria os diretórios necessários
                salvarLista(new ArrayList<>()); // Inicializa o arquivo com uma lista vazia
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Salva um novo produto no arquivo JSON.
     *
     * @param produto Produto a ser salvo.
     */
    public void salvar(Produto produto) {
        List<Produto> produtos = listar();
        produtos.add(produto);
        salvarLista(produtos);
    }

    /**
     * Lista todos os produtos armazenados no arquivo JSON.
     *
     * @return Lista de produtos.
     */
    public List<Produto> listar() {
        try (FileReader reader = new FileReader(ARQUIVO_PRODUTOS)) {
            List<Produto> produtos = gson.fromJson(reader, listType);
            if (produtos == null) {
                return new ArrayList<>();
            }

            // Reconstrói a relação entre produtos e lojas
            for (Produto produto : produtos) {
                String cpfCnpjLoja = produto.getLoja().getCpfCnpj();
                Optional<Loja> loja = lojaRepository.listar().stream()
                    .filter(l -> l.getCpfCnpj().equals(cpfCnpjLoja))
                    .findFirst();

                loja.ifPresent(produto::setLoja);
            }

            return produtos;
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
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
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(p -> p.getNome().equalsIgnoreCase(nome));
        if (removido) {
            salvarLista(produtos);
        }
        return removido;
    }

    /**
     * Salva a lista de produtos no arquivo JSON.
     *
     * @param produtos Lista de produtos a ser salva.
     */
    private void salvarLista(List<Produto> produtos) {
        try (FileWriter writer = new FileWriter(ARQUIVO_PRODUTOS)) {
            gson.toJson(produtos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Remove todos os produtos do arquivo JSON.
     */
    public void limparTodos() {
        salvarLista(new ArrayList<>());
    }

    /**
     * Busca um produto pelo nome.
     *
     * @param nome Nome do produto.
     * @return Produto encontrado, se existir.
     */
    public Optional<Produto> buscarPorNome(String nome) {
        return listar().stream()
            .filter(p -> p.getNome().equalsIgnoreCase(nome))
            .findFirst();
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id ID do produto.
     * @return Produto encontrado, se existir.
     */
    public Optional<Produto> buscarPorId(String id) {
        return listar().stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();
    }

    /**
     * Remove um produto pelo ID.
     *
     * @param id ID do produto.
     * @return True se o produto foi removido, false caso contrário.
     */
    public boolean removerPorId(String id) {
        List<Produto> produtos = listar();
        boolean removido = produtos.removeIf(p -> p.getId().equals(id));
        if (removido) {
            salvarLista(produtos);
        }
        return removido;
    }
}