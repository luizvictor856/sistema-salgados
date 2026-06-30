package com.luiz.sistema_salgados.repository;

import com.luiz.sistema_salgados.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByAtivoTrue();
}