package com.fatec.norton.atv.dto;

import java.util.List;

public class CarrinhoRequestDTO {

    private List<Long> produtoId;

    public List<Long> getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(List<Long> produtoId) {
        this.produtoId = produtoId;
    }
}
