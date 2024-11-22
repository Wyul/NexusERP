package modelo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import gestao.Usuario;

/**
 * Representa um produto no sistema.
 * Esta classe encapsula as informações de um produto, incluindo detalhes como
 * nome, descrição, preço, quantidade disponível, categoria associada, usuário criador e
 * a data/hora de criação.
 */
public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;
    private Categoria categoria;
    private Usuario usuario;
    private LocalDateTime datahoracriacao;

    /**
     * Construtor completo.
     * Inicializa uma nova instância da classe Produto com os valores fornecidos.
     *
     * @param id              Identificador único do produto.
     * @param nome            Nome do produto.
     * @param descricao       Descrição detalhada do produto.
     * @param preco           Preço do produto.
     * @param quantidade      Quantidade disponível do produto em estoque.
     * @param categoria       Categoria associada ao produto.
     * @param usuario         Usuário responsável pela criação do produto.
     * @param datahoracriacao Data e hora da criação do produto.
     */
    public Produto(int id, String nome, String descricao, BigDecimal preco, int quantidade, Categoria categoria,
                   Usuario usuario, LocalDateTime datahoracriacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.usuario = usuario;
        this.datahoracriacao = datahoracriacao;
    }

    /**
     * Obtém o identificador único do produto.
     *
     * @return ID do produto.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador único do produto.
     *
     * @param id Novo ID do produto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome do produto.
     *
     * @return Nome do produto.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do produto.
     *
     * @param nome Novo nome do produto.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição detalhada do produto.
     *
     * @return Descrição do produto.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição detalhada do produto.
     *
     * @param descricao Nova descrição do produto.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o preço do produto.
     *
     * @return Preço do produto.
     */
    public BigDecimal getPreco() {
        return preco;
    }

    /**
     * Define o preço do produto.
     *
     * @param preco Novo preço do produto.
     */
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    /**
     * Obtém a quantidade disponível do produto em estoque.
     *
     * @return Quantidade disponível.
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade disponível do produto em estoque.
     *
     * @param quantidade Nova quantidade disponível.
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Obtém a categoria associada ao produto.
     *
     * @return Categoria do produto.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Define a categoria associada ao produto.
     *
     * @param categoria Nova categoria do produto.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtém o usuário responsável pela criação do produto.
     *
     * @return Usuário criador do produto.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Define o usuário responsável pela criação do produto.
     *
     * @param usuario Novo usuário criador do produto.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtém a data e hora de criação do produto.
     *
     * @return Data e hora de criação.
     */
    public LocalDateTime getDatahoracriacao() {
        return datahoracriacao;
    }

    /**
     * Define a data e hora de criação do produto.
     *
     * @param datahoracriacao Nova data e hora de criação.
     */
    public void setDatahoracriacao(LocalDateTime datahoracriacao) {
        this.datahoracriacao = datahoracriacao;
    }
}
