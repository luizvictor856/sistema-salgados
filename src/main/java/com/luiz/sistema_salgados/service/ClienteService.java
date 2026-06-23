package com.luiz.sistema_salgados.service;

// Importa a entidade Cliente
import com.luiz.sistema_salgados.model.Cliente;

// Importa o Repository responsável por acessar o banco
import com.luiz.sistema_salgados.repository.ClienteRepository;

// Marca esta classe como um Service do Spring
import org.springframework.stereotype.Service;

// Utilizado para trabalhar com listas
import java.util.List;

// Informa ao Spring que esta classe contém regras de negócio
@Service
public class ClienteService {

    /*
     * O repository é responsável por acessar o banco.
     * O "final" significa que ele será inicializado
     * apenas uma vez no construtor.
     */
    private final ClienteRepository repository;

    /*
     * Injeção de dependência.
     *
     * O Spring cria automaticamente um ClienteRepository
     * e entrega para esta classe.
     */
    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    /*
     * Salva um cliente no banco.
     *
     * Recebe um objeto Cliente
     * e utiliza o Repository para persistir.
     */
    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    /*
     * Busca todos os clientes cadastrados.
     *
     * O findAll() é fornecido automaticamente
     * pelo JpaRepository.
     */
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }
}