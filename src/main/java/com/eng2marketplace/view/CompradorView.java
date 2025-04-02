package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Comprador;

import java.util.List;
import java.util.Scanner;

public class CompradorView {
    private MarketplaceFacade facade;
    private Scanner scanner;

    public CompradorView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Compradores ---");
            System.out.println("1. Cadastrar Comprador");
            System.out.println("2. Listar Compradores");
            System.out.println("3. Buscar Comprador por CPF");
            System.out.println("4. Remover Comprador");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
                
                switch (opcao) {
                    case 1:
                        cadastrarComprador();
                        break;
                    case 2:
                        listarCompradores();
                        break;
                    case 3:
                        buscarCompradorPorCpf();
                        break;
                    case 4:
                        removerComprador();
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Digite um número.");
                scanner.nextLine(); // Limpa o buffer
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private void cadastrarComprador() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        
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
        System.out.print("\nInforme o CPF do comprador: ");
        String cpf = scanner.nextLine();
        
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
        System.out.print("Informe o CPF do comprador a ser removido: ");
        String cpf = scanner.nextLine();
        
        if (facade.removerComprador(cpf)) {
            System.out.println("Comprador removido com sucesso!");
        } else {
            System.out.println("Comprador não encontrado.");
        }
    }
}