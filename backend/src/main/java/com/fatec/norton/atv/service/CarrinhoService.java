package com.fatec.norton.atv.service;

import com.fatec.norton.atv.dto.CarrinhoRequestDTO;
import com.fatec.norton.atv.dto.CarrinhoResponseDTO;
import com.fatec.norton.atv.model.carrinho.Carrinho;
import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.model.produto.Produto;
import com.fatec.norton.atv.repository.CarrinhoRepository;
import com.fatec.norton.atv.repository.ClienteRepository;
import com.fatec.norton.atv.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarrinhoService {

    private final ProdutoRepository produtoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;

    public CarrinhoService(ProdutoRepository produtoRepository,
                           CarrinhoRepository carrinhoRepository,
                           ClienteRepository clienteRepository) {
        this.produtoRepository = produtoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.clienteRepository = clienteRepository;
    }

    public CarrinhoResponseDTO carrinhoById(UUID id){
        Carrinho carrinho = carrinhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrinho nao econtrado."));
        return new CarrinhoResponseDTO(carrinho);
    }


    public CarrinhoResponseDTO adicionarProdutos(CarrinhoRequestDTO dto, Long clienteId, UUID carrinhoId){
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado."));
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("carrinho nao encontrado."));
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("produto nao encontrado."));

        carrinho.getProdutos().add(produto);
        cliente.setCesta(carrinho);

       Carrinho carrinhoSalvo = carrinhoRepository.save(carrinho);
       return new CarrinhoResponseDTO(carrinhoSalvo);
 }

}
