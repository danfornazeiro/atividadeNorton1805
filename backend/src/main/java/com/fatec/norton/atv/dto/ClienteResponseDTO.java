package com.fatec.norton.atv.dto;

import com.fatec.norton.atv.model.cliente.Cliente;

public record ClienteResponseDTO(

        Long id,
        String nome,
        String email,
        String telefone,
        String logradouro,
        CarrinhoResponseDTO cesta

) {

    public ClienteResponseDTO(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getLogradouro(),
                new CarrinhoResponseDTO(cliente.getCesta())
        );
    }
}