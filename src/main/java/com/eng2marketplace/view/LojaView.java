package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.view.input.ConsoleInput;

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
            System.out.println("3. Buscar Loja por CPF/CNPJ");
            System.out.println("4. Remover Loja");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 4);

            if(opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> adicionarLoja();
                case 2 -> listarLojas();
                case 3 -> buscarLojaPorCpfCnpj();
                case 4 -> removerLoja();
                case 0 -> System.out.println("Voltando ao menu principal...");
            }
        } while (opcao != 0);
    }

    private void adicionarLoja() {
        String nome = scanner.askName("Nome (entre 2 e 99 letras): ", 99, "Nome inválido!");
        String email = scanner.askMail("Email (padrão aaa@bbb.ccc): ", "Email inválido!");
        String senha = scanner.askText("Senha (pelo menos 8 caracteres): ", ".{8,}", "Senha inválida!");
        String cpfCnpj = scanner.askCPFCNPJ("CPF/CNPJ (somente números ou com ponto/hífen/barra): ", "Número de documento inválido!");
        String endereco = scanner.askText("Endereço (entre 5 e 250 caracteres): ", ".{5,250}", "Endereço inválido!");
    
        try {
            facade.adicionarLoja(nome, email, senha, cpfCnpj, endereco);
            System.out.println("Loja adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao adicionar loja: " + e.getMessage());
        }
    }

    private void listarLojas() {
        List<Loja> lojas = facade.listarLojas();
        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada.");
        } else {
            System.out.println("\n--- Lista de Lojas ---");
            lojas.forEach(loja -> 
                System.out.println(loja.getNome() + " - " + loja.getCpfCnpj() + 
                                 " - " + loja.getEmail()));
        }
    }

    private void buscarLojaPorCpfCnpj() {
        String cpfCnpj = scanner.askCPFCNPJ(
            "Informe o CPF/CNPJ da loja a ser buscada (somente números ou com ponto/hífen/barra): ",
            "Documento inválido!");
    
        try {
            Loja loja = facade.buscarLojaPorCpfCnpj(cpfCnpj);
            if (loja != null) {
                System.out.println("\n--- Dados da Loja ---");
                System.out.println("Nome: " + loja.getNome());
                System.out.println("Email: " + loja.getEmail());
                System.out.println("CPF/CNPJ: " + loja.getCpfCnpj());
                System.out.println("Endereço: " + loja.getEndereco());
            } else {
                System.out.println("Loja não encontrada.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
    }
    
    private void removerLoja() {
        String cpfCnpj = scanner.askCPFCNPJ(
            "Informe o CPF/CNPJ da loja a ser removida (somente números ou com ponto/hífen/barra): ",
            "Documento inválido!");
    
        try {
            if (facade.removerLoja(cpfCnpj)) {
                System.out.println("Loja removida com sucesso!");
            } else {
                System.out.println("Loja não encontrada.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao remover loja: " + e.getMessage());
        }
    }
}