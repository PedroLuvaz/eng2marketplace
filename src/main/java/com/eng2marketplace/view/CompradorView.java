package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.List;
import java.util.Scanner;

public class CompradorView {
    private final MarketplaceFacade facade;
    private final ConsoleInput scanner;

    public CompradorView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void menu() {
        Integer opcao;
        do {
            System.out.println("\n--- Gestão de Compradores ---");
            System.out.println("1. Cadastrar Comprador");
            System.out.println("2. Listar Compradores");
            System.out.println("3. Buscar Comprador por CPF");
            System.out.println("4. Remover Comprador");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 4);

            if (opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> cadastrarComprador();
                case 2 -> listarCompradores();
                case 3 -> buscarCompradorPorCpf();
                case 4 -> removerComprador();
                case 0 -> System.out.println("Voltando ao menu principal...");
            }
        } while (opcao != 0);
    }

    private void cadastrarComprador() {
        String nome = scanner.askName("Nome (entre 2 e 99 letras): ", 99, "Nome inválido!");
        String email = scanner.askMail("Email (padrão aaa@bbb.ccc): ", "Email inválido!");
        String senha = scanner.askText("Senha (pelo menos 8 caracteres): ", ".{8,}", "Senha inválida!");
        String cpf = scanner.askCPF("CPF (somente números ou com ponto e hífen): ", "Número de documento inválido!");
        String endereco = scanner.askText("Endereço (entre 5 e 250 caracteres): ", ".{5,250}", "Endereço inválido!");

        facade.cadastrarComprador(nome, email, senha, cpf, endereco);
        System.out.println("Comprador cadastrado com sucesso!");
    }

    private void listarCompradores() {
        List<Comprador> compradores = facade.listarCompradores();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
        } else {
            System.out.println("\n--- Lista de Compradores ---");
            compradores.forEach(comprador ->
                System.out.println(comprador.getNome() + " - " + comprador.getCpf() +
                    " - " + comprador.getEmail()));
        }
    }

    private void buscarCompradorPorCpf() {
        String cpf = scanner.askCPF("Informe o CPF do comprador: ", "Número de documento inválido!");

        try {
            Comprador comprador = facade.buscarCompradorPorCpf(cpf);
            if (comprador != null) {
                System.out.println("\n--- Dados do Comprador ---");
                System.out.println("Nome: " + comprador.getNome());
                System.out.println("Email: " + comprador.getEmail());
                System.out.println("CPF: " + comprador.getCpf());
                System.out.println("Endereço: " + comprador.getEndereco());
            } else {
                System.out.println("Comprador não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void removerComprador() {
        String cpf = scanner.askCPF("Informe o CPF do comprador a ser removido: ", "Número de documento inválido!");

        if (facade.removerComprador(cpf)) {
            System.out.println("Comprador removido com sucesso!");
        } else {
            System.out.println("Comprador não encontrado.");
        }
    }
}
