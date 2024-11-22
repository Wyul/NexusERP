package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import gestao.Fornecedor;
import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por fornecer uma interface gráfica para o cadastro de fornecedores.
 * Permite que o usuário insira informações de fornecedor (CNPJ, razão social, endereço, etc.)
 * e as armazene no banco de dados.
 */
public class FornecedorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtCnpj, txtRazaoSocial, txtInscricaoEstadual, txtRepresentante, txtTelefone, txtEmail;
    private JTextField txtRua, txtBairro, txtCidade, txtNomeFornecedor;
    private Conexao conexao;

    /**
     * Construtor da classe `FornecedorFrame`.
     * Configura a interface gráfica e inicializa os componentes necessários para o cadastro de fornecedores.
     *
     * @param conexao Objeto de conexão com o banco de dados.
     */
    public FornecedorFrame(Conexao conexao) {
        this.conexao = conexao;

        setTitle("Cadastro de Fornecedor");
        setSize(500, 500); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(11, 2, 10, 10)); 
        mainPanel.setBackground(Color.decode("#C0C0C0"));

        // Adicionar labels e campos de entrada
        mainPanel.add(new JLabel("CNPJ:"));
        txtCnpj = new JTextField();
        mainPanel.add(txtCnpj);

        mainPanel.add(new JLabel("Razão Social:"));
        txtRazaoSocial = new JTextField();
        mainPanel.add(txtRazaoSocial);

        mainPanel.add(new JLabel("Inscrição Estadual:"));
        txtInscricaoEstadual = new JTextField();
        mainPanel.add(txtInscricaoEstadual);

        mainPanel.add(new JLabel("Representante:"));
        txtRepresentante = new JTextField();
        mainPanel.add(txtRepresentante);

        mainPanel.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        mainPanel.add(txtTelefone);

        mainPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        mainPanel.add(txtEmail);

        mainPanel.add(new JLabel("Rua:"));
        txtRua = new JTextField();
        mainPanel.add(txtRua);

        mainPanel.add(new JLabel("Bairro:"));
        txtBairro = new JTextField();
        mainPanel.add(txtBairro);

        mainPanel.add(new JLabel("Cidade:"));
        txtCidade = new JTextField();
        mainPanel.add(txtCidade);

        mainPanel.add(new JLabel("Nome do Fornecedor:"));
        txtNomeFornecedor = new JTextField();
        mainPanel.add(txtNomeFornecedor);

        // Painel para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        buttonPanel.setBackground(Color.decode("#C0C0C0"));

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(Color.decode("#2E8B57"));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setPreferredSize(new Dimension(120, 40));
        btnSalvar.setFocusPainted(false);
        btnSalvar.addActionListener(this::salvarFornecedor);

        // Botão Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setPreferredSize(new Dimension(120, 40));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        // Adicionar botões ao painel
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        // Adicionar painéis à janela
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Método responsável por salvar as informações do fornecedor no banco de dados.
     * Esse método valida os dados inseridos e realiza o cadastro.
     *
     * @param e Evento acionado pelo clique no botão "Salvar".
     */
    private void salvarFornecedor(ActionEvent e) {
        try {
            // Capturar dados do formulário
            long cnpj = Long.parseLong(txtCnpj.getText().replaceAll("[^0-9]", ""));
            String razaoSocial = txtRazaoSocial.getText();
            String inscricaoEstadual = txtInscricaoEstadual.getText();
            String representante = txtRepresentante.getText();
            long telefone = Long.parseLong(txtTelefone.getText().replaceAll("[^0-9]", ""));
            String email = txtEmail.getText();
            String rua = txtRua.getText();
            String bairro = txtBairro.getText();
            String cidade = txtCidade.getText();
            String nomeFornecedor = txtNomeFornecedor.getText();

            // Verificar campos obrigatórios
            if (razaoSocial.isEmpty() || inscricaoEstadual.isEmpty() || representante.isEmpty() ||
                email.isEmpty() || rua.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || nomeFornecedor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar objeto fornecedor
            Fornecedor fornecedor = new Fornecedor(cnpj, razaoSocial, inscricaoEstadual, representante, telefone,
                    email, rua, bairro, cidade, nomeFornecedor);

            // Inserir dados no banco
            try (Connection conn = conexao.obterConexao()) {
                String inserirSQL = "INSERT INTO fornecedor (cnpj, razao_social, ie_for, representante, telefone, email, rua, bairro, cidade, nome) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                var stmt = conn.prepareStatement(inserirSQL);
                stmt.setLong(1, fornecedor.getCnpj());
                stmt.setString(2, fornecedor.getRazaoSocial());
                stmt.setString(3, fornecedor.getInscricaoEstadual());
                stmt.setString(4, fornecedor.getRepresentante());
                stmt.setLong(5, fornecedor.getTelefone());
                stmt.setString(6, fornecedor.getEmail());
                stmt.setString(7, fornecedor.getRua());
                stmt.setString(8, fornecedor.getBairro());
                stmt.setString(9, fornecedor.getCidade());
                stmt.setString(10, fornecedor.getNome());
                stmt.executeUpdate();
                conn.commit();

                JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "CNPJ ou Telefone inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
