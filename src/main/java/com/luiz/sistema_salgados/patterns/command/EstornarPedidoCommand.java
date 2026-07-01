package com.luiz.sistema_salgados.patterns.command;

import com.luiz.sistema_salgados.service.PedidoService;

public class EstornarPedidoCommand implements PedidoCommand {

    private final PedidoService pedidoService;
    private final Long idPedido;

    public EstornarPedidoCommand(PedidoService pedidoService, Long idPedido) {
        this.pedidoService = pedidoService;
        this.idPedido = idPedido;
    }

    @Override
    public void executar() {
        pedidoService.estornarInterno(idPedido);
    }
}