package com.fatec.norton.atv.controller;

import com.fatec.norton.atv.dto.ClienteAlterarSenhaRequestDTO;
import com.fatec.norton.atv.dto.ClienteRequestDTO;
import com.fatec.norton.atv.dto.ClienteResponseDTO;
import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.service.ClienteService;
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
@RequestMapping("/cliente")
public class ClienteController {

	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping
	public List<ClienteResponseDTO> listar() {
		return clienteService.listar();
	}

	@PostMapping
	public ResponseEntity<ClienteResponseDTO> criar(@RequestBody ClienteRequestDTO clienteRequestDTO) {
		return ResponseEntity.ok(clienteService.criar(clienteRequestDTO));
	}

	@PatchMapping("/{id}")
	public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
		return clienteService.atualizar(id, cliente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		clienteService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/alterarSenha/{id}")
	public String alterarSenha(@PathVariable Long id, @RequestBody ClienteAlterarSenhaRequestDTO dto) {
		clienteService.alterarSenha(id, dto);
		return "";
	}
}

