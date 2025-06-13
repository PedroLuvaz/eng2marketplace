package com.eng2marketplace.controller;

import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.repository.CompradorRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos compradores.
 */
public class CompradorController {
    private final CompradorRepository compradorRepository;
    private Comprador compradorLogado; // Referência ao comprador atualmente logado
    private int tentativasLogin; // Contador de tentativas de login

    /**
     * Construtor da classe CompradorController.
     * Inicializa o repositório e define o comprador logado como null.
     */
    public CompradorController() {
        this.compradorRepository = new CompradorRepository();
        this.compradorLogado = null;
        this.tentativasLogin = 0;
    }

    /**
     * Adiciona um novo comprador ao sistema.
     *
     * @param nome     Nome do comprador.
     * @param email    Email do comprador.
     * @param senha    Senha do comprador.
     * @param cpf      CPF do comprador (com ou sem formatação).
     * @param endereco Endereço do comprador.
     */
    public void adicionarComprador(String nome, String email, String senha, String cpf, String endereco) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", ""); // Remove formatação do CPF

        if (cpfNumerico.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }

        if (compradorRepository.buscarPorCpf(cpfNumerico).isPresent()) {
            throw new IllegalArgumentException("Já existe um comprador cadastrado com este CPF");
        }

        Comprador novoComprador = new Comprador(nome, email, senha, cpfNumerico, endereco);
        compradorRepository.salvar(novoComprador);
    }

    /**
     * Lista todos os compradores cadastrados.
     *
     * @return Lista de compradores.
     */
    public List<Comprador> listarCompradores() {
        return compradorRepository.listar();
    }

    /**
     * Remove um comprador pelo CPF.
     *
     * @param cpf CPF do comprador.
     * @return true se o comprador foi removido, false caso contrário.
     */
    public boolean removerComprador(String cpf) {
        return compradorRepository.remover(cpf);
    }

    /**
     * Busca um comprador pelo CPF.
     *
     * @param cpf CPF do comprador (com ou sem formatação).
     * @return O comprador encontrado.
     * @throws IllegalArgumentException se o CPF for inválido.
     * @throws NoSuchElementException   se o comprador não for encontrado.
     */
    public Comprador buscarCompradorPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }

        String cpfNumerico = cpf.replaceAll("[^0-9]", "");

        if (cpfNumerico.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }

        return compradorRepository.buscarPorCpf(cpfNumerico)
                .orElseThrow(() -> new NoSuchElementException("Comprador não encontrado para CPF: " + cpf));
    }

    /**
     * Realiza o login de um comprador.
     *
     * @param cpf   CPF do comprador.
     * @param senha Senha do comprador.
     * @return true se o login foi bem-sucedido, false caso contrário.
     */
    public boolean login(String cpf, String senha) {
        try {
            Comprador comprador = buscarCompradorPorCpf(cpf);
            if (comprador.getSenha().equals(senha)) {
                this.compradorLogado = comprador;
                this.tentativasLogin = 0; // Reseta o contador de tentativas
                return true;
            }
        } catch (NoSuchElementException e) {
            // Ignorado, pois será tratado abaixo
        }

        tentativasLogin++;
        if (tentativasLogin >= 5) {
            tentativasLogin = 0; // Reseta o contador
            throw new IllegalStateException("Número máximo de tentativas excedido");
        }

        return false;
    }

    /**
     * Realiza o logout do comprador logado.
     */
    public void logout() {
        this.compradorLogado = null;
    }

    /**
     * Verifica se há um comprador logado.
     *
     * @return true se há um comprador logado, false caso contrário.
     */
    public boolean isLoggedIn() {
        return this.compradorLogado != null;
    }

    /**
     * Obtém o comprador atualmente logado.
     *
     * @return O comprador logado ou null se não houver ninguém logado.
     */
    public Comprador getCompradorLogado() {
        return this.compradorLogado;
    }

    /**
     * Adiciona um produto ao carrinho do comprador logado.
     *
     * @param produtoId ID do produto.
     * @param quantidade Quantidade do produto.
     * @return true se o produto foi adicionado com sucesso, false caso contrário.
     */
    public boolean adicionarAoCarrinho(String produtoId, int quantidade) {
        validarCompradorLogado();

        if (quantidade <= 0) {
            return false;
        }

        int quantidadeAtual = compradorLogado.getCarrinho().getOrDefault(produtoId, 0);
        compradorLogado.getCarrinho().put(produtoId, quantidadeAtual + quantidade);

        atualizarCompradorNoRepositorio();
        return true;
    }

    /**
     * Remove um produto do carrinho do comprador logado.
     *
     * @param produtoId ID do produto.
     * @return true se o produto foi removido, false caso contrário.
     */
    public boolean removerDoCarrinho(String produtoId) {
        validarCompradorLogado();

        boolean existiaNoCarrinho = compradorLogado.getCarrinho().containsKey(produtoId);

        if (existiaNoCarrinho) {
            compradorLogado.getCarrinho().remove(produtoId);
            atualizarCompradorNoRepositorio();
        }

        return existiaNoCarrinho;
    }

    /**
     * Limpa o carrinho do comprador logado.
     */
    public void limparCarrinho() {
        validarCompradorLogado();
        compradorLogado.getCarrinho().clear();
        atualizarCompradorNoRepositorio();
    }

    /**
     * Obtém os itens do carrinho do comprador logado.
     *
     * @return Mapa de produtos no carrinho com suas respectivas quantidades.
     */
    public Map<String, Integer> getCarrinho() {
        validarCompradorLogado();
        return compradorLogado.getCarrinho();
    }

    /**
     * Altera a quantidade de um produto no carrinho do comprador logado.
     *
     * @param produtoId     ID do produto.
     * @param novaQuantidade Nova quantidade do produto.
     * @return true se a quantidade foi alterada com sucesso, false caso contrário.
     */
    public boolean alterarQuantidadeCarrinho(String produtoId, int novaQuantidade) {
        validarCompradorLogado();

        if (novaQuantidade <= 0) {
            return false;
        }

        if (compradorLogado.getCarrinho().containsKey(produtoId)) {
            compradorLogado.getCarrinho().put(produtoId, novaQuantidade);
            atualizarCompradorNoRepositorio();
            return true;
        }

        return false;
    }

    /**
     * Atualiza os dados do comprador logado no repositório.
     */
    private void atualizarCompradorNoRepositorio() {
        if (compradorLogado != null) {
            compradorRepository.remover(compradorLogado.getCpf());
            compradorRepository.salvar(compradorLogado);
        }
    }

    /**
     * Valida se há um comprador logado.
     *
     * @throws IllegalStateException se não houver comprador logado.
     */
    private void validarCompradorLogado() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Nenhum comprador logado");
        }
    }

    /**
     * Finaliza a compra do carrinho e concede pontos ao comprador.
     *
     * @param valorTotalCompra Valor total da compra em reais.
     */
    public void finalizarCompra(double valorTotalCompra) {
        validarCompradorLogado();

        if (valorTotalCompra <= 0) {
            throw new IllegalArgumentException("O valor da compra deve ser maior que zero.");
        }

        // Exemplo: 1 ponto a cada R$10 gastos
        int pontosGanhos = (int) (valorTotalCompra / 10);
        compradorLogado.adicionarPontos(pontosGanhos);

        limparCarrinho(); // Limpa o carrinho após a compra
        atualizarCompradorNoRepositorio();
    }

    /**
     * Concede pontos ao comprador por avaliar um produto.
     *
     * @param idProduto ID do produto avaliado
     */
    public void avaliarProduto(String idProduto) {
        validarCompradorLogado();

        // Exemplo: +50 pontos por avaliação
        int pontosPorAvaliacao = 50;
        compradorLogado.adicionarPontos(pontosPorAvaliacao);
        atualizarCompradorNoRepositorio();
    }

    /**
     * Tenta resgatar um benefício usando pontos.
     *
     * @param nomeBeneficio Nome do benefício
     * @param pontosNecessarios Pontos necessários para resgate
     * @return true se o benefício foi resgatado, false caso contrário
     */
    public boolean resgatarBeneficio(String nomeBeneficio, int pontosNecessarios) {
        validarCompradorLogado();

        if (compradorLogado.usarPontos(pontosNecessarios)) {
            System.out.println("Benefício '" + nomeBeneficio + "' resgatado com sucesso!");
            return true;
        } else {
            System.out.println("Você não possui pontos suficientes para resgatar '" + nomeBeneficio + "'.");
            return false;
        }
    }



}
