package com.luiz.sistema_salgados.model;

import jakarta.persistence.*;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sabor;
    private Double preco;
    private Integer estoque;

    public Produto() {
    }

    public Produto(String sabor, Double preco, Integer estoque) {
        this.sabor = sabor;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Long getId() {
        return id;
    }

    public String getSabor() {
        return sabor;
    }

    public Double getPreco() {
        return preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}