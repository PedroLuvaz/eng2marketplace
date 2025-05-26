package com.eng2marketplace.view;

import com.eng2marketplace.Facade.MarketplaceFacade;
import com.eng2marketplace.model.Pedido;
import com.eng2marketplace.model.Produto;
import com.eng2marketplace.view.input.ConsoleInput;

import java.util.HashMap;
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

            Map<String, Integer> carrinhoOriginal = new HashMap<>(facade.getCarrinho());
            if (carrinhoOriginal.isEmpty()) {
                System.out.println("Seu carrinho está vazio. Adicione produtos antes de finalizar.");
                return;
            }

            Pedido pedido = facade.finalizarCompra(carrinhoOriginal);

            System.out.println("\n--- Compra Finalizada com Sucesso ---");
            System.out.printf("Número do Pedido: %s%n", pedido.getId());
            System.out.printf("Valor Total: R$%.2f%n", pedido.getValorTotal());
            System.out.println("Obrigado por sua compra!");

            // Avaliação dos produtos
            for (Map.Entry<String, Integer> item : carrinhoOriginal.entrySet()) {
                Produto produto = facade.listarProdutos().stream()
                    .filter(p -> p.getId().equals(item.getKey()))
                    .findFirst().orElse(null);
                if (produto != null) {
                    System.out.printf("Deseja avaliar o produto '%s'? (s/n): ", produto.getNome());
                    String resp = scanner.askText("> ", "[sSnN]", "Digite s ou n");
                    if (resp.equalsIgnoreCase("s")) {
                        System.out.print("Digite uma nota de 1 a 5 para o produto: ");
                        Integer nota = scanner.getNumber(1, 5);
                        if (nota == null) {
                            System.out.println("Nota inválida. Avaliação cancelada.");
                            continue;
                        }
                        String comentario = scanner.askText("Comentário para o produto: ", ".{0,250}", "Comentário inválido!");
                        facade.avaliarProduto(produto.getId(), facade.getCompradorLogado().getCpf(), nota, comentario);
                    }
                }
            }

            // Avaliação da loja (pedido)
            String lojaCpfCnpj = pedido.getLoja() != null ? pedido.getLoja().getCpfCnpj() : null;
            if (lojaCpfCnpj != null) {
                System.out.print("Deseja avaliar a loja desta compra? (s/n): ");
                String resp = scanner.askText("> ", "[sSnN]", "Digite s ou n");
                if (resp.equalsIgnoreCase("s")) {
                    System.out.print("Digite uma nota de 1 a 5 para a loja: ");
                    Integer notaLoja = scanner.getNumber(1, 5);
                    if (notaLoja == null) {
                        System.out.println("Nota inválida. Avaliação cancelada.");
                    } else {
                        String comentarioLoja = scanner.askText("Comentário para a loja: ", ".{0,250}", "Comentário inválido!");
                        facade.avaliarLoja(lojaCpfCnpj, facade.getCompradorLogado().getCpf(), notaLoja, comentarioLoja);
                    }
                }
            }


            System.out.println("Avaliações registradas. Obrigado pelo feedback!");
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
