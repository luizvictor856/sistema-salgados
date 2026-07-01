package com.luiz.sistema_salgados.patterns.observer;

import com.luiz.sistema_salgados.model.Pedido;

public class HistoricoObserver implements PedidoObserver {

    @Override
    public void atualizar(Pedido pedido) {
        System.out.println("Observer: Pedido " + pedido.getId() + " registrado no histórico.");
    }
}