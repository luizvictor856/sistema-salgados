package com.luiz.sistema_salgados.model;

import jakarta.persistence.*;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Produto produto;

    private Integer quantidade;

    private Double valorTotal;

    private Boolean ativo = true;

    private Boolean catupiry = false;
    private Boolean cheddar = false;

    public Pedido() {
    }

    public Pedido(Cliente cliente,
                  Produto produto,
                  Integer quantidade,
                  Double valorTotal) {

        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAtivo() {
    return ativo;
    }

    public void setAtivo(boolean ativo) {
     this.ativo = ativo;
    }

    public Boolean getCatupiry() {
    return catupiry;
}

public void setCatupiry(Boolean catupiry) {
    this.catupiry = catupiry;
}

public Boolean getCheddar() {
    return cheddar;
}

public void setCheddar(Boolean cheddar) {
    this.cheddar = cheddar;
}

    
}               