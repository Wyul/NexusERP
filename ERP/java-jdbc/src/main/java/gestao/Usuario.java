package gestao;

import java.time.LocalDateTime;

/**
 * Classe abstrata que representa um usuário genérico no sistema.
 * Essa classe contém atributos e comportamentos comuns aos diferentes tipos de usuários,
 * permitindo a definição de métodos abstratos para ações específicas de cada perfil.
 */
public abstract class Usuario {

    private Long id;
    private String nome;
    private String username;
    private String senha;
    private boolean estado;
    private LocalDateTime dataHoraCriacao;

    /**
     * Construtor da classe Usuario.
     *
     * @param id               Identificador único do usuário.
     * @param nome             Nome completo do usuário.
     * @param username         Nome de usuário utilizado para login.
     * @param senha            Senha do usuário.
     * @param estado           Indica se o usuário está ativo (true) ou inativo (false).
     * @param dataHoraCriacao  Data e hora de criação do usuário no sistema.
     */
    public Usuario(Long id, String nome, String username, String senha, boolean estado, LocalDateTime dataHoraCriacao) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.estado = estado;
        this.dataHoraCriacao = dataHoraCriacao;
    }

    /**
     * Retorna o identificador único do usuário.
     *
     * @return ID do usuário.
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna o nome completo do usuário.
     *
     * @return Nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o nome de usuário utilizado para login.
     *
     * @return Nome de usuário.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return Senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Retorna o estado do usuário.
     *
     * @return {@code true} se o usuário está ativo, {@code false} caso contrário.
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Retorna a data e hora de criação do usuário.
     *
     * @return Data e hora de criação.
     */
    public LocalDateTime getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    /**
     * Método abstrato que deve ser implementado pelas subclasses para
     * definir o comportamento específico de cada perfil de usuário.
     */
    public abstract void realizarAcao();
}
