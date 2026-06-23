package com.luiz.sistema_salgados.controller;

import com.luiz.sistema_salgados.model.Produto;
import com.luiz.sistema_salgados.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return service.salvar(produto);
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return service.listarTodos();
    }
}   