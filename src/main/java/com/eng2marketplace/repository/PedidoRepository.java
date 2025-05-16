package com.eng2marketplace.repository;

import com.eng2marketplace.model.Pedido;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PedidoRepository extends JSONRepository<Pedido> {
    private static final String ARQUIVO_PEDIDOS = "src/main/data/pedidos.json";

    public PedidoRepository() {
        super(ARQUIVO_PEDIDOS,
            new TypeToken<>(){},
            new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        );
    }

    public boolean remover(String id) {
        return super.remover(pedido -> pedido.getId().equals(id));
    }

    public List<Pedido> listarPorComprador(String compradorCpf) {
        return listar().stream()
                .filter(p -> p.getCompradorCpf().equals(compradorCpf))
                .collect(Collectors.toList());
    }

    public boolean atualizarStatus(String pedidoId, String novoStatus) {
        Optional<Pedido> optionalPedido = super.buscar(p -> p.getId().equals(pedidoId));
        if(optionalPedido.isEmpty())
            return false;
        Pedido pedido = optionalPedido.get();
        pedido.setStatus(novoStatus);
        return super.atualizar(pedido, p -> p.getId().equals(pedidoId));
    }

    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
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
