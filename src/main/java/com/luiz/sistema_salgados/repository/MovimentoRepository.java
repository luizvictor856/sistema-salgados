package com.luiz.sistema_salgados.repository;

import com.luiz.sistema_salgados.model.Movimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentoRepository extends JpaRepository<Movimento, Long> {

}