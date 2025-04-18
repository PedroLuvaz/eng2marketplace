package com.eng2marketplace.repository;

import com.eng2marketplace.model.Pedido;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoRepository {
    private static final String ARQUIVO_PEDIDOS = "src/main/data/pedidos.json";
    private final Gson gson;
    private final Type listType = new TypeToken<ArrayList<Pedido>>() {}.getType();

    public PedidoRepository() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            java.io.File file = new java.io.File(ARQUIVO_PEDIDOS);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                salvarLista(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo JSON: " + e.getMessage());
        }
    }

    public void salvar(Pedido pedido) {
        List<Pedido> pedidos = listarTodos();
        pedidos.add(pedido);
        salvarLista(pedidos);
    }

    public List<Pedido> listarTodos() {
        try (FileReader reader = new FileReader(ARQUIVO_PEDIDOS)) {
            List<Pedido> pedidos = gson.fromJson(reader, listType);
            return pedidos != null ? pedidos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Pedido> listarPorComprador(String compradorCpf) {
        return listarTodos().stream()
                .filter(p -> p.getCompradorCpf().equals(compradorCpf))
                .collect(Collectors.toList());
    }

    public boolean atualizarStatus(String pedidoId, String novoStatus) {
        List<Pedido> pedidos = listarTodos();
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(pedidoId)) {
                pedido.setStatus(novoStatus);
                salvarLista(pedidos);
                return true;
            }
        }
        return false;
    }

    private void salvarLista(List<Pedido> pedidos) {
        try (FileWriter writer = new FileWriter(ARQUIVO_PEDIDOS)) {
            gson.toJson(pedidos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }

    public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(src));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
}