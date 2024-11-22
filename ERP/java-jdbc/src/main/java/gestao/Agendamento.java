package gestao;

/**
 * Classe que representa um agendamento de atendimento.
 * Contém informações como cliente, veículo, data do atendimento e situação do agendamento.
 */
public class Agendamento {

    private int id;
    private int idCliente;
    private String dataAtendimento;
    private String modelo;
    private String marca;
    private String nomeCliente;
    private int idCarro;
    private int situacao; // Representa o estado do agendamento
    private String observacao;

    /**
     * Construtor completo da classe Agendamento.
     *
     * @param id             Identificador único do agendamento.
     * @param idCliente      Identificador do cliente associado ao agendamento.
     * @param dataAtendimento Data prevista para o atendimento.
     * @param modelo         Modelo do veículo.
     * @param marca          Marca do veículo.
     * @param nomeCliente    Nome do cliente.
     * @param idCarro        Identificador do carro associado ao agendamento.
     * @param situacao       Situação do agendamento (ex.: pendente, concluído).
     * @param observacao     Observações adicionais sobre o agendamento.
     */
    public Agendamento(int id, int idCliente, String dataAtendimento, String modelo, String marca, String nomeCliente, int idCarro, int situacao, String observacao) {
        this.id = id;
        this.idCliente = idCliente;
        this.dataAtendimento = dataAtendimento;
        this.modelo = modelo;
        this.marca = marca;
        this.nomeCliente = nomeCliente;
        this.idCarro = idCarro;
        this.situacao = situacao;
        this.observacao = observacao;
    }

    /**
     * Construtor padrão da classe Agendamento.
     * Inicializa o agendamento com valores padrão.
     */
    public Agendamento() {
        this.situacao = 0; // Situação padrão como "pendente"
        this.dataAtendimento = ""; // Data padrão vazia
    }

    // Getters e Setters

    /**
     * Retorna o identificador do agendamento.
     *
     * @return ID do agendamento.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do agendamento.
     *
     * @param id ID do agendamento.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o identificador do cliente associado.
     *
     * @return ID do cliente.
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Define o identificador do cliente associado.
     *
     * @param idCliente ID do cliente.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Retorna a data do atendimento.
     *
     * @return Data do atendimento.
     */
    public String getDataAtendimento() {
        return dataAtendimento;
    }

    /**
     * Define a data do atendimento.
     *
     * @param dataAtendimento Data do atendimento.
     */
    public void setDataAtendimento(String dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    /**
     * Retorna o modelo do veículo.
     *
     * @return Modelo do veículo.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Define o modelo do veículo.
     *
     * @param modelo Modelo do veículo.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Retorna a marca do veículo.
     *
     * @return Marca do veículo.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Define a marca do veículo.
     *
     * @param marca Marca do veículo.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Retorna o nome do cliente.
     *
     * @return Nome do cliente.
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * Define o nome do cliente.
     *
     * @param nomeCliente Nome do cliente.
     */
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * Retorna o identificador do carro associado.
     *
     * @return ID do carro.
     */
    public int getIdCarro() {
        return idCarro;
    }

    /**
     * Define o identificador do carro associado.
     *
     * @param idCarro ID do carro.
     */
    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }

    /**
     * Retorna a situação do agendamento.
     *
     * @return Situação do agendamento.
     */
    public int getSituacao() {
        return situacao;
    }

    /**
     * Define a situação do agendamento.
     *
     * @param situacao Situação do agendamento.
     */
    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    /**
     * Retorna as observações do agendamento.
     *
     * @return Observações do agendamento.
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * Define as observações do agendamento.
     *
     * @param observacao Observações do agendamento.
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
