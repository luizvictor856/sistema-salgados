package com.luiz.sistema_salgados.service;

import com.luiz.sistema_salgados.model.Produto;
import com.luiz.sistema_salgados.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import com.luiz.sistema_salgados.patterns.factory.ProdutoFactory;               

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto salvar(Produto produto) {

    ProdutoFactory factory = new ProdutoFactory();

    Produto novoProduto = factory.criarProduto(
            produto.getSabor(),
            produto.getPreco(),
            produto.getEstoque()
    );

        return repository.save(novoProduto);
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }
}