package front;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por gerenciar o cadastro de agendamentos na aplicação.
 * Exibe uma interface gráfica para entrada de dados relacionados a agendamentos
 * e realiza a validação e persistência dos dados no banco.
 */
public class AgendamentoFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final Conexao conexao;
    private JTextField txtIdCliente, txtDataAtendimento, txtModelo, txtMarca, txtSituacao, txtObservacao;

    /**
     * Construtor da classe `AgendamentoFrame`.
     * Configura a interface gráfica e inicializa os componentes necessários para
     * o cadastro de agendamentos.
     *
     * @param conexao Objeto de conexão com o banco de dados.
     */
    public AgendamentoFrame(Conexao conexao) {
        this.conexao = conexao;
        setTitle("Cadastro de Agendamento");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        mainPanel.setBackground(Color.decode("#C0C0C0"));

        mainPanel.add(createLabel("ID do Cliente:"));
        txtIdCliente = new JTextField();
        mainPanel.add(txtIdCliente);

        mainPanel.add(createLabel("Data de Atendimento (DD-MM-AAAA):"));
        txtDataAtendimento = new JTextField();
        mainPanel.add(txtDataAtendimento);

        mainPanel.add(createLabel("Modelo:"));
        txtModelo = new JTextField();
        mainPanel.add(txtModelo);

        mainPanel.add(createLabel("Marca:"));
        txtMarca = new JTextField();
        mainPanel.add(txtMarca);

        mainPanel.add(createLabel("Situação (A: Aberto, P: Pendente, F: Fechado):"));
        txtSituacao = new JTextField();
        mainPanel.add(txtSituacao);

        mainPanel.add(createLabel("Observação:"));
        txtObservacao = new JTextField();
        mainPanel.add(txtObservacao);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#C0C0C0"));


        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(Color.decode("#2E8B57"));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setPreferredSize(new Dimension(150, 40));
        btnSalvar.setFocusPainted(false);
        btnSalvar.addActionListener(this::salvarAgendamento);


        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Cria um JLabel com estilização padrão para os formulários.
     *
     * @param text O texto a ser exibido no label.
     * @return Um JLabel configurado.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    /**
     * Realiza a validação e o salvamento de um novo agendamento no banco de dados.
     * Esse método é executado quando o botão "Salvar" é clicado.
     *
     * @param e O evento de clique do botão.
     */
    private void salvarAgendamento(ActionEvent e) {
        String idClienteStr = txtIdCliente.getText().trim();
        String dataAtendimento = txtDataAtendimento.getText().trim();
        String modelo = txtModelo.getText().trim();
        String marca = txtMarca.getText().trim();
        String situacao = txtSituacao.getText().trim().toUpperCase();
        String observacao = txtObservacao.getText().trim();

        if (idClienteStr.isEmpty() || !idClienteStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "O campo 'ID do Cliente' deve conter apenas números!", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dataAtendimento.isEmpty() || modelo.isEmpty() || marca.isEmpty() || situacao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos!", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idCliente = Integer.parseInt(idClienteStr);

        try (Connection conn = conexao.obterConexao()) {

            String queryCliente = "SELECT cnpj, telefone, nome FROM cliente WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryCliente)) {
                stmt.setInt(1, idCliente);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Cliente não encontrado!", "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    conn.setAutoCommit(false);

                    String cnpj = rs.getString("cnpj");
                    String telefone = rs.getString("telefone");
                    String nomeCliente = rs.getString("nome");

                    String inserirSQL = "INSERT INTO agendamento (id_cliente, cnpj, telefone, nome_cliente, data_entrada, status, nome_veiculo, marca_veiculo, obs_age) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(inserirSQL)) {
                        insertStmt.setInt(1, idCliente);
                        insertStmt.setString(2, cnpj);
                        insertStmt.setString(3, telefone);
                        insertStmt.setString(4, nomeCliente);
                        insertStmt.setString(5, dataAtendimento);
                        insertStmt.setString(6, situacao);
                        insertStmt.setString(7, modelo);
                        insertStmt.setString(8, marca);
                        insertStmt.setString(9, observacao);
                        insertStmt.executeUpdate();
                    }
                    conn.commit();

                    JOptionPane.showMessageDialog(this, "Agendamento cadastrado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + ex.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
