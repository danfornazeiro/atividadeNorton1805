package com.fatec.norton.atv.dto;

import com.fatec.norton.atv.model.carrinho.Carrinho;
import com.fatec.norton.atv.model.pedido.Pedido;

import java.util.UUID;

public record PedidoResponseDTO(
        UUID id,
        CarrinhoResponseDTO carrinho
) {
    public PedidoResponseDTO(Pedido pedido) {
        this(
                pedido.getId(),
                new CarrinhoResponseDTO(pedido.getCarrinho())
        );
    }
}