package com.luiz.sistema_salgados.model;

import jakarta.persistence.*;

@Entity
// Define que esta classe representa a tabela Pedido no banco de dados.
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Chave primária gerada automaticamente pelo banco.
    private Long id;

    @ManyToOne
    // Um cliente pode realizar vários pedidos.
    private Cliente cliente;

    @ManyToOne
    // Cada pedido está associado a um produto.
    private Produto produto;

    // Quantidade comprada pelo cliente.
    private Integer quantidade;

    // Valor final do pedido calculado pelo PedidoService.
    private Double valorTotal;

    // Indica se o pedido está ativo ou foi estornado.
    private Boolean ativo = true;

    // Adicionais escolhidos pelo cliente.
    // Esses campos são utilizados pelo Decorator Pattern.
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

    // ===========================
    // GETTERS E SETTERS
    // Responsáveis por acessar e modificar
    // os atributos da entidade.
    // ===========================

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