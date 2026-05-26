package com.fatec.norton.atv.service;

import com.fatec.norton.atv.dto.EmailRequestDTO;
import com.fatec.norton.atv.dto.PedidoResponseDTO;
import com.fatec.norton.atv.model.carrinho.Carrinho;
import com.fatec.norton.atv.model.pedido.Pedido;
import com.fatec.norton.atv.model.produto.Produto;
import com.fatec.norton.atv.repository.CarrinhoRepository;
import com.fatec.norton.atv.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class PedidoService {

    private final CarrinhoRepository carrinhoRepository;
    private final PedidoRepository pedidoRepository;
    private final EmailService emailService;


    public PedidoService(CarrinhoRepository carrinhoRepository, PedidoRepository pedidoRepository, EmailService emailService) {
        this.carrinhoRepository = carrinhoRepository;
        this.pedidoRepository = pedidoRepository;
        this.emailService = emailService;
    }


    public PedidoResponseDTO fazerPedido(UUID carrinhoId) {

        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho nao existe"));

        Pedido pedido = new Pedido();
        pedido.setCarrinho(carrinho);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        EmailRequestDTO email = new EmailRequestDTO();
        email.setTo(carrinho.getCliente().getEmail());
        email.setSubject("🎉 Pedido realizado com sucesso!");

        StringBuilder produtosHtml = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;

        for (Produto p : carrinho.getProdutos()) {

            total = total.add(p.getValor());

            produtosHtml.append("""
                <div style="
                    background:#ffffff;
                    border-radius:12px;
                    padding:15px;
                    margin-bottom:12px;
                    box-shadow:0 2px 8px rgba(0,0,0,0.08);
                ">
                    <h3 style="margin:0; color:#2563eb;">%s</h3>
                    <p style="margin:5px 0; color:#333;">
                        Preço: R$ %s
                    </p>
                </div>
            """.formatted(
                    p.getNome(),
                    p.getValor().setScale(2, RoundingMode.HALF_UP)
            ));
        }

        String html = """
        <div style="
            font-family: Arial, sans-serif;
            max-width: 650px;
            margin: auto;
            background:#f4f4f4;
            padding:30px;
            border-radius:12px;
        ">

            <div style="
                background:white;
                padding:30px;
                border-radius:12px;
                box-shadow:0 2px 10px rgba(0,0,0,0.1);
            ">

                <h1 style="text-align:center; color:#16a34a;">
                    🎉 Pedido confirmado!
                </h1>

                <p style="font-size:16px; color:#333;">
                    Olá <strong>%s</strong>, seu pedido foi recebido e já está sendo processado 🚚
                </p>

                <div style="
                    background:#e0f2fe;
                    padding:12px;
                    border-radius:10px;
                    margin:20px 0;
                ">
                    <strong>Status:</strong> Processando pedido
                </div>

                <h2 style="color:#111;">🛍️ Produtos</h2>

                %s

                <div style="
                    margin-top:20px;
                    padding:15px;
                    background:#f0fdf4;
                    border-radius:10px;
                ">
                    <h2 style="margin:0; color:#166534;">
                        💰 Total: R$ %s
                    </h2>
                </div>

                <div style="text-align:center; margin-top:30px;">
                    <a href="http://localhost:4200/pedidos"
                       style="
                        background:#16a34a;
                        color:white;
                        padding:14px 26px;
                        border-radius:8px;
                        text-decoration:none;
                        font-weight:bold;
                        display:inline-block;
                       ">
                        📦 Acompanhar pedido
                    </a>
                </div>

                <p style="text-align:center; margin-top:20px; font-size:12px; color:#777;">
                    Obrigado por comprar com a gente 💙
                </p>

            </div>
        </div>
        """.formatted(
                carrinho.getCliente().getNome(),
                produtosHtml.toString(),
                total.setScale(2, RoundingMode.HALF_UP)
        );

        email.setBody(html);

        emailService.sendSimpleEmail(email);

        return new PedidoResponseDTO(pedidoSalvo);
    }


    public void deletarPedido(UUID pedidoId){
        pedidoRepository.deleteById(pedidoId);
    }

}
