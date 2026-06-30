async function cadastrarProduto() {
    const produto = {
        sabor: document.getElementById("sabor").value,
        preco: parseFloat(document.getElementById("preco").value),
        estoque: parseInt(document.getElementById("estoque").value)
    };

    const resposta = await fetch("/produtos", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(produto)
    });

    if (resposta.ok) {
        alert("Produto cadastrado com sucesso!");

        document.getElementById("sabor").value = "";
        document.getElementById("preco").value = "";
        document.getElementById("estoque").value = "";

        await listarProdutos();
    } else {
        alert("Erro ao cadastrar produto.");
    }
}

async function listarProdutos() {
    const resposta = await fetch("/produtos");
    const produtos = await resposta.json();

    const lista = document.getElementById("listaProdutos");

    lista.innerHTML = "";

    produtos.forEach(produto => {
        lista.innerHTML += `
            <tr>
                <td>${produto.id}</td>
                <td>${produto.sabor}</td>
                <td>R$ ${produto.preco}</td>
                <td>${produto.estoque}</td>
            </tr>
        `;
    });
}

listarProdutos();

async function fazerPedido() {
    const pedido = {
        produto: {
            id: parseInt(document.getElementById("produtoId").value)
        },
        quantidade: parseInt(document.getElementById("quantidadePedido").value)
    };

    await fetch("/pedidos", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(pedido)
    });

    alert("Pedido realizado com sucesso!");

    document.getElementById("produtoId").value = "";
    document.getElementById("quantidadePedido").value = "";

    listarPedidos();
    listarProdutos();
    listarMovimentos();
}

async function listarPedidos() {
    const resposta = await fetch("/pedidos");
    const pedidos = await resposta.json();

    const lista = document.getElementById("listaPedidos");

    lista.innerHTML = "";

    pedidos.forEach(pedido => {
        lista.innerHTML += `
            <tr>
                <td>${pedido.id}</td>
                <td>${pedido.produto ? pedido.produto.sabor : "-"}</td>
                <td>${pedido.quantidade}</td>
                <td>R$ ${pedido.valorTotal}</td>
                <td>
                    <button onclick="estornarPedido(${pedido.id})">
                        Estornar
                    </button>
                </td>
            </tr>
        `;
    });
}

listarPedidos();

async function listarMovimentos() {
    const resposta = await fetch("/movimentos");
    const movimentos = await resposta.json();

    const lista = document.getElementById("listaMovimentos");

    lista.innerHTML = "";

    movimentos.forEach(movimento => {
        lista.innerHTML += `
            <tr>
                <td>${movimento.id}</td>
                <td>${movimento.tipo}</td>
                <td>${movimento.quantidade}</td>
                <td>R$ ${movimento.valor}</td>
            </tr>
        `;
    });
}

listarMovimentos();


async function cadastrarCliente() {

    const cliente = {
        nome: document.getElementById("nomeCliente").value,
        email: document.getElementById("emailCliente").value,
        senha: document.getElementById("senhaCliente").value
    };

    await fetch("/clientes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cliente)
    });

    alert("Cliente cadastrado com sucesso!");

    document.getElementById("nomeCliente").value = "";
    document.getElementById("emailCliente").value = "";
    document.getElementById("senhaCliente").value = "";
}

async function listarClientes() {

    const resposta = await fetch("/clientes");
    const clientes = await resposta.json();

    const lista = document.getElementById("listaClientes");

    lista.innerHTML = "";

    clientes.forEach(cliente => {
        lista.innerHTML += `
            <tr>
                <td>${cliente.id}</td>
                <td>${cliente.nome}</td>
                <td>${cliente.email}</td>
            </tr>
        `;
    });
}

listarClientes();

async function entradaEstoque() {
    const produtoId = parseInt(document.getElementById("produtoEntradaId").value);
    const quantidade = parseInt(document.getElementById("quantidadeEntrada").value);

    const produto = {
        id: produtoId
    };

    const movimento = {
        tipo: "ENTRADA",
        quantidade: quantidade,
        valor: 0,
        produto: produto
    };

    await fetch("/movimentos", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(movimento)
    });

    alert("Entrada de estoque registrada!");

    document.getElementById("produtoEntradaId").value = "";
    document.getElementById("quantidadeEntrada").value = "";

    listarProdutos();
    listarMovimentos();
}

async function estornarPedido(id) {

    await fetch(`/pedidos/${id}/estornar`, {
        method: "PUT"
    });

    alert("Pedido estornado com sucesso!");

    listarPedidos();
    listarProdutos();
    listarMovimentos();

}