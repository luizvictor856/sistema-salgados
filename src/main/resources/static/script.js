// ===== AUTH =====

function mostrarAuth(id) {
    document.querySelectorAll('.tela-auth').forEach(t => t.classList.remove('ativa'));
    document.getElementById(id).classList.add('ativa');
}

function fazerLogin() {
    const email = document.getElementById('loginEmail').value.trim();
    const senha = document.getElementById('loginSenha').value.trim();
    if (!email || !senha) { alert('Preencha e-mail e senha.'); return; }

    // Adapte aqui se tiver rota real de autenticação no back-end
    document.getElementById('nomeUsuario').textContent = email.split('@')[0];
    document.querySelectorAll('.tela-auth').forEach(t => t.classList.remove('ativa'));
    document.getElementById('telaApp').classList.add('ativo');
    irPara('dashboard');
}

function sair() {
    document.getElementById('telaApp').classList.remove('ativo');
    document.getElementById('loginEmail').value = '';
    document.getElementById('loginSenha').value = '';
    mostrarAuth('telaLogin');
}

// ===== NAVEGAÇÃO =====

const paginas = {
    dashboard:  { sec: 'secDashboard',  titulo: 'Dashboard' },
    produtos:   { sec: 'secProdutos',   titulo: 'Produtos' },
    pedidos:    { sec: 'secPedidos',    titulo: 'Pedidos' },
    extrato:    { sec: 'secExtrato',    titulo: 'Extrato' },
    relatorios: { sec: 'secRelatorios', titulo: 'Relatórios' },
};

function irPara(pagina) {
    document.querySelectorAll('.secao').forEach(s => s.classList.remove('ativa'));
    document.querySelectorAll('.sidebar li').forEach(l => l.classList.remove('ativo'));

    const p = paginas[pagina];
    document.getElementById(p.sec).classList.add('ativa');
    document.getElementById('topoTitulo').textContent = p.titulo;

    // Marca o li ativo
    const lis = document.querySelectorAll('.sidebar li');
    const mapa = ['dashboard','produtos','pedidos','extrato','relatorios'];
    const idx = mapa.indexOf(pagina);
    if (idx >= 0 && lis[idx]) lis[idx].classList.add('ativo');

    // Carrega dados da seção
    if (pagina === 'dashboard')  { listarPedidosDash(); listarProdutosDash(); }
    if (pagina === 'produtos')   listarProdutos();
    if (pagina === 'pedidos')    listarPedidos();
    if (pagina === 'extrato')    listarMovimentos();
    if (pagina === 'relatorios') listarClientes();
}

// ===== DASHBOARD =====

async function listarPedidosDash() {
    const resposta = await fetch('/pedidos');
    const pedidos = await resposta.json();
    const lista = document.getElementById('listaPedidosDash');
    lista.innerHTML = '';
    pedidos.forEach(pedido => {
        lista.innerHTML += `
            <tr>
                <td>${pedido.id}</td>
                <td>${pedido.produto ? pedido.produto.sabor : '-'}</td>
                <td>${pedido.quantidade}</td>
                <td>R$ ${pedido.valorTotal}</td>
                <td><button onclick="estornarPedido(${pedido.id})">Estornar</button></td>
            </tr>`;
    });
}

async function listarProdutosDash() {
    const resposta = await fetch('/produtos');
    const produtos = await resposta.json();
    const lista = document.getElementById('listaProdutosDash');
    lista.innerHTML = '';
    produtos.forEach(produto => {
        lista.innerHTML += `
            <tr>
                <td>${produto.id}</td>
                <td>${produto.sabor}</td>
                <td>R$ ${produto.preco}</td>
                <td>${produto.estoque}</td>
            </tr>`;
    });
}

// ===== PRODUTOS =====

async function cadastrarProduto() {
    const produto = {
        sabor: document.getElementById('sabor').value,
        preco: parseFloat(document.getElementById('preco').value),
        estoque: parseInt(document.getElementById('estoque').value)
    };

    const resposta = await fetch('/produtos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(produto)
    });

    if (resposta.ok) {
        alert('Produto cadastrado com sucesso!');
        document.getElementById('sabor').value = '';
        document.getElementById('preco').value = '';
        document.getElementById('estoque').value = '';
        await listarProdutos();
    } else {
        alert('Erro ao cadastrar produto.');
    }
}

async function listarProdutos() {
    const resposta = await fetch('/produtos');
    const produtos = await resposta.json();
    const lista = document.getElementById('listaProdutos');
    lista.innerHTML = '';
    produtos.forEach(produto => {
        lista.innerHTML += `
            <tr>
                <td>${produto.id}</td>
                <td>${produto.sabor}</td>
                <td>R$ ${produto.preco}</td>
                <td>${produto.estoque}</td>
            </tr>`;
    });
}

// ===== PEDIDOS =====

async function fazerPedido() {
    const pedido = {
        produto: { id: parseInt(document.getElementById('produtoId').value) },
        quantidade: parseInt(document.getElementById('quantidadePedido').value)
    };

    await fetch('/pedidos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pedido)
    });

    alert('Pedido realizado com sucesso!');
    document.getElementById('produtoId').value = '';
    document.getElementById('quantidadePedido').value = '';
    listarPedidos();
    listarMovimentos();
}

async function listarPedidos() {
    const resposta = await fetch('/pedidos');
    const pedidos = await resposta.json();
    const lista = document.getElementById('listaPedidos');
    lista.innerHTML = '';
    pedidos.forEach(pedido => {
        lista.innerHTML += `
            <tr>
                <td>${pedido.id}</td>
                <td>${pedido.produto ? pedido.produto.sabor : '-'}</td>
                <td>${pedido.quantidade}</td>
                <td>R$ ${pedido.valorTotal}</td>
                <td><button onclick="estornarPedido(${pedido.id})">Estornar</button></td>
            </tr>`;
    });
}

async function estornarPedido(id) {
    await fetch(`/pedidos/${id}/estornar`, { method: 'PUT' });
    alert('Pedido estornado com sucesso!');
    listarPedidos();
    listarMovimentos();
}

// ===== EXTRATO =====

async function listarMovimentos() {
    const resposta = await fetch('/movimentos');
    const movimentos = await resposta.json();
    const lista = document.getElementById('listaMovimentos');
    lista.innerHTML = '';
    movimentos.forEach(movimento => {
        lista.innerHTML += `
            <tr>
            <td>${movimento.id}</td>
            <td style="color: ${movimento.tipo === 'SAIDA' ? '#2ecc71' : movimento.tipo === 'ESTORNO' ? '#e74c3c' : 'white'}; font-weight: bold;">
            ${movimento.tipo}
            </td>
            <td>${movimento.quantidade}</td>
            <td>R$ ${movimento.valor}</td>
    </tr>`;
    });
}

// ===== CLIENTES =====

async function cadastrarCliente() {
    const cliente = {
        nome: document.getElementById('nomeCliente').value,
        email: document.getElementById('emailCliente').value,
        senha: document.getElementById('senhaCliente').value
    };

    await fetch('/clientes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(cliente)
    });

    alert('Conta criada! Faça login para continuar.');
    document.getElementById('nomeCliente').value = '';
    document.getElementById('emailCliente').value = '';
    document.getElementById('senhaCliente').value = '';
    mostrarAuth('telaLogin');
}

async function listarClientes() {
    const resposta = await fetch('/clientes');
    const clientes = await resposta.json();
    const lista = document.getElementById('listaClientes');
    lista.innerHTML = '';
    clientes.forEach(cliente => {
        lista.innerHTML += `
            <tr>
                <td>${cliente.id}</td>
                <td>${cliente.nome}</td>
                <td>${cliente.email}</td>
            </tr>`;
    });
}

// ===== ESTOQUE =====

async function entradaEstoque() {
    const produtoId = parseInt(document.getElementById('produtoEntradaId').value);
    const quantidade = parseInt(document.getElementById('quantidadeEntrada').value);

    const movimento = {
        tipo: 'ENTRADA',
        quantidade: quantidade,
        valor: 0,
        produto: { id: produtoId }
    };

    await fetch('/movimentos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(movimento)
    });

    alert('Entrada de estoque registrada!');
    document.getElementById('produtoEntradaId').value = '';
    document.getElementById('quantidadeEntrada').value = '';
    listarProdutos();
    listarMovimentos();
}