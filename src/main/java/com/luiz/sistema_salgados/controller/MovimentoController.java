package com.luiz.sistema_salgados.controller;

import com.luiz.sistema_salgados.model.Movimento;
import com.luiz.sistema_salgados.service.MovimentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentos")
public class MovimentoController {

    private final MovimentoService service;

    public MovimentoController(MovimentoService service) {
        this.service = service;
    }

    @PostMapping
    public Movimento salvar(@RequestBody Movimento movimento) {
        return service.salvar(movimento);
    }

    @GetMapping
    public List<Movimento> listarTodos() {
        return service.listarTodos();
    }
}       