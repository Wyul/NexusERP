package gestao;

/**
 * Classe que representa um fornecedor no sistema.
 * Contém informações como CNPJ, razão social, endereço e dados de contato do fornecedor.
 */
public class Fornecedor {

    private long cnpj; // CNPJ do fornecedor
    private String razaoSocial; // Razão social do fornecedor
    private String inscricaoEstadual; // Inscrição estadual do fornecedor
    private String representante; // Nome do representante do fornecedor
    private long telefone; // Número de telefone do fornecedor
    private String email; // Endereço de email do fornecedor
    private String rua; // Endereço - Rua
    private String bairro; // Endereço - Bairro
    private String cidade; // Endereço - Cidade
    private String nome; // Nome do fornecedor

    /**
     * Construtor completo para inicializar um fornecedor com todos os atributos.
     *
     * @param cnpj              CNPJ do fornecedor.
     * @param razaoSocial       Razão social do fornecedor.
     * @param inscricaoEstadual Inscrição estadual do fornecedor.
     * @param representante     Nome do representante do fornecedor.
     * @param telefone          Número de telefone do fornecedor.
     * @param email             Endereço de email do fornecedor.
     * @param rua               Rua do endereço do fornecedor.
     * @param bairro            Bairro do endereço do fornecedor.
     * @param cidade            Cidade do endereço do fornecedor.
     * @param nome              Nome do fornecedor.
     */
    public Fornecedor(long cnpj, String razaoSocial, String inscricaoEstadual, String representante,
                      long telefone, String email, String rua, String bairro, String cidade, String nome) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.inscricaoEstadual = inscricaoEstadual;
        this.representante = representante;
        this.telefone = telefone;
        this.email = email;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.nome = nome;
    }

    // Getters

    /**
     * Retorna o CNPJ do fornecedor.
     *
     * @return CNPJ do fornecedor.
     */
    public long getCnpj() {
        return cnpj;
    }

    /**
     * Retorna a razão social do fornecedor.
     *
     * @return Razão social do fornecedor.
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * Retorna a inscrição estadual do fornecedor.
     *
     * @return Inscrição estadual do fornecedor.
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * Retorna o nome do representante do fornecedor.
     *
     * @return Nome do representante.
     */
    public String getRepresentante() {
        return representante;
    }

    /**
     * Retorna o número de telefone do fornecedor.
     *
     * @return Número de telefone do fornecedor.
     */
    public long getTelefone() {
        return telefone;
    }

    /**
     * Retorna o endereço de email do fornecedor.
     *
     * @return Endereço de email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna a rua do endereço do fornecedor.
     *
     * @return Rua do endereço.
     */
    public String getRua() {
        return rua;
    }

    /**
     * Retorna o bairro do endereço do fornecedor.
     *
     * @return Bairro do endereço.
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Retorna a cidade do endereço do fornecedor.
     *
     * @return Cidade do endereço.
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * Retorna o nome do fornecedor.
     *
     * @return Nome do fornecedor.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método ainda não implementado. 
     * Representa a obtenção de informações adicionais relacionadas à inscrição estadual.
     *
     * @return {@code null} atualmente.
     */
    public String getIeCli() {
        return null; // Placeholder para implementação futura
    }
}
