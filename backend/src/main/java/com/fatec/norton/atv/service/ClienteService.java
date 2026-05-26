package com.fatec.norton.atv.service;

import com.fatec.norton.atv.dto.ClienteAlterarSenhaRequestDTO;
import com.fatec.norton.atv.dto.ClienteRequestDTO;
import com.fatec.norton.atv.dto.ClienteResponseDTO;
import com.fatec.norton.atv.model.carrinho.Carrinho;
import com.fatec.norton.atv.model.cliente.Cliente;
import com.fatec.norton.atv.repository.ClienteRepository;
import com.fatec.norton.atv.repository.CarrinhoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CarrinhoRepository carrinhoRepository;

    public ClienteService(ClienteRepository clienteRepository,
                         CarrinhoRepository carrinhoRepository) {
        this.clienteRepository = clienteRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::new)
                .toList();
    }

    public ClienteResponseDTO criar(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequestDTO.getNome());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setSenha(clienteRequestDTO.getSenha());
        cliente.setTelefone(clienteRequestDTO.getTelefone());
        cliente.setLogradouro(clienteRequestDTO.getLogradouro());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        Carrinho carrinho = new Carrinho();
        carrinho.setCliente(clienteSalvo);
        Carrinho carrinhoSalvo = carrinhoRepository.save(carrinho);

        clienteSalvo.setCesta(carrinhoSalvo);
        clienteRepository.save(clienteSalvo);

        return new ClienteResponseDTO(clienteSalvo);
    }

    public Cliente atualizar(Long id, Cliente cliente) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        existente.setNome(cliente.getNome());
        existente.setEmail(cliente.getEmail());
        existente.setSenha(cliente.getSenha());
        existente.setTelefone(cliente.getTelefone());
        existente.setLogradouro(cliente.getLogradouro());

        return clienteRepository.save(existente);
    }

    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }

	public String alterarSenha(Long id, ClienteAlterarSenhaRequestDTO dto){
		Cliente existente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		existente.setSenha(dto.getSenha());
		clienteRepository.save(existente);
		return "Senha alterada.";
	}

}
