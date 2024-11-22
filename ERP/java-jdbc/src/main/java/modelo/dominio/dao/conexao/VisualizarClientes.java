package modelo.dominio.dao.conexao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe responsável por listar e visualizar clientes cadastrados.
 * Permite realizar buscas por nome ou CNPJ e exibe os resultados em uma tabela.
 */
public class VisualizarClientes {
    private Conexao conexao;

    /**
     * Construtor da classe {@code VisualizarClientes}.
     *
     * @param conexao instância da interface {@link Conexao} para acesso ao banco de dados
     */
    public VisualizarClientes(Conexao conexao) {
        this.conexao = conexao;
    }

    /**
     * Método principal para exibir a lista de clientes em uma janela gráfica.
     * Inclui funcionalidades para busca e navegação.
     */
    public void listarClientes() {
        // Criação da janela principal
    	 JFrame frame = new JFrame("Lista de Clientes");
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         frame.setSize(1200, 800); // Aumentando o tamanho da janela
         frame.setLocationRelativeTo(null); // Centralizando no meio da tela

        // Painel para a barra de pesquisa
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblSearch = new JLabel("Pesquisar:");
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");

        panelTop.add(lblSearch);
        panelTop.add(txtSearch);
        panelTop.add(btnSearch);

        // Tabela para exibir os clientes
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Configuração do modelo da tabela
        String[] columnNames = {"ID", "CNPJ/CPF", "Nome", "Telefone", "Email", "Endereço"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);

        // Painel inferior com o botão "Voltar"
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar");
        panelBottom.add(btnVoltar);

        // Ação do botão "Buscar"
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0); // Limpar a tabela
                buscarClientes(txtSearch.getText().trim(), tableModel);
            }
        });

        // Ação do botão "Voltar"
        btnVoltar.addActionListener(e -> frame.dispose());

        // Layout principal da janela
        frame.setLayout(new BorderLayout());
        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelBottom, BorderLayout.SOUTH);

        // Exibir a janela
        frame.setVisible(true);
    }

    /**
     * Busca clientes no banco de dados com base no termo de pesquisa informado.
     * Os resultados são exibidos no modelo da tabela fornecido.
     *
     * @param searchTerm   termo de pesquisa (nome ou CNPJ do cliente)
     * @param tableModel   modelo da tabela onde os resultados serão exibidos
     */
    private void buscarClientes(String searchTerm, DefaultTableModel tableModel) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexao.obterConexao();
            String sql = "SELECT * FROM cliente WHERE nome LIKE ? OR cnpj LIKE ?";
            stmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String cnpj = rs.getString("cnpj");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String endereco = rs.getString("rua") + ", " + rs.getString("bairro") + ", " + rs.getString("cidade");

                Object[] rowData = {id, cnpj, nome, telefone, email, endereco};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar clientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
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
