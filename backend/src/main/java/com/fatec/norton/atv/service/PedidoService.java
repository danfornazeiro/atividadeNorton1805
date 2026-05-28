package com.fatec.norton.atv.service;

import com.fatec.norton.atv.dto.EmailRequestDTO;
import com.fatec.norton.atv.dto.PedidoResponseDTO;
import com.fatec.norton.atv.model.carrinho.Carrinho;
import com.fatec.norton.atv.model.pedido.Pedido;
import com.fatec.norton.atv.model.produto.Produto;
import com.fatec.norton.atv.repository.CarrinhoRepository;
import com.fatec.norton.atv.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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

    @Transactional // Garante que toda a operação aconteça na mesma transação do banco
    public PedidoResponseDTO fazerPedido(UUID carrinhoId) {

        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho nao existe"));

        if (carrinho.getCliente() == null) {
            throw new RuntimeException("Não é possível fechar o pedido: este carrinho não possui um cliente associado!");
        }

        if (carrinho.getProdutos() == null || carrinho.getProdutos().isEmpty()) {
            throw new RuntimeException("Não é possível fechar o pedido: o carrinho está vazio.");
        }

        List<Produto> produtosDoPedido = new ArrayList<>(carrinho.getProdutos());

        Pedido pedido = new Pedido();
        pedido.setCarrinho(carrinho);
        pedido.setCliente(carrinho.getCliente());
        pedido.setProdutos(produtosDoPedido);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        EmailRequestDTO email = new EmailRequestDTO();
        email.setTo(carrinho.getCliente().getEmail());
        email.setSubject("🎉 Pedido realizado com sucesso!");

        StringBuilder produtosHtml = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;

        for (Produto p : produtosDoPedido) {
            total = total.add(p.getValor());

            produtosHtml.append("""
                <div style="
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding: 16px 0;
                    border-bottom: 1px solid #2a2a2a;
                ">
                    <span style="color: #ffffff; font-size: 14px; font-weight: 500;">%s</span>
                    <span style="color: #b0b0b0; font-size: 14px;">R$ %s</span>
                </div>
            """.formatted(
                    p.getNome(),
                    p.getValor().setScale(2, RoundingMode.HALF_UP)
            ));
        }

        String html = """
        <div style="
            font-family: 'Helvetica Neue', Arial, sans-serif;
            max-width: 600px;
            margin: 0 auto;
            background: #1a1a1a;
            color: #ffffff;
        ">
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

            <div style="padding: 40px 30px;">
                <h1 style="
                    font-size: 32px;
                    font-weight: 900;
                    margin: 0 0 8px 0;
                    letter-spacing: 1px;
                    color: #ffffff;
                ">
                    PEDIDO CONFIRMADO
                </h1>

                <p style="
                    font-size: 14px;
                    color: #b0b0b0;
                    margin: 0 0 30px 0;
                ">
                    Obrigado, <strong style="color: #ffffff;">%s</strong>
                </p>

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
                        Status do pedido
                    </p>
                    <p style="
                        margin: 8px 0 0 0;
                        font-size: 18px;
                        font-weight: 700;
                        color: #ffffff;
                    ">
                        Em processamento
                    </p>
                </div>

                <div style="margin-bottom: 30px;">
                    <h2 style="
                        font-size: 14px;
                        font-weight: 700;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                        color: #ffffff;
                        margin: 0 0 20px 0;
                    ">
                        Itens do pedido
                    </h2>
                    %s
                </div>

                <div style="
                    background: #0f0f0f;
                    padding: 20px;
                    margin-bottom: 30px;
                    border-top: 1px solid #2a2a2a;
                    border-bottom: 1px solid #2a2a2a;
                ">
                    <div style="
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                    ">
                        <span style="
                            font-size: 14px;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            color: #b0b0b0;
                            font-weight: 700;
                        ">
                            Total
                        </span>
                        <span style="
                            font-size: 24px;
                            font-weight: 900;
                            color: #ffffff;
                        ">
                            R$ %s
                        </span>
                    </div>
                </div>

                <div style="text-align: center; margin-bottom: 30px;">
                    <a href="https://frontend-p2-psi.vercel.app/orders"
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
                        transition: all 0.3s ease;
                       ">
                        ACOMPANHAR PEDIDO
                    </a>
                </div>
            </div>

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
                    Conectando moda urbana com quality
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
        PedidoResponseDTO respostaDTO = new PedidoResponseDTO(pedidoSalvo);
        carrinho.getProdutos().clear();
        carrinhoRepository.save(carrinho);

        return respostaDTO;
    }

    public void deletarPedido(UUID pedidoId){
        pedidoRepository.deleteById(pedidoId);
    }
}