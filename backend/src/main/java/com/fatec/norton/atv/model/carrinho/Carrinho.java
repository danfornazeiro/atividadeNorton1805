package com.fatec.norton.atv.model.carrinho;

import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.model.produto.Produto;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carrinho_tb")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID carrinho;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
            name = "carrinho_produto",
            joinColumns = @JoinColumn(name = "carrinho_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;

    public UUID getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(UUID carrinho) {
        this.carrinho = carrinho;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
