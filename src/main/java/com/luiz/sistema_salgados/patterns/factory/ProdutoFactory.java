package com.luiz.sistema_salgados.patterns.factory;

import com.luiz.sistema_salgados.model.Produto;

public class ProdutoFactory {

    public Produto criarProduto(String sabor, Double preco, Integer estoque) {
        Produto produto = new Produto();

        produto.setSabor(sabor);
        produto.setPreco(preco);
        produto.setEstoque(estoque);

        return produto;
    }
}