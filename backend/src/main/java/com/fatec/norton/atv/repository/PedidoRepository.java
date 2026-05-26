package com.fatec.norton.atv.repository;

import com.fatec.norton.atv.model.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
