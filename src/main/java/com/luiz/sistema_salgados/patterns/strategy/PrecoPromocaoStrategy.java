package com.luiz.sistema_salgados.patterns.strategy;

import com.luiz.sistema_salgados.model.Produto;

public class PrecoPromocaoStrategy implements CalculoPrecoStrategy {

    @Override
    public Double calcular(Produto produto, Integer quantidade) {
        return produto.getPreco() * quantidade * 0.9;
    }
}