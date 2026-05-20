package com.fatec.norton.atv.service;

import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;

	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public List<Cliente> listar() {
		return clienteRepository.findAll();
	}

	public Cliente criar(Cliente cliente) {
		cliente.setCodigo(null);
		return clienteRepository.save(cliente);
	}

	public Cliente atualizar(Long codigo, Cliente cliente) {
		Cliente existente = clienteRepository.findById(codigo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

		existente.setNome(cliente.getNome());
		existente.setEmail(cliente.getEmail());
		existente.setSenha(cliente.getSenha());
		existente.setTelefone(cliente.getTelefone());
		existente.setLogradouro(cliente.getLogradouro());

		return clienteRepository.save(existente);
	}

	public void excluir(Long codigo) {
		if (!clienteRepository.existsById(codigo)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
		}
		clienteRepository.deleteById(codigo);
	}
}

