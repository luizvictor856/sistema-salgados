package com.luiz.sistema_salgados.patterns.decorator;

public abstract class ItemPedidoDecorator implements ItemPedido {

    protected ItemPedido itemPedido;

    public ItemPedidoDecorator(ItemPedido itemPedido) {
        this.itemPedido = itemPedido;
    }
}