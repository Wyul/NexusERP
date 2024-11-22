package modelo.controller;

/**
 * Representa uma categoria no sistema.
 * Esta classe encapsula as informações relacionadas a uma categoria, incluindo
 * seu identificador, nome e descrição.
 */
public class Categoria {
    private int id;
    private String nome;
    private String descricao;

    /**
     * Construtor padrão.
     * Inicializa uma nova instância da classe Categoria com valores padrão.
     */
    public Categoria() {
    }

    /**
     * Construtor completo.
     * Inicializa uma nova instância da classe Categoria com os valores fornecidos.
     *
     * @param id        Identificador único da categoria.
     * @param nome      Nome da categoria.
     * @param descricao Descrição detalhada da categoria.
     */
    public Categoria(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Obtém o identificador único da categoria.
     *
     * @return ID da categoria.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador único da categoria.
     *
     * @param id Novo ID da categoria.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome da categoria.
     *
     * @return Nome da categoria.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da categoria.
     *
     * @param nome Novo nome da categoria.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição detalhada da categoria.
     *
     * @return Descrição da categoria.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição detalhada da categoria.
     *
     * @param descricao Nova descrição da categoria.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
