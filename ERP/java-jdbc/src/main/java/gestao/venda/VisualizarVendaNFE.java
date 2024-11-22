package gestao.venda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por exibir as vendas de Nota Fiscal Eletrônica (NF-e) em uma interface gráfica.
 */
public class VisualizarVendaNFE {
    private Conexao conexaoSQL;

    /**
     * Construtor para inicializar a conexão com o banco de dados.
     *
     * @param conexaoSQL Instância da classe {@link Conexao} para interação com o banco de dados.
     */
    public VisualizarVendaNFE(Conexao conexaoSQL) {
        this.conexaoSQL = conexaoSQL;
    }

    /**
     * Exibe uma interface gráfica para buscar e listar as vendas de NF-e.
     */
    public void exibirVendas() {
        // Criação da janela principal
    	// Criação da janela principal
        JFrame frame = new JFrame("Vendas NF-e");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela

        // Painel para os campos de busca
        JPanel panelBusca = new JPanel(new FlowLayout());

        // Campos de busca
        JLabel lblCNPJ = new JLabel("CNPJ:");
        JTextField txtCNPJ = new JTextField(15);
        JLabel lblNomeCliente = new JLabel("Nome Cliente:");
        JTextField txtNomeCliente = new JTextField(15);
        JLabel lblNomeVeiculo = new JLabel("Nome Veículo:");
        JTextField txtNomeVeiculo = new JTextField(15);

        // Botão de busca
        JButton btnBuscar = new JButton("Buscar");

        // Adicionar componentes ao painel de busca
        panelBusca.add(lblCNPJ);
        panelBusca.add(txtCNPJ);
        panelBusca.add(lblNomeCliente);
        panelBusca.add(txtNomeCliente);
        panelBusca.add(lblNomeVeiculo);
        panelBusca.add(txtNomeVeiculo);
        panelBusca.add(btnBuscar);

        // Configuração da tabela para exibir os resultados
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Configuração do modelo da tabela
        String[] columnNames = {"CNPJ", "Telefone", "Nome Cliente", "Nome Veículo", "Marca Veículo",
                                "Forma Pagamento", "Peça", "Valor", "Defeito", "Data Venda", "Código Peça"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);

        // Botão de voltar
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());

        // Painel para o botão Voltar
        JPanel panelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVoltar.add(btnVoltar);

        // Adicionar os painéis e a tabela à janela
        frame.setLayout(new BorderLayout());
        frame.add(panelBusca, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelVoltar, BorderLayout.SOUTH);

        // Ação do botão Buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cnpj = txtCNPJ.getText().trim();
                String nomeCliente = txtNomeCliente.getText().trim();
                String nomeVeiculo = txtNomeVeiculo.getText().trim();

                // Limpar a tabela antes de preencher com novos dados
                tableModel.setRowCount(0);

                // Realizar a busca
                buscarVendas(cnpj, nomeCliente, nomeVeiculo, tableModel);
            }
        });

        // Exibir a janela
        frame.setVisible(true);
    }

    /**
     * Realiza a busca de vendas no banco de dados com base nos critérios fornecidos e atualiza a tabela.
     *
     * @param cnpj           Filtro pelo CNPJ do cliente.
     * @param nomeCliente    Filtro pelo nome do cliente.
     * @param nomeVeiculo    Filtro pelo nome do veículo.
     * @param tableModel     Modelo da tabela para exibir os resultados.
     */
    private void buscarVendas(String cnpj, String nomeCliente, String nomeVeiculo, DefaultTableModel tableModel) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexaoSQL.obterConexao();
            String sql = "SELECT cnpj, telefone, nome_cliente, nome_veiculo, marca_veiculo, "
                       + "forma_pagamento, peca, valor, defeito, data_venda, cod_peca FROM venda_nfe WHERE 1=1";

            // Adicionar filtros dinâmicos
            if (!cnpj.isEmpty()) sql += " AND cnpj LIKE ?";
            if (!nomeCliente.isEmpty()) sql += " AND nome_cliente LIKE ?";
            if (!nomeVeiculo.isEmpty()) sql += " AND nome_veiculo LIKE ?";

            stmt = conn.prepareStatement(sql);

            // Configurar os parâmetros de busca
            int paramIndex = 1;
            if (!cnpj.isEmpty()) stmt.setString(paramIndex++, "%" + cnpj + "%");
            if (!nomeCliente.isEmpty()) stmt.setString(paramIndex++, "%" + nomeCliente + "%");
            if (!nomeVeiculo.isEmpty()) stmt.setString(paramIndex++, "%" + nomeVeiculo + "%");

            rs = stmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            while (rs.next()) {
                // Extrair os dados da venda
                String cnpjResult = rs.getString("cnpj");
                String telefone = rs.getString("telefone");
                String nomeClienteResult = rs.getString("nome_cliente");
                String nomeVeiculoResult = rs.getString("nome_veiculo");
                String marcaVeiculo = rs.getString("marca_veiculo");
                String formaPagamento = rs.getString("forma_pagamento");
                String peca = rs.getString("peca");
                double valor = rs.getDouble("valor");
                String defeito = rs.getString("defeito");
                java.sql.Timestamp dataVenda = rs.getTimestamp("data_venda");
                String codPeca = rs.getString("cod_peca");

                String dataVendaFormatada = dataVenda != null ? sdf.format(dataVenda) : "N/A";

                // Adicionar linha à tabela
                Object[] rowData = {cnpjResult, telefone, nomeClienteResult, nomeVeiculoResult, marcaVeiculo,
                                    formaPagamento, peca, valor, defeito, dataVendaFormatada, codPeca};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar a busca: " + e.getMessage(),
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Fechar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
