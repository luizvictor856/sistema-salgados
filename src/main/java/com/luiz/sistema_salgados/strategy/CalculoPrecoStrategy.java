package com.luiz.sistema_salgados.strategy;

import com.luiz.sistema_salgados.model.Produto;

public interface CalculoPrecoStrategy {

    Double calcular(Produto produto, Integer quantidade);
}