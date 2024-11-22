package modelo.agendamento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
 * Classe responsável por exibir e buscar agendamentos no sistema.
 */
public class VisualizarAgendamentos {
    private Conexao conexaoSQL;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     *
     * @param conexaoSQL Instância de {@link Conexao} para interação com o banco de dados.
     */
    public VisualizarAgendamentos(Conexao conexaoSQL) {
        this.conexaoSQL = conexaoSQL;
    }

    /**
     * Exibe a janela com os agendamentos e opções de busca.
     */
    public void exibirAgendamentos() {
        // Criação da janela
    	 JFrame frame = new JFrame("Agendamentos");
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         frame.setSize(1200, 700); // Aumentar o tamanho da janela
         frame.setLocationRelativeTo(null); // Centralizar a janela na tela

        // Painel para campos de busca
        JPanel panelBusca = new JPanel(new FlowLayout());

        // Criar campos de texto para busca
        JLabel lblNomeCliente = new JLabel("Nome Cliente:");
        JTextField txtNomeCliente = new JTextField(15);
        JLabel lblCNPJ = new JLabel("CNPJ:");
        JTextField txtCNPJ = new JTextField(15);
        JLabel lblStatus = new JLabel("Status:");
        JTextField txtStatus = new JTextField(10);

        // Botão de busca
        JButton btnBuscar = new JButton("Buscar");

        // Adicionar os componentes ao painel de busca
        panelBusca.add(lblNomeCliente);
        panelBusca.add(txtNomeCliente);
        panelBusca.add(lblCNPJ);
        panelBusca.add(txtCNPJ);
        panelBusca.add(lblStatus);
        panelBusca.add(txtStatus);
        panelBusca.add(btnBuscar);

        // Tabela para exibir os agendamentos
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Cabeçalhos da tabela
        String[] columnNames = { "ID", "Cliente", "CNPJ", "Telefone", "Nome Veículo", "Data Entrada", "Status" };

        // Modelo da tabela
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);

        // Botão de Voltar
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> frame.dispose()); // Fecha apenas a janela atual

        // Painel para o botão Voltar
        JPanel panelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVoltar.add(btnVoltar);

        // Adicionar o painel de busca, tabela e botão Voltar na janela
        frame.setLayout(new BorderLayout());
        frame.add(panelBusca, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelVoltar, BorderLayout.SOUTH);

        // Ação do botão de busca
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obter os valores dos campos de texto
                String nomeCliente = txtNomeCliente.getText().trim();
                String cnpj = txtCNPJ.getText().trim();
                String status = txtStatus.getText().trim();

                // Limpar a tabela antes de preencher com os resultados da busca
                tableModel.setRowCount(0);

                // Realizar a busca
                buscarAgendamentos(nomeCliente, cnpj, status, tableModel);
            }
        });

        // Exibir a janela
        frame.setVisible(true);
    }

    /**
     * Realiza a busca de agendamentos no banco de dados com base nos filtros fornecidos.
     *
     * @param nomeCliente Nome do cliente a ser filtrado.
     * @param cnpj        CNPJ a ser filtrado.
     * @param status      Status do agendamento a ser filtrado.
     * @param tableModel  Modelo da tabela onde os resultados serão exibidos.
     */
    private void buscarAgendamentos(String nomeCliente, String cnpj, String status, DefaultTableModel tableModel) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexaoSQL.obterConexao();
            String sql = "SELECT a.id, c.nome, c.cnpj, c.telefone, a.nome_veiculo, a.data_entrada, a.status "
                    + "FROM agendamento a " + "JOIN cliente c ON a.id_cliente = c.id WHERE 1=1";

            // Adicionar filtros conforme os campos preenchidos
            if (!nomeCliente.isEmpty())
                sql += " AND c.nome LIKE ?";
            if (!cnpj.isEmpty())
                sql += " AND c.cnpj LIKE ?";
            if (!status.isEmpty())
                sql += " AND a.status LIKE ?";

            stmt = conn.prepareStatement(sql);

            // Definir os parâmetros de busca se o usuário forneceu algum valor
            int paramIndex = 1;
            if (!nomeCliente.isEmpty())
                stmt.setString(paramIndex++, "%" + nomeCliente + "%");
            if (!cnpj.isEmpty())
                stmt.setString(paramIndex++, "%" + cnpj + "%");
            if (!status.isEmpty())
                stmt.setString(paramIndex++, "%" + status + "%");

            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cnpjResult = rs.getString("cnpj");
                String telefone = rs.getString("telefone");
                String nomeVeiculo = rs.getString("nome_veiculo");
                String dataEntrada = rs.getString("data_entrada");
                String statusResult = rs.getString("status");

                // Adicionando os dados na tabela
                Object[] rowData = { id, nome, cnpjResult, telefone, nomeVeiculo, dataEntrada, statusResult };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar a busca: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Fechar recursos
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
