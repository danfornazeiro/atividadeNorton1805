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

	public Produto criar(Produto produto) {
		produto.setCodigo(null);
		return produtoRepository.save(produto);
	}

	public Produto atualizar(Long codigo, Produto produto) {
		Produto existente = produtoRepository.findById(codigo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

		existente.setNome(produto.getNome());
		existente.setDescricao(produto.getDescricao());
		existente.setValor(produto.getValor());
		existente.setPromo(produto.getPromo());
		existente.setQuantidade(produto.getQuantidade());

		return produtoRepository.save(existente);
	}

	public void excluir(Long codigo) {
		if (!produtoRepository.existsById(codigo)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
		}
		produtoRepository.deleteById(codigo);
	}
}

