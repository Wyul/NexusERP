package front;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.*;

import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por fornecer uma interface gráfica para o cadastro de clientes.
 * Permite que o usuário insira informações de cliente (CPF/CNPJ, nome, endereço, telefone, etc.)
 * e as armazene no banco de dados.
 */
public class ClienteFrame extends JFrame {

    private static final long serialVersionUID = 7125715240622945208L;
    private JTextField txtCnpj, txtTelefone, txtNome, txtEmail, txtRua, txtBairro, txtCidade;
    private Conexao conexao;

    /**
     * Construtor da classe `ClienteFrame`.
     * Inicializa a interface gráfica e seus componentes.
     *
     * @param conexao Objeto de conexão com o banco de dados.
     */
    public ClienteFrame(Conexao conexao) {
        this.conexao = conexao;

        setTitle("Cadastro de Cliente");
        setSize(600, 400); // Dimensões ajustadas
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 2, 10, 10)); // Alinhar os campos em grade
        mainPanel.setBackground(Color.decode("#C0C0C0"));

        // Labels e campos de entrada
        JLabel lblCnpj = new JLabel("CNPJ/CPF:");
        txtCnpj = new JTextField();
        JLabel lblTelefone = new JLabel("Telefone:");
        txtTelefone = new JTextField();
        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField();
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();
        JLabel lblRua = new JLabel("Rua:");
        txtRua = new JTextField();
        JLabel lblBairro = new JLabel("Bairro:");
        txtBairro = new JTextField();
        JLabel lblCidade = new JLabel("Cidade:");
        txtCidade = new JTextField();

        // Adicionar componentes ao painel
        mainPanel.add(lblCnpj);
        mainPanel.add(txtCnpj);
        mainPanel.add(lblTelefone);
        mainPanel.add(txtTelefone);
        mainPanel.add(lblNome);
        mainPanel.add(txtNome);
        mainPanel.add(lblEmail);
        mainPanel.add(txtEmail);
        mainPanel.add(lblRua);
        mainPanel.add(txtRua);
        mainPanel.add(lblBairro);
        mainPanel.add(txtBairro);
        mainPanel.add(lblCidade);
        mainPanel.add(txtCidade);

        // Painel para botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#C0C0C0"));

        // Botão Salvar (Verde)
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(Color.decode("#2E8B57"));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setPreferredSize(new Dimension(120, 40)); // Botão maior
        btnSalvar.setFocusPainted(false);

        // Botão Cancelar (Vermelho)
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setPreferredSize(new Dimension(120, 40)); // Botão maior
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose()); // Fecha a janela

        // Adicionar ação ao botão Salvar
        btnSalvar.addActionListener(e -> {
            String[] opcoes = { "Cliente (CPF)", "Cliente (CNPJ)", "Cancelar" };
            int escolha = JOptionPane.showOptionDialog(this, "Você está cadastrando um:", "Escolha o tipo de cliente",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            if (escolha == 0) {
                salvarCliente("CPF");
            } else if (escolha == 1) {
                salvarCliente("CNPJ");
            } else {
                JOptionPane.showMessageDialog(this, "Operação cancelada.", "Cancelar", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Adicionar botões ao painel
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        // Adicionar painéis à janela
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Salva as informações de um cliente no banco de dados.
     *
     * @param tipoCliente O tipo do cliente, podendo ser "CPF" ou "CNPJ".
     */
    private void salvarCliente(String tipoCliente) {
        try {
            // Capturar dados dos campos
            String identificador = txtCnpj.getText().trim(); // Para CPF ou CNPJ
            String telefoneStr = txtTelefone.getText().trim();
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String rua = txtRua.getText().trim();
            String bairro = txtBairro.getText().trim();
            String cidade = txtCidade.getText().trim();

            // Validação básica
            if (identificador.isEmpty() || telefoneStr.isEmpty() || nome.isEmpty() || email.isEmpty() || rua.isEmpty()
                    || bairro.isEmpty() || cidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar CPF ou CNPJ
            if (tipoCliente.equals("CPF") && identificador.length() != 11) {
                JOptionPane.showMessageDialog(this, "CPF deve ter 11 dígitos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (tipoCliente.equals("CNPJ") && identificador.length() != 14) {
                JOptionPane.showMessageDialog(this, "CNPJ deve ter 14 dígitos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar e formatar o telefone (remover caracteres não numéricos)
            String telefoneFormatado = telefoneStr.replaceAll("[^0-9]", "");
            if (telefoneFormatado.length() < 10 || telefoneFormatado.length() > 11) {
                JOptionPane.showMessageDialog(this, "Telefone inválido! Deve ter 10 ou 11 dígitos.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = conexao.obterConexao()) {
                String inserirSQL = "INSERT INTO cliente (cnpj, telefone, nome, email, rua, bairro, cidade) VALUES (?, ?, ?, ?, ?, ?, ?)";
                var stmt = conn.prepareStatement(inserirSQL);
                stmt.setString(1, identificador);
                stmt.setString(2, telefoneFormatado);
                stmt.setString(3, nome);
                stmt.setString(4, email);
                stmt.setString(5, rua);
                stmt.setString(6, bairro);
                stmt.setString(7, cidade);
                stmt.executeUpdate();
                conn.commit();
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "CNPJ ou Telefone inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
