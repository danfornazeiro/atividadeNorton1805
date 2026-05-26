package com.fatec.norton.atv.model.pedido;

import com.fatec.norton.atv.model.carrinho.Carrinho;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "pedido_tb")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}
