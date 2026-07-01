package com.luiz.sistema_salgados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.luiz.sistema_salgados.model.Produto;
import com.luiz.sistema_salgados.repository.ProdutoRepository;

@SpringBootApplication
public class SistemaSalgadosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaSalgadosApplication.class, args);
    }

    @Bean
    CommandLineRunner carregarProdutos(ProdutoRepository repository) {
        return args -> {

            if (repository.count() == 0) {
                repository.save(new Produto("Coxinha", 10.0, 50));
                repository.save(new Produto("Pastel", 8.0, 50));
                repository.save(new Produto("Kibe", 7.0, 50));
                repository.save(new Produto("Esfiha", 9.0, 50));
            }

        };
    }
}