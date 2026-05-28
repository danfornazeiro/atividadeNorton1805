package com.fatec.norton.atv.dto;

import com.fatec.norton.atv.model.carrinho.Carrinho;
import com.fatec.norton.atv.model.produto.Produto;

import java.util.List;
import java.util.UUID;

public record CarrinhoResponseDTO(

        UUID id,
        List<Produto> produtos

) {

    public CarrinhoResponseDTO(Carrinho carrinho) {
        this(
                carrinho == null ? null : carrinho.getCarrinhoId(),
                (carrinho == null || carrinho.getProdutos() == null) ? List.of() : List.copyOf(carrinho.getProdutos())
        );
    }
}