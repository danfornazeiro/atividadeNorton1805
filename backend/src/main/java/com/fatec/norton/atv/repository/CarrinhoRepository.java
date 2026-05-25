package com.fatec.norton.atv.repository;

import com.fatec.norton.atv.model.carrinho.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarrinhoRepository extends JpaRepository<Carrinho, UUID> {
}
