package com.luiz.sistema_salgados.patterns.decorator;

public class CatupiryDecorator extends ItemPedidoDecorator {

    public CatupiryDecorator(ItemPedido itemPedido) {
        super(itemPedido);
    }

    @Override
    public String getDescricao() {
        return itemPedido.getDescricao() + " + Catupiry";
    }

    @Override
    public Double getPreco() {
        return itemPedido.getPreco() + 2.0;
    }
}