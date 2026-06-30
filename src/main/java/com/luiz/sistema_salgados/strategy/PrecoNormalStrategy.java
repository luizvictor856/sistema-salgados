package com.luiz.sistema_salgados.strategy;

import com.luiz.sistema_salgados.model.Produto;

public class PrecoNormalStrategy implements CalculoPrecoStrategy {

    @Override
    public Double calcular(Produto produto, Integer quantidade) {
        return produto.getPreco() * quantidade;
    }
}