package com.luiz.sistema_salgados.service;

import com.luiz.sistema_salgados.model.Pedido;
import com.luiz.sistema_salgados.model.Produto;
import com.luiz.sistema_salgados.repository.PedidoRepository;
import com.luiz.sistema_salgados.repository.ProdutoRepository;
import com.luiz.sistema_salgados.strategy.CalculoPrecoStrategy;
import com.luiz.sistema_salgados.strategy.PrecoNormalStrategy;
import com.luiz.sistema_salgados.strategy.PrecoPromocaoStrategy;

import org.springframework.stereotype.Service;
import com.luiz.sistema_salgados.model.Movimento;
import com.luiz.sistema_salgados.repository.MovimentoRepository;


import java.util.List;


@Service //injeção de dependência
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentoRepository movimentoRepository;

    public PedidoService(PedidoRepository pedidoRepository,ProdutoRepository produtoRepository,MovimentoRepository movimentoRepository) {

    this.pedidoRepository = pedidoRepository;
    this.produtoRepository = produtoRepository;
    this.movimentoRepository = movimentoRepository;
}

    public Pedido salvar(Pedido pedido) {

        Produto produto = produtoRepository.findById(pedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getEstoque() < pedido.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente");
        }

        produto.setEstoque(produto.getEstoque() - pedido.getQuantidade());

        pedido.setProduto(produto);
        CalculoPrecoStrategy strategy;

        //desconto se pedido for igual ou maior que 10 unidades
        
        if (pedido.getQuantidade() >= 10) {
            strategy = new PrecoPromocaoStrategy();
        } else {
            strategy = new PrecoNormalStrategy();
        }        
        
        pedido.setValorTotal(
            strategy.calcular(produto, pedido.getQuantidade())
        );

        Movimento movimento = new Movimento(
        "SAIDA",
        pedido.getQuantidade(),
        pedido.getValorTotal()
        );

        movimentoRepository.save(movimento);

        produtoRepository.save(produto);

        return pedidoRepository.save(pedido);
    }
    
    public void estornar(Long idPedido) {

    Pedido pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

    if (!pedido.isAtivo()) {
        throw new RuntimeException("Este pedido já foi estornado.");
    }

    Produto produto = pedido.getProduto();

    produto.setEstoque(
            produto.getEstoque() + pedido.getQuantidade()
    );

    pedido.setAtivo(false);

    Movimento movimento = new Movimento(
        "ESTORNO",
        pedido.getQuantidade(),
        pedido.getValorTotal()
    );

        movimentoRepository.save(movimento);
        produtoRepository.save(produto);
        pedidoRepository.save(pedido);

    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findByAtivoTrue();
    }
}