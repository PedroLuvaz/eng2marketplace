package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Comprador;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.List;

public class CompradorView {
    private MarketplaceFacade facade;
    private ConsoleInput scanner;

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
            System.out.println("3. Remover Comprador");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 3);
            if(opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> cadastrarComprador();
                case 2 -> listarCompradores();
                case 3 -> removerComprador();
                case 0 -> System.out.println("Voltando ao menu principal...");
            }
        } while (opcao == null);
    }

    private void cadastrarComprador() {
        String nome = scanner.askName("Nome (entre 2 e 99 letras): ", 99, "Nome inválido!");
        String email = scanner.askMail("Email (padrão aaa@bbb.ccc): ", "Email inválido!");
        String senha = scanner.askText("Senha (pelo menos 8 caracteres): ", ".{8,}", "Senha inválida!");
        String endereco = scanner.askText("Endereço (entre 5 e 250 caracteres): ", ".{5,250}", "Endereço inválido!");

        facade.cadastrarComprador(nome, email, senha, endereco);
        System.out.println("Comprador cadastrado com sucesso!");
    }

    private void listarCompradores() {
        List<Comprador> compradores = facade.listarCompradores();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado.");
        } else {
            compradores.forEach(System.out::println);
        }
    }

    private void removerComprador() {
        String email = scanner.askMail("Informe o email do comprador a ser removido (padrão aaa@bbb.ccc): ", "Email inválido!");
        if (facade.removerComprador(email)) {
            System.out.println("Comprador removido com sucesso!");
        } else {
            System.out.println("Comprador não encontrado.");
        }
    }
}
