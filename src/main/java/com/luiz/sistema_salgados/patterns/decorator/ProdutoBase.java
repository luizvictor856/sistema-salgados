package com.luiz.sistema_salgados.patterns.decorator;

public class ProdutoBase implements ItemPedido {

    private final String descricao;
    private final Double preco;

    public ProdutoBase(String descricao, Double preco) {
        this.descricao = descricao;
        this.preco = preco;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public Double getPreco() {
        return preco;
    }
}