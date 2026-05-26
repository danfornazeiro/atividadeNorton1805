package com.fatec.norton.atv.controller;

import com.fatec.norton.atv.model.produto.Produto;
import com.fatec.norton.atv.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@GetMapping
	public List<Produto> listar() {
		return produtoService.listar();
	}
	@GetMapping("/{nome}")
	public List<Produto> listarPeloNome(@PathVariable String nome) {
		return produtoService.listarPeloNome(nome);
	}

	@PostMapping
	public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
		Produto criado = produtoService.criar(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PatchMapping("/{id}")
	public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
		return produtoService.atualizar(id, produto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		produtoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}

