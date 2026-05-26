package com.fatec.norton.atv.controller;

import com.fatec.norton.atv.dto.CarrinhoRequestDTO;
import com.fatec.norton.atv.dto.CarrinhoResponseDTO;
import com.fatec.norton.atv.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/{id}")
    public CarrinhoResponseDTO carrinhoById(@PathVariable UUID id){
        return carrinhoService.carrinhoById(id);
    }

    @PostMapping("/{clienteId}/{carrinhoId}")
    public ResponseEntity<CarrinhoResponseDTO> adicionarProdutos(@RequestBody CarrinhoRequestDTO carrinhoRequestDTO,
                                                                 @PathVariable Long clienteId,
                                                                 @PathVariable  UUID carrinhoId){
        return ResponseEntity.ok(carrinhoService.adicionarProdutos(carrinhoRequestDTO, clienteId, carrinhoId));
    }
}
