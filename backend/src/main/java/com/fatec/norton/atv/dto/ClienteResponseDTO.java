package com.fatec.norton.atv.dto;

import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.model.pedido.Pedido;

import java.util.List;
import java.util.stream.Collectors;

public record ClienteResponseDTO(

        Long id,
        String nome,
        String senha,
        String email,
        String telefone,
        String logradouro,
        CarrinhoResponseDTO cesta,
        List<PedidoResponseDTO> pedido

) {

    public ClienteResponseDTO(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getSenha(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getLogradouro(),
                new CarrinhoResponseDTO(cliente.getCesta()),
                cliente.getPedidos() == null ? List.of() : cliente.getPedidos().stream()
                        .map(PedidoResponseDTO::new)
                        .collect(Collectors.toList())
        );
    }
}