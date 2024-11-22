package gestao.estoque;

import java.time.LocalDateTime;

/**
 * Representa um produto ou serviço no estoque.
 */
public class Produto {

    private long codigo;
    private String nome;
    private long quantidade;
    private boolean estado;
    private float prcCusto;
    private float prcVenda;
    private String embalagem;
    private LocalDateTime data;
    private String prodser;

    /**
     * Construtor completo para inicializar um produto ou serviço.
     *
     * @param codigo    Código único do produto.
     * @param nome      Nome do produto.
     * @param quantidade Quantidade disponível no estoque.
     * @param prcCusto  Preço de custo do produto.
     * @param prcVenda  Preço de venda do produto.
     * @param embalagem Tipo de embalagem do produto.
     * @param data      Data de cadastro do produto.
     * @param prodser   Tipo do item, indicando se é "Produto" ou "Serviço".
     */
    public Produto(long codigo, String nome, long quantidade, float prcCusto, float prcVenda, String embalagem, LocalDateTime data, String prodser) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.estado = true; // Estado padrão sempre true
        this.prcCusto = prcCusto;
        this.prcVenda = prcVenda;
        this.embalagem = embalagem;
        this.data = data;
        this.prodser = prodser;
    }

    /**
     * Obtém o código único do produto.
     *
     * @return Código do produto.
     */
    public long getCodigo() {
        return codigo;
    }

    /**
     * Define o código único do produto.
     *
     * @param codigo Código do produto.
     */
    public void setCodigo(long codigo) {
        this.codigo = codigo;
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
     * @param nome Nome do produto.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a quantidade disponível no estoque.
     *
     * @return Quantidade do produto.
     */
    public long getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade disponível no estoque.
     *
     * @param quantidade Quantidade do produto.
     */
    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Obtém o preço de custo do produto.
     *
     * @return Preço de custo.
     */
    public float getPrcCusto() {
        return prcCusto;
    }

    /**
     * Define o preço de custo do produto.
     *
     * @param prcCusto Preço de custo.
     */
    public void setPrcCusto(float prcCusto) {
        this.prcCusto = prcCusto;
    }

    /**
     * Obtém o preço de venda do produto.
     *
     * @return Preço de venda.
     */
    public float getPrcVenda() {
        return prcVenda;
    }

    /**
     * Define o preço de venda do produto.
     *
     * @param prcVenda Preço de venda.
     */
    public void setPrcVenda(float prcVenda) {
        this.prcVenda = prcVenda;
    }

    /**
     * Obtém o tipo de embalagem do produto.
     *
     * @return Tipo de embalagem.
     */
    public String getEmbalagem() {
        return embalagem;
    }

    /**
     * Define o tipo de embalagem do produto.
     *
     * @param embalagem Tipo de embalagem.
     */
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    /**
     * Obtém a data de cadastro do produto.
     *
     * @return Data de cadastro.
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Define a data de cadastro do produto.
     *
     * @param data Data de cadastro.
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    /**
     * Obtém o tipo do item (Produto ou Serviço).
     *
     * @return Tipo do item.
     */
    public String getProdser() {
        return prodser;
    }

    /**
     * Define o tipo do item (Produto ou Serviço).
     *
     * @param prodser Tipo do item.
     */
    public void setProdser(String prodser) {
        this.prodser = prodser;
    }

    /**
     * Verifica o estado atual do produto (ativo ou inativo).
     *
     * @return {@code true} se o produto estiver ativo, {@code false} caso contrário.
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Define o estado do produto (ativo ou inativo).
     *
     * @param estado Estado do produto.
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
