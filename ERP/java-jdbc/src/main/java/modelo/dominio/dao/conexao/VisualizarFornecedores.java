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
 * Classe responsável por listar e visualizar fornecedores cadastrados. Permite
 * realizar buscas por nome ou CNPJ e exibe os resultados em uma tabela.
 */
public class VisualizarFornecedores {
	private Conexao conexaoSQL;

	/**
	 * Construtor da classe {@code VisualizarFornecedores}.
	 *
	 * @param conexaoSQL instância da interface {@link Conexao} para acesso ao banco
	 *                   de dados
	 */
	public VisualizarFornecedores(Conexao conexaoSQL) {
		this.conexaoSQL = conexaoSQL;
	}

	/**
	 * Método principal para exibir a lista de fornecedores em uma janela gráfica.
	 * Inclui funcionalidades para busca e navegação.
	 */
	public void exibirFornecedores() {
		// Criação da janela principal
		JFrame frame = new JFrame("Fornecedores");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1000, 600);
		frame.setLocationRelativeTo(null); // Centralizando no meio da tela

		// Painel para os campos de busca
		JPanel panelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBusca.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Campos de texto para busca
		JLabel lblNomeFornecedor = new JLabel("Nome Fornecedor:");
		JTextField txtNomeFornecedor = new JTextField(15);
		JLabel lblCNPJ = new JLabel("CNPJ:");
		JTextField txtCNPJ = new JTextField(15);

		// Botão de busca
		JButton btnBuscar = new JButton("Buscar");

		// Adicionar componentes ao painel de busca
		panelBusca.add(lblNomeFornecedor);
		panelBusca.add(txtNomeFornecedor);
		panelBusca.add(lblCNPJ);
		panelBusca.add(txtCNPJ);
		panelBusca.add(btnBuscar);

		// Configuração da tabela
		JTable table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);

		// Cabeçalhos da tabela
		String[] columnNames = { "CNPJ", "Razão Social", "Nome", "Telefone", "Email", "Endereço", "Bairro", "Cidade" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		table.setModel(tableModel);

		// Painel inferior com o botão "Voltar"
		JPanel panelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnVoltar = new JButton("Voltar");
		panelVoltar.add(btnVoltar);

		// Ação do botão "Buscar"
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Limpar a tabela antes de preencher com os resultados da busca
				tableModel.setRowCount(0);
				buscarFornecedores(txtNomeFornecedor.getText().trim(), txtCNPJ.getText().trim(), tableModel);
			}
		});

		// Ação do botão "Voltar"
		btnVoltar.addActionListener(e -> frame.dispose());

		// Configurar o layout principal
		frame.setLayout(new BorderLayout());
		frame.add(panelBusca, BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(panelVoltar, BorderLayout.SOUTH);

		// Exibir a janela
		frame.setVisible(true);
	}

	/**
	 * Busca fornecedores no banco de dados com base no termo de pesquisa informado.
	 * Os resultados são exibidos no modelo da tabela fornecido.
	 *
	 * @param nomeFornecedor nome do fornecedor para busca
	 * @param cnpj           CNPJ do fornecedor para busca
	 * @param tableModel     modelo da tabela onde os resultados serão exibidos
	 */
	private void buscarFornecedores(String nomeFornecedor, String cnpj, DefaultTableModel tableModel) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = conexaoSQL.obterConexao();
			String sql = "SELECT cnpj, razao_social, nome, telefone, email, rua, bairro, cidade FROM fornecedor WHERE 1=1";

			// Adicionar filtros conforme os campos preenchidos
			if (!nomeFornecedor.isEmpty())
				sql += " AND nome LIKE ?";
			if (!cnpj.isEmpty())
				sql += " AND cnpj LIKE ?";

			stmt = conn.prepareStatement(sql);

			// Definir os parâmetros de busca se o usuário forneceu algum valor
			int paramIndex = 1;
			if (!nomeFornecedor.isEmpty())
				stmt.setString(paramIndex++, "%" + nomeFornecedor + "%");
			if (!cnpj.isEmpty())
				stmt.setString(paramIndex++, "%" + cnpj + "%");

			rs = stmt.executeQuery();

			while (rs.next()) {
				long cnpjResult = rs.getLong("cnpj");
				String razaoSocial = rs.getString("razao_social");
				String nome = rs.getString("nome");
				String telefone = rs.getString("telefone");
				String email = rs.getString("email");
				String rua = rs.getString("rua");
				String bairro = rs.getString("bairro");
				String cidade = rs.getString("cidade");

				Object[] rowData = { cnpjResult, razaoSocial, nome, telefone, email, rua, bairro, cidade };
				tableModel.addRow(rowData);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro ao realizar a busca: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		} finally {
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
