package gestao;

/**
 * Classe que representa um cliente ou fornecedor no sistema.
 * Contém informações como CNPJ, telefone, endereço e dados específicos do cliente ou fornecedor.
 */
public class Cliente {

    private long cnpj; // Identificador único do cliente ou fornecedor (CNPJ)
    private long telefone; // Número de telefone
    private String nome; // Nome do cliente
    private String email; // Email do cliente
    private String rua; // Endereço - Rua
    private String bairro; // Endereço - Bairro
    private String cidade; // Endereço - Cidade
    private String nomeFornecedor; // Nome do fornecedor (caso aplicável)
    private String RazaoSocial; // Razão social do cliente ou fornecedor
    private long ieCli; // Inscrição estadual
    private String representante; // Nome do representante do cliente ou fornecedor

    /**
     * Construtor completo para inicializar um fornecedor com dados adicionais.
     *
     * @param nomeFornecedor Nome do fornecedor.
     * @param razaoSocial    Razão social do fornecedor.
     * @param ieCli          Inscrição estadual do fornecedor.
     * @param representante  Nome do representante do fornecedor.
     */
    public Cliente(String nomeFornecedor, String razaoSocial, long ieCli, String representante) {
        this.nomeFornecedor = nomeFornecedor;
        this.RazaoSocial = razaoSocial;
        this.ieCli = ieCli;
        this.representante = representante;
    }

    /**
     * Construtor padrão para inicializar um cliente sem o ID.
     *
     * @param cnpj    CNPJ do cliente.
     * @param telefone Número de telefone do cliente.
     * @param nome    Nome do cliente.
     * @param email   Email do cliente.
     * @param rua     Rua do endereço do cliente.
     * @param bairro  Bairro do endereço do cliente.
     * @param cidade  Cidade do endereço do cliente.
     */
    public Cliente(long cnpj, long telefone, String nome, String email, String rua, String bairro, String cidade) {
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.nome = nome;
        this.email = email;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
    }

    // Getters e Setters

    /**
     * Retorna o CNPJ do cliente.
     *
     * @return CNPJ do cliente.
     */
    public long getCnpj() {
        return cnpj;
    }

    /**
     * Define o CNPJ do cliente.
     *
     * @param cnpj CNPJ do cliente.
     */
    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * Retorna o número de telefone do cliente.
     *
     * @return Número de telefone.
     */
    public long getTelefone() {
        return telefone;
    }

    /**
     * Define o número de telefone do cliente.
     *
     * @param telefone Número de telefone.
     */
    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o nome do cliente.
     *
     * @return Nome do cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do cliente.
     *
     * @param nome Nome do cliente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o email do cliente.
     *
     * @return Email do cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do cliente.
     *
     * @param email Email do cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna a rua do endereço do cliente.
     *
     * @return Rua do endereço.
     */
    public String getRua() {
        return rua;
    }

    /**
     * Define a rua do endereço do cliente.
     *
     * @param rua Rua do endereço.
     */
    public void setRua(String rua) {
        this.rua = rua;
    }

    /**
     * Retorna o bairro do endereço do cliente.
     *
     * @return Bairro do endereço.
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Define o bairro do endereço do cliente.
     *
     * @param bairro Bairro do endereço.
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * Retorna a cidade do endereço do cliente.
     *
     * @return Cidade do endereço.
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * Define a cidade do endereço do cliente.
     *
     * @param cidade Cidade do endereço.
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * Retorna o nome do fornecedor.
     *
     * @return Nome do fornecedor.
     */
    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    /**
     * Define o nome do fornecedor.
     *
     * @param nomeFornecedor Nome do fornecedor.
     */
    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    /**
     * Retorna a razão social do cliente ou fornecedor.
     *
     * @return Razão social.
     */
    public String getRazaoSocial() {
        return RazaoSocial;
    }

    /**
     * Define a razão social do cliente ou fornecedor.
     *
     * @param razaoSocial Razão social.
     */
    public void setRazaoSocial(String razaoSocial) {
        this.RazaoSocial = razaoSocial;
    }

    /**
     * Retorna a inscrição estadual do cliente.
     *
     * @return Inscrição estadual.
     */
    public long getIeCli() {
        return ieCli;
    }

    /**
     * Define a inscrição estadual do cliente.
     *
     * @param ieCli Inscrição estadual.
     */
    public void setIeCli(long ieCli) {
        this.ieCli = ieCli;
    }

    /**
     * Retorna o nome do representante do cliente ou fornecedor.
     *
     * @return Nome do representante.
     */
    public String getRepresentante() {
        return representante;
    }

    /**
     * Define o nome do representante do cliente ou fornecedor.
     *
     * @param representante Nome do representante.
     */
    public void setRepresentante(String representante) {
        this.representante = representante;
    }
}
