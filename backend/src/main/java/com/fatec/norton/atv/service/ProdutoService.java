package com.fatec.norton.atv.service;

import com.fatec.norton.atv.model.produto.Produto;
import com.fatec.norton.atv.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;

	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public List<Produto> listar() {
		return produtoRepository.findAll();
	}

	public List<Produto> listarPeloNome(String nome) {
		return produtoRepository.findByNomeContainingIgnoreCase(nome);
	}

	public List<Produto> listarPorCategoria(String categoria){
		return produtoRepository.findByCategoriaIgnoreCase(categoria);
	}

	public Produto criar(Produto produto) {
		produto.setId(null);
		return produtoRepository.save(produto);
	}

	public Produto atualizar(Long id, Produto produto) {
		Produto existente = produtoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

		if(produto.getNome() != null){
			existente.setNome(produto.getNome());
		}
		if(produto.getDescricao() != null){
			existente.setDescricao(produto.getDescricao());
		}
		if(produto.getImageUrl() != null){
			existente.setImageUrl(produto.getImageUrl());
		}
		if(produto.getValor() != null){
			existente.setValor(produto.getValor());
		}
		if(produto.getPromo() != null){
			existente.setPromo(produto.getPromo());
		}
		if(produto.getQuantidade() != null){
			existente.setQuantidade(produto.getQuantidade());
		}
		if(produto.getCategoria() != null){
			existente.setCategoria(produto.getCategoria());
		}
		return produtoRepository.save(existente);
	}

	public void excluir(Long id) {
		if (!produtoRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
		}
		produtoRepository.deleteById(id);
	}
}

