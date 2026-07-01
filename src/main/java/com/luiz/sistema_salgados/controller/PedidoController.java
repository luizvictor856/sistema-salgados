package com.luiz.sistema_salgados.controller;

import com.luiz.sistema_salgados.model.Pedido;
import com.luiz.sistema_salgados.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Indica que esta classe é um Controller REST.
// Ela recebe as requisições vindas do Front-end.
@RestController

// Define que todas as rotas desta classe começam com /pedidos.
@RequestMapping("/pedidos")
public class PedidoController {

    // Injeta o PedidoService, responsável pelas regras de negócio.
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // Recebe a requisição POST enviada pelo JavaScript.
    // O JSON recebido é convertido automaticamente em um objeto Pedido.
    @PostMapping
    public Pedido salvar(@RequestBody Pedido pedido) {

        // Encaminha o pedido para a camada Service,
        // onde acontece toda a regra de negócio.
        return service.salvar(pedido);
    }

    // Lista todos os pedidos cadastrados.
    @GetMapping
    public List<Pedido> listarTodos() {
        return service.listarTodos();
    }

    // Realiza o estorno de um pedido.
    @PutMapping("/{id}/estornar")
    public void estornar(@PathVariable Long id) {
        service.estornar(id);
    }
}
