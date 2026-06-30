package com.luiz.sistema_salgados.controller;

import com.luiz.sistema_salgados.model.Pedido;
import com.luiz.sistema_salgados.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public Pedido salvar(@RequestBody Pedido pedido) {
        return service.salvar(pedido);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}/estornar")
    public void estornar(@PathVariable Long id) {
        service.estornar(id);
    }
}