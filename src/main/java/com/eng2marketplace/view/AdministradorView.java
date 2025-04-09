package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Loja;
import com.eng2marketplace.view.input.ConsoleInput;

public class AdministradorView {
    private final MarketplaceFacade facade;
    private final ConsoleInput scanner;

    public AdministradorView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void menu() {
        Integer opcao;
        do {
            System.out.println("\n--- Menu do Administrador ---");
            System.out.println("1. Acessar Menu de Lojas");
            System.out.println("2. Acessar Menu de Compradores");
            System.out.println("3. Acessar Menu de Produtos de uma Loja");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.getNumber(0, 3);

            if (opcao == null) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> acessarMenuLojas();
                case 2 -> acessarMenuCompradores();
                case 3 -> acessarMenuProdutosDeLoja();
                case 0 -> System.out.println("Saindo do menu do administrador...");
            }

        } while (opcao != 0);
    }

    private void acessarMenuLojas() {
        LojaView lojaView = new LojaView(facade);
        lojaView.menu();
    }

    private void acessarMenuCompradores() {
        CompradorView compradorView = new CompradorView(facade);
        compradorView.menu();
    }

    private void acessarMenuProdutosDeLoja() {
        System.out.println("\n--- Acessar Produtos de uma Loja ---");
        String cpfCnpj = scanner.askText("Informe o CPF/CNPJ da loja: ", "\\d{11,14}", "CPF/CNPJ inválido.");

        Loja loja = facade.buscarLojaPorCpfCnpj(cpfCnpj); // você deve ter esse método na fachada

        if (loja != null) {
            ProdutoView produtoView = new ProdutoView(facade);
            produtoView.menu(loja); // passa a loja como argumento
        } else {
            System.out.println("Loja não encontrada.");
        }
    }
}
