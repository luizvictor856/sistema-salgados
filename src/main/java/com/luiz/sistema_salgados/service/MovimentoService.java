package com.luiz.sistema_salgados.service;

import com.luiz.sistema_salgados.model.Movimento;
import com.luiz.sistema_salgados.repository.MovimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentoService {

    private final MovimentoRepository repository;

    public MovimentoService(MovimentoRepository repository) {
        this.repository = repository;
    }

    public Movimento salvar(Movimento movimento) {
        return repository.save(movimento);
    }

    public List<Movimento> listarTodos() {
        return repository.findAll();
    }
}       