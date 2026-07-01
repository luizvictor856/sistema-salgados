package com.luiz.sistema_salgados.patterns.decorator;

public class CheddarDecorator extends ItemPedidoDecorator {

    public CheddarDecorator(ItemPedido itemPedido) {
        super(itemPedido);
    }

    @Override
    public String getDescricao() {
        return itemPedido.getDescricao() + " + Cheddar";
    }

    @Override
    public Double getPreco() {
        return itemPedido.getPreco() + 3.0;
    }
}