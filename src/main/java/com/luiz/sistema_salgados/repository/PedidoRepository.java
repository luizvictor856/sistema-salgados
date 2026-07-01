package com.luiz.sistema_salgados.repository;

import com.luiz.sistema_salgados.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


// Repository responsável por realizar operações
// de leitura e gravação da entidade Pedido no banco.
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Retorna apenas os pedidos ativos.
    // Pedidos estornados não aparecem na listagem principal.
    List<Pedido> findByAtivoTrue();
}