package com.luiz.sistema_salgados.patterns.observer;

import com.luiz.sistema_salgados.model.Pedido;

public interface PedidoObserver {

    void atualizar(Pedido pedido);
}