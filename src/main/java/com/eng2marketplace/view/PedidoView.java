package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.List;
import java.util.Map;

public class PedidoView {
    private final MarketplaceFacade facade;
    private final ConsoleInput scanner;

    public PedidoView(MarketplaceFacade facade) {
        this.facade = facade;
        this.scanner = new ConsoleInput();
    }

    public void finalizarCompra() {
        try {
            if (!facade.isCompradorLogado()) {
                System.out.println("Você precisa estar logado como comprador.");
                return;
            }

            Map<String, Integer> carrinho = facade.getCarrinho();
            if (carrinho.isEmpty()) {
                System.out.println("Seu carrinho está vazio. Adicione produtos antes de finalizar.");
                return;
            }

            Pedido pedido = facade.finalizarCompra(facade.getCompradorLogado().getCpf());
            System.out.println("\n--- Compra Finalizada com Sucesso ---");
            System.out.printf("Número do Pedido: %s%n", pedido.getId());
            System.out.printf("Valor Total: R$%.2f%n", pedido.getValorTotal());
            System.out.println("Obrigado por sua compra!");

            // Permitir avaliação da loja
            String avaliar = scanner.askText("Deseja avaliar a loja desta compra? (s/n): ", "[sSnN]", "Digite s ou n");
            if (avaliar.equalsIgnoreCase("s")) {
                String lojaCpfCnpj = pedido.getLoja() != null ? pedido.getLoja().getCpfCnpj() : null;
                if (lojaCpfCnpj == null) {
                    System.out.println("Não foi possível identificar a loja para avaliação.");
                    return;
                }
                int nota = scanner.getNumber(1, 5);
                scanner.nextLine(); // Limpa buffer se necessário
                String comentario = scanner.askText("Comentário: ");
                try {
                    facade.avaliarLoja(lojaCpfCnpj, facade.getCompradorLogado().getCpf(), nota, comentario);
                    System.out.println("Avaliação registrada com sucesso!");
                } catch (Exception e) {
                    System.out.println("Erro ao avaliar loja: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao finalizar compra: " + e.getMessage());
            e.printStackTrace(); // Para debug durante desenvolvimento
        }
    }

    public void menuHistorico() {
        try {
            if (!facade.isCompradorLogado()) {
                System.out.println("Você precisa estar logado como comprador.");
                return;
            }

            String compradorCpf = facade.getCompradorLogado().getCpf();
            List<Pedido> pedidos = facade.listarHistoricoCompras(compradorCpf);

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
                return;
            }

            System.out.println("\n--- Seu Histórico de Pedidos ---");
            pedidos.forEach(pedido -> {
                System.out.printf("Pedido #%s | Data: %s | Total: R$%.2f | Status: %s%n",
                        pedido.getId().substring(0, 8),
                        pedido.getDataPedido(),
                        pedido.getValorTotal(),
                        pedido.getStatus());
            });
        } catch (Exception e) {
            System.out.println("Erro ao acessar histórico: " + e.getMessage());
            e.printStackTrace(); // Para debug durante desenvolvimento
        }
    }
}