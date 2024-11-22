package front;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import modelo.dominio.dao.conexao.ConexaoSQL;

/**
 * Classe que fornece uma interface gráfica para visualização de produtos no estoque.
 * Os dados são carregados a partir da tabela `estoque` no banco de dados.
 */
public class TelaEstoque extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    /**
     * Construtor da classe `TelaEstoque`.
     * Inicializa a interface gráfica e carrega os dados do estoque do banco de dados.
     *
     * @param conexaoSQL Instância de `ConexaoSQL` para obter a conexão com o banco de dados.
     */
    public TelaEstoque(ConexaoSQL conexaoSQL) {
        setTitle("Visualizar Produtos - Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null);

        // Criação do modelo e tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Código");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Quantidade");
        tableModel.addColumn("Preço de Venda");
        tableModel.addColumn("Preço de Custo");
        tableModel.addColumn("Tipo de Embalagem");
        tableModel.addColumn("Produto/Serviço");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Estabelecer conexão e carregar dados
        try {
            connection = conexaoSQL.obterConexao();
            carregarProdutos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    /**
     * Método responsável por carregar os produtos do banco de dados para a tabela.
     * Realiza uma consulta na tabela `estoque` e popula o modelo de tabela.
     */
    private void carregarProdutos() {
        String query = "SELECT codigo_pro, nome_pro, quant_pro, prc_venda, prc_custo, tipo_emba, prod_ser FROM estoque";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Recuperar dados do banco
                String codigoPro = rs.getString("codigo_pro");
                String nomePro = rs.getString("nome_pro");
                int quantProd = rs.getInt("quant_pro");
                double prcVenda = rs.getDouble("prc_venda");
                double prcCusto = rs.getDouble("prc_custo");
                String tipoEmba = rs.getString("tipo_emba");
                String prodSer = rs.getString("prod_ser");

                // Adicionar os dados na tabela
                tableModel.addRow(new Object[]{codigoPro, nomePro, quantProd, prcVenda, prcCusto, tipoEmba, prodSer});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar os produtos: " + e.getMessage());
        }
    }
}
