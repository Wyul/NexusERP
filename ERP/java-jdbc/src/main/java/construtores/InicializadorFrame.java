package construtores;

import java.util.Scanner;

import gestao.estoque.CadastroProduto;
import gestao.venda.VisualizarVendaNFE;
import modelo.agendamento.Agendamento;
import modelo.agendamento.VisualizarAgendamentos;
import modelo.dominio.dao.conexao.CadastroFornecedor;
import modelo.dominio.dao.conexao.Conexao;
import modelo.dominio.dao.conexao.VisualizarClientes;
import modelo.dominio.dao.conexao.VisualizarFornecedores;
import modelo.dominio.dao.usuario.ListarUsuario;

/**
 * Classe responsável por inicializar os componentes principais do sistema.
 * Essa classe atua como um construtor de dependências, criando instâncias
 * dos módulos necessários para o funcionamento da aplicação.
 */
public class InicializadorFrame {    

    private CadastroFornecedor cadastroFornecedor;
    private CadastroProduto cadastroProduto;
    private VisualizarClientes visualizarClientes;
    private VisualizarFornecedores visualizarFornecedores;
    private VisualizarAgendamentos visualizarAgendamentos;
    private Agendamento agendamento;
    private VisualizarVendaNFE visualizarVendaNFE;
    private ListarUsuario listarUsuario;

    /**
     * Construtor da classe `InicializadorFrame`.
     * Inicializa todas as dependências necessárias para o funcionamento do sistema.
     *
     * @param conexao Objeto de conexão com o banco de dados.
     * @param scanner Scanner para entrada de dados do usuário.
     * @throws Exception Lança uma exceção caso a inicialização de algum componente falhe.
     */
    public InicializadorFrame(Conexao conexao, Scanner scanner) throws Exception {
        cadastroFornecedor = new CadastroFornecedor(conexao);
        cadastroProduto = new CadastroProduto(conexao);
        visualizarClientes = new VisualizarClientes(conexao);
        visualizarFornecedores = new VisualizarFornecedores(conexao);
        visualizarAgendamentos = new VisualizarAgendamentos(conexao);
        agendamento = new Agendamento(conexao);
        visualizarVendaNFE = new VisualizarVendaNFE(conexao);
        listarUsuario = new ListarUsuario(scanner);
    }

    /**
     * Retorna a instância do cadastro de fornecedores.
     *
     * @return Objeto {@link CadastroFornecedor}.
     */
    public CadastroFornecedor getCadastroFornecedor() {
        return cadastroFornecedor;
    }

    /**
     * Retorna a instância do cadastro de produtos.
     *
     * @return Objeto {@link CadastroProduto}.
     */
    public CadastroProduto getCadastroProduto() {
        return cadastroProduto;
    }

    /**
     * Retorna a instância para visualização de clientes.
     *
     * @return Objeto {@link VisualizarClientes}.
     */
    public VisualizarClientes getVisualizarClientes() {
        return visualizarClientes;
    }

    /**
     * Retorna a instância para visualização de fornecedores.
     *
     * @return Objeto {@link VisualizarFornecedores}.
     */
    public VisualizarFornecedores getVisualizarFornecedores() {
        return visualizarFornecedores;
    }

    /**
     * Retorna a instância para visualização de agendamentos.
     *
     * @return Objeto {@link VisualizarAgendamentos}.
     */
    public VisualizarAgendamentos getVisualizarAgendamentos() {
        return visualizarAgendamentos;
    }

    /**
     * Retorna a instância do módulo de agendamento.
     *
     * @return Objeto {@link Agendamento}.
     */
    public Agendamento getAgendamento() {
        return agendamento;
    }

    /**
     * Retorna a instância para visualização de vendas de NF-e.
     *
     * @return Objeto {@link VisualizarVendaNFE}.
     */
    public VisualizarVendaNFE getVisualizarVendaNFE() {
        return visualizarVendaNFE;
    }

    /**
     * Retorna a instância do módulo de listagem de usuários.
     *
     * @return Objeto {@link ListarUsuario}.
     */
    public ListarUsuario getListarUsuario() {
        return listarUsuario;
    }
}
