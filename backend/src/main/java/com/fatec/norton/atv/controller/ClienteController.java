package com.fatec.norton.atv.controller;

import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping
	public List<Cliente> listar() {
		return clienteService.listar();
	}

	@PostMapping
	public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
		Cliente criado = clienteService.criar(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PatchMapping("/{codigo}")
	public Cliente atualizar(@PathVariable Long codigo, @RequestBody Cliente cliente) {
		return clienteService.atualizar(codigo, cliente);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		clienteService.excluir(codigo);
		return ResponseEntity.noContent().build();
	}
}

