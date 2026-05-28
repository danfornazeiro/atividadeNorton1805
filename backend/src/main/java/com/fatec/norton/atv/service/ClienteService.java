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
        email.setSubject("🔥 Bem-vindo à DROP CITY!");

        String html = """
    <div style="
        font-family: 'Helvetica Neue', Arial, sans-serif;
        max-width: 600px;
        margin: 0 auto;
        background: #1a1a1a;
        color: #ffffff;
    ">

        <!-- Header -->
        <div style="
            background: #0f0f0f;
            padding: 40px 30px;
            text-align: center;
            border-bottom: 2px solid #2a2a2a;
        ">
            <div style="
                font-size: 28px;
                font-weight: 900;
                letter-spacing: 2px;
                color: #ffffff;
                margin-bottom: 8px;
            ">
                DROP CITY
            </div>
            <div style="
                font-size: 12px;
                letter-spacing: 1px;
                color: #7a7a7a;
                text-transform: uppercase;
            ">
                STREETWEAR LAB
            </div>
        </div>

        <!-- Conteúdo -->
        <div style="padding: 40px 30px;">

            <h1 style="
                font-size: 32px;
                font-weight: 900;
                margin: 0 0 10px 0;
                letter-spacing: 1px;
            ">
                BEM-VINDO(A)
            </h1>

            <p style="
                font-size: 14px;
                color: #b0b0b0;
                margin: 0 0 30px 0;
            ">
                Olá, <strong style="color: #ffffff;">%s</strong>
            </p>

            <!-- Status -->
            <div style="
                background: #1a1a1a;
                border-left: 3px solid #ffffff;
                padding: 16px;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 12px;
                    color: #b0b0b0;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                ">
                    Conta criada com sucesso
                </p>
                <p style="
                    margin: 8px 0 0 0;
                    font-size: 16px;
                    font-weight: 600;
                    color: #ffffff;
                ">
                    Sua conta está ativa na DROP CITY.
                </p>
            </div>

            <!-- Info -->
            <div style="
                background: #0f0f0f;
                padding: 20px;
                border: 1px solid #2a2a2a;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 13px;
                    color: #b0b0b0;
                    line-height: 1.6;
                ">
                    Agora você já pode explorar a vitrine, adicionar produtos ao carrinho e fazer seus pedidos.
                </p>
            </div>

            <!-- CTA -->
            <div style="text-align: center;">
                <a href="http://localhost:4200/login"
                   style="
                    background: #ffffff;
                    color: #000000;
                    padding: 16px 40px;
                    text-decoration: none;
                    font-weight: 700;
                    font-size: 14px;
                    letter-spacing: 1px;
                    text-transform: uppercase;
                    display: inline-block;
                   ">
                    ACESSAR PLATAFORMA
                </a>
            </div>

        </div>

        <!-- Footer -->
        <div style="
            background: #0f0f0f;
            padding: 30px;
            text-align: center;
            border-top: 1px solid #2a2a2a;
            font-size: 12px;
            color: #7a7a7a;
        ">
            <p style="margin: 0 0 10px 0;">
                Drop City - Streetwear Lab © 2026
            </p>
            <p style="margin: 0;">
                Bem-vindo ao lifestyle urbano
            </p>
        </div>

    </div>
    """.formatted(clienteSalvo.getNome());

        email.setBody(html);

        emailService.sendSimpleEmail(email);

        return new ClienteResponseDTO(clienteSalvo);
    }

    public Cliente atualizar(Long id, Cliente cliente) {

        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        existente.setNome(cliente.getNome());
        existente.setEmail(cliente.getEmail());
        existente.setSenha(cliente.getSenha());
        existente.setTelefone(cliente.getTelefone());
        existente.setLogradouro(cliente.getLogradouro());

        Cliente atualizado = clienteRepository.save(existente);

        EmailRequestDTO email = new EmailRequestDTO();
        email.setTo(atualizado.getEmail());
        email.setSubject("⚡ Dados atualizados com sucesso!");

        String html = """
    <div style="
        font-family: 'Helvetica Neue', Arial, sans-serif;
        max-width: 600px;
        margin: 0 auto;
        background: #1a1a1a;
        color: #ffffff;
    ">

        <!-- Header -->
        <div style="
            background: #0f0f0f;
            padding: 40px 30px;
            text-align: center;
            border-bottom: 2px solid #2a2a2a;
        ">
            <div style="
                font-size: 28px;
                font-weight: 900;
                letter-spacing: 2px;
                color: #ffffff;
                margin-bottom: 8px;
            ">
                DROP CITY
            </div>
            <div style="
                font-size: 12px;
                letter-spacing: 1px;
                color: #7a7a7a;
                text-transform: uppercase;
            ">
                STREETWEAR LAB
            </div>
        </div>

        <!-- Conteúdo -->
        <div style="padding: 40px 30px;">

            <h1 style="
                font-size: 32px;
                font-weight: 900;
                margin: 0 0 10px 0;
                letter-spacing: 1px;
            ">
                PERFIL ATUALIZADO
            </h1>

            <p style="
                font-size: 14px;
                color: #b0b0b0;
                margin: 0 0 30px 0;
            ">
                Olá, <strong style="color: #ffffff;">%s</strong>
            </p>

            <!-- Status -->
            <div style="
                background: #1a1a1a;
                border-left: 3px solid #ffffff;
                padding: 16px;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 12px;
                    color: #b0b0b0;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                ">
                    Atualização de dados
                </p>
                <p style="
                    margin: 8px 0 0 0;
                    font-size: 16px;
                    font-weight: 600;
                    color: #ffffff;
                ">
                    Suas informações foram atualizadas com sucesso.
                </p>
            </div>

            <!-- Info -->
            <div style="
                background: #0f0f0f;
                padding: 20px;
                border: 1px solid #2a2a2a;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 13px;
                    color: #b0b0b0;
                    line-height: 1.6;
                ">
                    Se você não reconhece essa alteração, recomendamos alterar sua senha imediatamente.
                </p>
            </div>

            <!-- CTA -->
            <div style="text-align: center;">
                <a href="https://frontend-p2-psi.vercel.app/profile"
                   style="
                    background: #ffffff;
                    color: #000000;
                    padding: 16px 40px;
                    text-decoration: none;
                    font-weight: 700;
                    font-size: 14px;
                    letter-spacing: 1px;
                    text-transform: uppercase;
                    display: inline-block;
                   ">
                    VER PERFIL
                </a>
            </div>

        </div>

        <!-- Footer -->
        <div style="
            background: #0f0f0f;
            padding: 30px;
            text-align: center;
            border-top: 1px solid #2a2a2a;
            font-size: 12px;
            color: #7a7a7a;
        ">
            <p style="margin: 0 0 10px 0;">
                Drop City - Streetwear Lab © 2026
            </p>
            <p style="margin: 0;">
                Seu estilo, sua identidade
            </p>
        </div>

    </div>
    """.formatted(atualizado.getNome());

        email.setBody(html);

        emailService.sendSimpleEmail(email);

        return atualizado;
    }

    public void excluir(Long id) {

        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        EmailRequestDTO email = new EmailRequestDTO();
        email.setTo(existente.getEmail());
        email.setSubject("🖤 Sua conta foi encerrada");

        String html = """
    <div style="
        font-family: 'Helvetica Neue', Arial, sans-serif;
        max-width: 600px;
        margin: 0 auto;
        background: #1a1a1a;
        color: #ffffff;
    ">

        <!-- Header -->
        <div style="
            background: #0f0f0f;
            padding: 40px 30px;
            text-align: center;
            border-bottom: 2px solid #2a2a2a;
        ">
            <div style="
                font-size: 28px;
                font-weight: 900;
                letter-spacing: 2px;
                color: #ffffff;
                margin-bottom: 8px;
            ">
                DROP CITY
            </div>
            <div style="
                font-size: 12px;
                letter-spacing: 1px;
                color: #7a7a7a;
                text-transform: uppercase;
            ">
                STREETWEAR LAB
            </div>
        </div>

        <!-- Conteúdo -->
        <div style="padding: 40px 30px;">

            <h1 style="
                font-size: 32px;
                font-weight: 900;
                margin: 0 0 10px 0;
                letter-spacing: 1px;
            ">
                CONTA ENCERRADA
            </h1>

            <p style="
                font-size: 14px;
                color: #b0b0b0;
                margin: 0 0 30px 0;
            ">
                Até logo, <strong style="color: #ffffff;">%s</strong>
            </p>

            <!-- Status -->
            <div style="
                background: #1a1a1a;
                border-left: 3px solid #ffffff;
                padding: 16px;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 12px;
                    color: #b0b0b0;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                ">
                    Conta removida com sucesso
                </p>
                <p style="
                    margin: 8px 0 0 0;
                    font-size: 16px;
                    font-weight: 600;
                    color: #ffffff;
                ">
                    Seus dados foram excluídos da DROP CITY.
                </p>
            </div>

            <!-- Info -->
            <div style="
                background: #0f0f0f;
                padding: 20px;
                border: 1px solid #2a2a2a;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 13px;
                    color: #b0b0b0;
                    line-height: 1.6;
                ">
                    Sentiremos sua falta. Você pode voltar quando quiser criando uma nova conta.
                </p>
            </div>

            <!-- CTA -->
            <div style="text-align: center;">
                <a href="https://frontend-p2-psi.vercel.app/create-profile"
                   style="
                    background: #ffffff;
                    color: #000000;
                    padding: 16px 40px;
                    text-decoration: none;
                    font-weight: 700;
                    font-size: 14px;
                    letter-spacing: 1px;
                    text-transform: uppercase;
                    display: inline-block;
                   ">
                    CRIAR NOVA CONTA
                </a>
            </div>

        </div>

        <!-- Footer -->
        <div style="
            background: #0f0f0f;
            padding: 30px;
            text-align: center;
            border-top: 1px solid #2a2a2a;
            font-size: 12px;
            color: #7a7a7a;
        ">
            <p style="margin: 0 0 10px 0;">
                Drop City - Streetwear Lab © 2026
            </p>
            <p style="margin: 0;">
                Você sempre será bem-vindo de volta
            </p>
        </div>

    </div>
    """.formatted(existente.getNome());

        emailService.sendSimpleEmail(email);

        clienteRepository.deleteById(id);


    }
    public String alterarSenha(Long id, ClienteAlterarSenhaRequestDTO dto) {

        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        existente.setSenha(dto.getSenha());
        clienteRepository.save(existente);

        EmailRequestDTO email = new EmailRequestDTO();
        email.setTo(existente.getEmail());
        email.setSubject("🔐 Senha alterada com sucesso!");

        String html = """
    <div style="
        font-family: 'Helvetica Neue', Arial, sans-serif;
        max-width: 600px;
        margin: 0 auto;
        background: #1a1a1a;
        color: #ffffff;
    ">

        <!-- Header com branding -->
        <div style="
            background: #0f0f0f;
            padding: 40px 30px;
            text-align: center;
            border-bottom: 2px solid #2a2a2a;
        ">
            <div style="
                font-size: 28px;
                font-weight: 900;
                letter-spacing: 2px;
                color: #ffffff;
                margin-bottom: 8px;
            ">
                DROP CITY
            </div>
            <div style="
                font-size: 12px;
                letter-spacing: 1px;
                color: #7a7a7a;
                text-transform: uppercase;
            ">
                STREETWEAR LAB
            </div>
        </div>

        <!-- Conteúdo Principal -->
        <div style="padding: 40px 30px;">

            <h1 style="
                font-size: 32px;
                font-weight: 900;
                margin: 0 0 8px 0;
                letter-spacing: 1px;
                color: #ffffff;
            ">
                SENHA ALTERADA
            </h1>

            <p style="
                font-size: 14px;
                color: #b0b0b0;
                margin: 0 0 30px 0;
            ">
                Olá, <strong style="color: #ffffff;">%s</strong>
            </p>

            <!-- Status -->
            <div style="
                background: #1a1a1a;
                border-left: 3px solid #ffffff;
                padding: 16px;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 12px;
                    color: #b0b0b0;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                ">
                    Atualização de segurança
                </p>
                <p style="
                    margin: 8px 0 0 0;
                    font-size: 16px;
                    font-weight: 600;
                    color: #ffffff;
                ">
                    Sua senha foi alterada com sucesso.
                </p>
            </div>

            <!-- Aviso -->
            <div style="
                background: #0f0f0f;
                padding: 20px;
                border: 1px solid #2a2a2a;
                margin-bottom: 30px;
            ">
                <p style="
                    margin: 0;
                    font-size: 13px;
                    color: #b0b0b0;
                    line-height: 1.6;
                ">
                    Se você não realizou essa alteração, entre em contato com o suporte imediatamente.
                </p>
            </div>

            <!-- CTA -->
            <div style="text-align: center;">
                <a href="http://localhost:4200/login"
                   style="
                    background: #ffffff;
                    color: #000000;
                    padding: 16px 40px;
                    border-radius: 0;
                    text-decoration: none;
                    font-weight: 700;
                    display: inline-block;
                    font-size: 14px;
                    letter-spacing: 1px;
                    text-transform: uppercase;
                   ">
                    FAZER LOGIN
                </a>
            </div>

        </div>

        <!-- Footer -->
        <div style="
            background: #0f0f0f;
            padding: 30px;
            text-align: center;
            border-top: 1px solid #2a2a2a;
            font-size: 12px;
            color: #7a7a7a;
        ">
            <p style="margin: 0 0 10px 0;">
                Drop City - Streetwear Lab © 2026
            </p>
            <p style="margin: 0;">
                Segurança e estilo em primeiro lugar
            </p>
        </div>

    </div>
    """.formatted(existente.getNome());

        email.setBody(html);

        emailService.sendSimpleEmail(email);

        return "Senha alterada.";
    }

}
