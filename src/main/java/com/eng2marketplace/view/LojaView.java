package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import java.util.List;

public class LojaView {
    private final MarketplaceFacade facade;
    private final ConsoleInput scanner;

    public LojaView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void menu() {
        Integer opcao;
        do {
            System.out.println("\n--- Gestão de Lojas ---");
            System.out.println("1. Adicionar Loja");
            System.out.println("2. Listar Lojas");
            System.out.println("3. Remover Loja");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 3);

            if(opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> adicionarLoja();
                case 2 -> listarLojas();
                case 3 -> removerLoja();
                case 0 -> System.out.println("Voltando ao menu principal...");
            }
        } while (opcao == null);
    }

    private void adicionarLoja() {
        String nome = scanner.askName("Nome (entre 2 e 99 letras): ", 99, "Nome inválido!");
        String email = scanner.askMail("Email (padrão aaa@bbb.ccc): ", "Email inválido!");
        String senha = scanner.askText("Senha (pelo menos 8 caracteres): ", ".{8,}", "Senha inválida!");
        String cpfCnpj = scanner.askCNPJ("CPF/CNPJ (somente números ou com ponto/hífen/barra): ", "Número de documento inválido!");
        String endereco = scanner.askText("Endereço (entre 5 e 250 caracteres): ", ".{5,250}", "Endereço inválido!");

        facade.adicionarLoja(nome, email, senha, cpfCnpj, endereco);
        System.out.println("Loja adicionada com sucesso!");
    }

    private void listarLojas() {
        List<Loja> lojas = facade.listarLojas();
        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada.");
        } else {
            lojas.forEach(System.out::println);
        }
    }

    private void removerLoja() {
        String cpfCnpj = scanner.askCPFCNPJ(
            "Informe o CPF/CNPJ da loja a ser removida (somente números ou com ponto/hífen/barra): ",
            "Documento inválido!");

        if (facade.removerLoja(cpfCnpj)) {
            System.out.println("Loja removida com sucesso!");
        } else {
            System.out.println("Loja não encontrada.");
        }
    }
}
