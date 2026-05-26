package com.fatec.norton.atv.controller;

import com.fatec.norton.atv.dto.PedidoResponseDTO;
import com.fatec.norton.atv.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    @PostMapping("/{carrinhoId}")
    public ResponseEntity<PedidoResponseDTO> fazerPedido(@PathVariable UUID carrinhoId){
        return ResponseEntity.ok(pedidoService.fazerPedido(carrinhoId));
    }

    @DeleteMapping("/{pedidoId}")
    public void deletePedido(@PathVariable UUID pedidoId){
        pedidoService.deletarPedido(pedidoId);
    }

}
