package com.fatec.norton.atv.dto;

import com.fatec.norton.atv.model.pedido.Pedido;
import com.fatec.norton.atv.model.produto.Produto;

import java.util.UUID;
import java.util.List;

public record PedidoResponseDTO(
        UUID id,
        CarrinhoResponseDTO carrinho,
        List<Produto> produtos
) {
    public PedidoResponseDTO(Pedido pedido) {
        this(
                pedido.getId(),
                new CarrinhoResponseDTO(pedido.getCarrinho()),
                pedido.getProdutos() == null ? List.of() : List.copyOf(pedido.getProdutos())
        );
    }
}