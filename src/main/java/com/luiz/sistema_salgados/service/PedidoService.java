package com.luiz.sistema_salgados.service;

import com.luiz.sistema_salgados.model.Pedido;
import com.luiz.sistema_salgados.model.Produto;
import com.luiz.sistema_salgados.patterns.strategy.CalculoPrecoStrategy;
import com.luiz.sistema_salgados.patterns.strategy.PrecoNormalStrategy;
import com.luiz.sistema_salgados.patterns.strategy.PrecoPromocaoStrategy;
import com.luiz.sistema_salgados.repository.PedidoRepository;
import com.luiz.sistema_salgados.repository.ProdutoRepository;

import org.springframework.stereotype.Service;
import com.luiz.sistema_salgados.model.Movimento;
import com.luiz.sistema_salgados.repository.MovimentoRepository;
import com.luiz.sistema_salgados.patterns.observer.PedidoObserver;
import com.luiz.sistema_salgados.patterns.observer.HistoricoObserver;
import java.util.ArrayList;


import java.util.List;


@Service //injeção de dependência
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentoRepository movimentoRepository;

    private final List<PedidoObserver> observers = new ArrayList<>();

    public PedidoService(PedidoRepository pedidoRepository,ProdutoRepository produtoRepository,MovimentoRepository movimentoRepository) {

    this.pedidoRepository = pedidoRepository;
    this.produtoRepository = produtoRepository;
    this.movimentoRepository = movimentoRepository;

    observers.add(new HistoricoObserver());
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

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        for (PedidoObserver observer : observers) {
            observer.atualizar(pedidoSalvo);
        }

        return pedidoSalvo;
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