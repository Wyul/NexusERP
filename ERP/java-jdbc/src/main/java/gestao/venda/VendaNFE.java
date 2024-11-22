package gestao.venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por realizar o processo de venda e registrar os detalhes da Nota Fiscal Eletrônica (NF-e).
 */

public class VendaNFE {
    private Conexao conexao;

    /**
     * Construtor que inicializa a classe com a conexão ao banco de dados.
     *
     * @param conexao Instância da classe {@link Conexao} para gerenciar a conexão com o banco de dados.
     */
    public VendaNFE(Conexao conexao) {
        this.conexao = conexao;
    }
    /**
     * Realiza o processo completo de venda, registrando os dados da venda, atualizando o estoque
     * e o status do agendamento.
     *
     * @param idAgendamento      ID do agendamento associado à venda.
     * @param formaPagamento     Forma de pagamento utilizada na venda.
     * @param defeito            Defeito solucionado durante o atendimento.
     * @param codigosProdutos    Lista de códigos dos produtos vendidos.
     * @param quantidadesProdutos Lista de quantidades correspondentes aos produtos vendidos.
     * @return {@code true} se a venda for realizada com sucesso, caso contrário, {@code false}.
     */
    public boolean realizarVenda(int idAgendamento, String formaPagamento, String defeito,
            ArrayList<String> codigosProdutos, ArrayList<Integer> quantidadesProdutos) {
    Connection conn = null;
    PreparedStatement stmtAgendamento = null;
    PreparedStatement stmtVendaNFE = null;
    PreparedStatement stmtDetalheVendaNFE = null;
    PreparedStatement stmtAtualizaEstoque = null;
    PreparedStatement stmtAtualizaStatus = null; 
    ResultSet rsAgendamento = null;
    /**
     * Busca informações sobre uma peça específica no estoque com base no código informado.
     *
     * @param conn    Conexão com o banco de dados.
     * @param codPeca Código da peça a ser buscada.
     * @return Um array de objetos contendo:
     *         - Nome da peça (índice 0)
     *         - Preço de venda (índice 1)
     *         Retorna {@code null} se a peça não for encontrada.
     */
    double valorTotal = 0.0;

    try {
        conn = conexao.obterConexao();
        conn.setAutoCommit(false); 

        // Buscar os dados do agendamento
        String sqlAgendamento = "SELECT id_cliente, cnpj, telefone, nome_cliente, nome_veiculo, marca_veiculo FROM agendamento WHERE id = ?";
        stmtAgendamento = conn.prepareStatement(sqlAgendamento);
        stmtAgendamento.setInt(1, idAgendamento);
        rsAgendamento = stmtAgendamento.executeQuery();

        if (!rsAgendamento.next()) {
            System.out.println("Agendamento não encontrado.");
            return false;
        }

        // Recupera as informações do agendamento
        int idCliente = rsAgendamento.getInt("id_cliente");
        String cnpj = rsAgendamento.getString("cnpj");
        String telefone = rsAgendamento.getString("telefone");
        String nomeCliente = rsAgendamento.getString("nome_cliente");
        String nomeVeiculo = rsAgendamento.getString("nome_veiculo");
        String marcaVeiculo = rsAgendamento.getString("marca_veiculo");

        // Inserir venda na tabela venda_nfe
        String sqlVendaNFE = "INSERT INTO venda_nfe (id_cliente, cnpj, telefone, nome_cliente, nome_veiculo, marca_veiculo, forma_pagamento, defeito) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id"; // Usando RETURNING para pegar o ID gerado
        stmtVendaNFE = conn.prepareStatement(sqlVendaNFE);
        stmtVendaNFE.setInt(1, idCliente);
        stmtVendaNFE.setString(2, cnpj);
        stmtVendaNFE.setString(3, telefone);
        stmtVendaNFE.setString(4, nomeCliente);
        stmtVendaNFE.setString(5, nomeVeiculo);
        stmtVendaNFE.setString(6, marcaVeiculo);
        stmtVendaNFE.setString(7, formaPagamento);
        stmtVendaNFE.setString(8, defeito);

        // Executando a inserção na tabela venda_nfe e obtendo o ID gerado
        ResultSet rsVendaNFE = stmtVendaNFE.executeQuery();
        int idVenda = -1;
        if (rsVendaNFE.next()) {
            idVenda = rsVendaNFE.getInt("id");
        }

        if (idVenda == -1) {
            System.out.println("Falha ao registrar venda na tabela venda_nfe.");
            conn.rollback(); // Reverte transação em caso de falha
            return false;
        } else {
            System.out.println("Venda registrada com sucesso na tabela venda_nfe.");
        }

        // Novo PreparedStatement para atualizar os detalhes da venda_nfe
        String sqlDetalheVendaNFE = "UPDATE venda_nfe SET cod_peca = ?, valor = ?, quantidade = ?, peca = ? WHERE id = ?";
        stmtDetalheVendaNFE = conn.prepareStatement(sqlDetalheVendaNFE);

        // Inicializa stmtAtualizaEstoque antes do loop
        String sqlAtualizaEstoque = "UPDATE estoque SET quant_pro = quant_pro - ? WHERE codigo_pro = ?";
        stmtAtualizaEstoque = conn.prepareStatement(sqlAtualizaEstoque);

        for (int i = 0; i < codigosProdutos.size(); i++) {
            String codProduto = codigosProdutos.get(i);
            int quantidade = quantidadesProdutos.get(i);

            // Buscar o preço e nome da peça
            Object[] infoProduto = buscarInformacoesPeca(conn, codProduto);
            if (infoProduto != null) {
                double precoProduto = (Double) infoProduto[1]; // Preço do produto
                String nomePeca = (String) infoProduto[0]; // Nome da peça
                valorTotal += precoProduto * quantidade; // Calcular o valor total

                // Atualizar os detalhes do produto na tabela venda_nfe
                stmtDetalheVendaNFE.setString(1, codProduto);  // código da peça
                stmtDetalheVendaNFE.setDouble(2, precoProduto);  // preço
                stmtDetalheVendaNFE.setInt(3, quantidade);  // quantidade
                stmtDetalheVendaNFE.setString(4, nomePeca); // nome da peça
                stmtDetalheVendaNFE.setInt(5, idVenda);  // ID da venda
                stmtDetalheVendaNFE.executeUpdate();

                // Atualizar o estoque
                stmtAtualizaEstoque.setInt(1, quantidade);
                stmtAtualizaEstoque.setString(2, codProduto);
                stmtAtualizaEstoque.executeUpdate();
            }
        }

        // Atualizar status do agendamento
        String sqlAtualizaStatus = "UPDATE agendamento SET status = 'F' WHERE id = ?";
        stmtAtualizaStatus = conn.prepareStatement(sqlAtualizaStatus);
        stmtAtualizaStatus.setInt(1, idAgendamento);
        stmtAtualizaStatus.executeUpdate();

        // Commit da transação
        conn.commit();

        // Opcional: Se necessário, salvar o valor total em algum lugar ou exibir
        System.out.println("Valor total da venda: " + valorTotal);

        return true;
    } catch (SQLException e) {
        System.out.println("Erro ao realizar venda: " + e.getMessage());
        try {
            if (conn != null) {
                conn.rollback(); // Reverte transação em caso de erro
            }
        } catch (SQLException rollbackEx) {
            System.out.println("Erro ao reverter transação: " + rollbackEx.getMessage());
        }
        return false;
    } finally {
        try {
            if (rsAgendamento != null)
                rsAgendamento.close();
            if (stmtAgendamento != null)
                stmtAgendamento.close();
            if (stmtVendaNFE != null)
                stmtVendaNFE.close();
            if (stmtDetalheVendaNFE != null)
                stmtDetalheVendaNFE.close();
            if (stmtAtualizaEstoque != null)
                stmtAtualizaEstoque.close();
            if (stmtAtualizaStatus != null)
                stmtAtualizaStatus.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
    /**
     * Busca informações de uma peça no estoque.
     *
     * @param conn    Conexão com o banco de dados.
     * @param codPeca Código da peça a ser buscada.
     * @return Um array com o nome do produto e seu preço, ou {@code null} se não encontrado.
     */
    private Object[] buscarInformacoesPeca(Connection conn, String codPeca) {
        try (PreparedStatement stmtEstoque = conn
                .prepareStatement("SELECT nome_pro, prc_venda FROM estoque WHERE codigo_pro = ?")) {
            stmtEstoque.setString(1, codPeca);
            try (ResultSet rsEstoque = stmtEstoque.executeQuery()) {
                if (rsEstoque.next()) {
                    return new Object[] { rsEstoque.getString("nome_pro"), rsEstoque.getDouble("prc_venda") };
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar informações da peça: " + e.getMessage());
        }
        return null;
    }
}
