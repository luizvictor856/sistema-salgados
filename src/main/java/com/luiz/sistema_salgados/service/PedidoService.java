package com.luiz.sistema_salgados.service;

import com.luiz.sistema_salgados.model.Pedido;
import com.luiz.sistema_salgados.model.Produto;
import com.luiz.sistema_salgados.model.Movimento;

import com.luiz.sistema_salgados.repository.PedidoRepository;
import com.luiz.sistema_salgados.repository.ProdutoRepository;
import com.luiz.sistema_salgados.repository.MovimentoRepository;

import com.luiz.sistema_salgados.patterns.strategy.CalculoPrecoStrategy;
import com.luiz.sistema_salgados.patterns.strategy.PrecoNormalStrategy;
import com.luiz.sistema_salgados.patterns.strategy.PrecoPromocaoStrategy;

import com.luiz.sistema_salgados.patterns.decorator.ItemPedido;
import com.luiz.sistema_salgados.patterns.decorator.ProdutoBase;
import com.luiz.sistema_salgados.patterns.decorator.CatupiryDecorator;
import com.luiz.sistema_salgados.patterns.decorator.CheddarDecorator;

import com.luiz.sistema_salgados.patterns.observer.PedidoObserver;
import com.luiz.sistema_salgados.patterns.observer.HistoricoObserver;

import com.luiz.sistema_salgados.patterns.command.PedidoCommand;
import com.luiz.sistema_salgados.patterns.command.EstornarPedidoCommand;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    // Repositories usados para acessar o banco de dados.
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentoRepository movimentoRepository;

    // Lista de observadores usados no Observer Pattern.
    private final List<PedidoObserver> observers = new ArrayList<>();

    // Injeção de dependência: o Spring entrega os repositories prontos.
    public PedidoService(
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            MovimentoRepository movimentoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.movimentoRepository = movimentoRepository;

        // Observer Pattern:
        // registra um observador que será avisado quando um pedido for salvo.
        observers.add(new HistoricoObserver());
    }

    // =========================================================
    // FLUXO PRINCIPAL DA COMPRA
    // =========================================================
    public Pedido salvar(Pedido pedido) {

        // Busca o produto escolhido pelo cliente no banco.
        Produto produto = produtoRepository.findById(pedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Regra de negócio:
        // impede vender uma quantidade maior do que existe no estoque.
        if (produto.getEstoque() < pedido.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente");
        }

        // Atualiza o estoque, removendo a quantidade comprada.
        produto.setEstoque(produto.getEstoque() - pedido.getQuantidade());

        // Associa o produto encontrado no banco ao pedido.
        pedido.setProduto(produto);

        // =====================================================
        // STRATEGY PATTERN
        // =====================================================
        // Define qual estratégia de cálculo de preço será usada.
        // Se a quantidade for 10 ou mais, aplica promoção.
        // Caso contrário, aplica preço normal.
        CalculoPrecoStrategy strategy;

        if (pedido.getQuantidade() >= 10) {
            strategy = new PrecoPromocaoStrategy();
        } else {
            strategy = new PrecoNormalStrategy();
        }

        // =====================================================
        // DECORATOR PATTERN
        // =====================================================
        // Cria o produto base com o preço calculado pela Strategy.
        ItemPedido itemPedido = new ProdutoBase(
                produto.getSabor(),
                strategy.calcular(produto, pedido.getQuantidade())
        );

        // Se o cliente escolheu Catupiry, adiciona esse extra ao pedido.
        if (Boolean.TRUE.equals(pedido.getCatupiry())) {
            itemPedido = new CatupiryDecorator(itemPedido);
        }

        // Se o cliente escolheu Cheddar, adiciona esse extra ao pedido.
        if (Boolean.TRUE.equals(pedido.getCheddar())) {
            itemPedido = new CheddarDecorator(itemPedido);
        }

        // Define o valor final do pedido após estratégia e adicionais.
        pedido.setValorTotal(itemPedido.getPreco());

        // Mostra no terminal quais adicionais foram aplicados.
        System.out.println("Decorator aplicado: " + itemPedido.getDescricao());

        // Cria uma movimentação de saída para registrar a compra.
        Movimento movimento = new Movimento(
                "SAIDA",
                pedido.getQuantidade(),
                pedido.getValorTotal()
        );

        // Salva a movimentação no banco.
        movimentoRepository.save(movimento);

        // Salva o produto com o estoque atualizado.
        produtoRepository.save(produto);

        // Salva o pedido no banco.
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // =====================================================
        // OBSERVER PATTERN
        // =====================================================
        // Após salvar o pedido, todos os observadores são notificados.
        for (PedidoObserver observer : observers) {
            observer.atualizar(pedidoSalvo);
        }

        // Retorna o pedido salvo para o Controller.
        return pedidoSalvo;
    }

    // =========================================================
    // COMMAND PATTERN
    // =========================================================
    // O estorno é executado por meio de um comando.
    public void estornar(Long idPedido) {
        PedidoCommand command = new EstornarPedidoCommand(this, idPedido);
        command.executar();
    }

    // Método interno chamado pelo Command.
    // Ele contém a lógica real do estorno.
    public void estornarInterno(Long idPedido) {

        // Busca o pedido no banco.
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Impede estornar o mesmo pedido mais de uma vez.
        if (!pedido.isAtivo()) {
            throw new RuntimeException("Este pedido já foi estornado.");
        }

        // Recupera o produto do pedido.
        Produto produto = pedido.getProduto();

        // Devolve a quantidade comprada ao estoque.
        produto.setEstoque(
                produto.getEstoque() + pedido.getQuantidade()
        );

        // Marca o pedido como inativo.
        // Assim ele deixa de aparecer na lista de pedidos ativos.
        pedido.setAtivo(false);

        // Cria uma movimentação de ESTORNO.
        Movimento movimento = new Movimento(
                "ESTORNO",
                pedido.getQuantidade(),
                pedido.getValorTotal()
        );

        // Salva a movimentação de estorno.
        movimentoRepository.save(movimento);

        // Salva o produto com estoque devolvido.
        produtoRepository.save(produto);

        // Salva o pedido como inativo.
        pedidoRepository.save(pedido);
    }

    // Lista apenas pedidos ativos.
    // Pedidos estornados não aparecem mais na lista principal.
    public List<Pedido> listarTodos() {
        return pedidoRepository.findByAtivoTrue();
    }
}