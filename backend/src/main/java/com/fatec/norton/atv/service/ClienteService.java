package com.fatec.norton.atv.service;

import com.fatec.norton.atv.dto.ClienteAlterarSenhaRequestDTO;
import com.fatec.norton.atv.dto.ClienteRequestDTO;
import com.fatec.norton.atv.dto.ClienteResponseDTO;
import com.fatec.norton.atv.dto.EmailRequestDTO;
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
    private final EmailService emailService;

    public ClienteService(ClienteRepository clienteRepository,
                          CarrinhoRepository carrinhoRepository, EmailService emailService) {
        this.clienteRepository = clienteRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.emailService = emailService;
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

        EmailRequestDTO email = new EmailRequestDTO();

        email.setTo(clienteSalvo.getEmail());

        email.setSubject("🎉 Bem-vindo(a) à plataforma!");

        String html = """
    <div style="
        font-family: Arial, sans-serif;
        max-width: 600px;
        margin: auto;
        padding: 30px;
        background-color: #f4f4f4;
        border-radius: 12px;
    ">

        <div style="
            background-color: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        ">

            <h1 style="
                color: #2563eb;
                text-align: center;
            ">
                Bem-vindo(a)!
            </h1>

            <p style="
                font-size: 16px;
                color: #333;
                line-height: 1.6;
            ">
                Olá <strong>%s</strong>,
            </p>

            <p style="
                font-size: 16px;
                color: #333;
                line-height: 1.6;
            ">
                Sua conta foi criada com sucesso 🚀
            </p>

            <p style="
                font-size: 16px;
                color: #333;
                line-height: 1.6;
            ">
                Agora você já pode acessar a plataforma.
            </p>

            <div style="text-align:center; margin-top:30px;">

                <a href="http://localhost:4200/login"
                   style="
                    background-color:#2563eb;
                    color:white;
                    padding:14px 24px;
                    border-radius:8px;
                    text-decoration:none;
                    font-weight:bold;
                   ">
                    Acessar Plataforma
                </a>

            </div>

        </div>
    </div>
    """.formatted(clienteSalvo.getNome());

        email.setBody(html);

        emailService.sendSimpleEmail(email);
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
