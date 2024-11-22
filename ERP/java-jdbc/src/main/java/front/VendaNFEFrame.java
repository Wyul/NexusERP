package front;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gestao.venda.VendaNFE;
import modelo.dominio.dao.conexao.Conexao;

public class VendaNFEFrame extends JFrame {

    private static final long serialVersionUID = 1;
    private JTextField txtIdAgendamento, txtFormaPagamento, txtDefeito, txtCodPeca, txtQtdPeca;
    private JCheckBox chkVendaPeca;
    private Conexao conexao;
    private ArrayList<String> codigosProdutos;
    private ArrayList<Integer> quantidadesProdutos;
    private double valorTotal;

    public VendaNFEFrame(Conexao conexao) {
        this.conexao = conexao;
        codigosProdutos = new ArrayList<>();
        quantidadesProdutos = new ArrayList<>();
        valorTotal = 0.0;

        // Configurações básicas da janela
        setTitle("Realizar Venda NF-e");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#C0C0C0")); // Cor de fundo cinza claro
        mainPanel.setLayout(null);

        // Labels e campos de entrada
        JLabel lblIdAgendamento = new JLabel("ID do Agendamento:");
        lblIdAgendamento.setBounds(30, 20, 150, 25);
        mainPanel.add(lblIdAgendamento);

        txtIdAgendamento = new JTextField();
        txtIdAgendamento.setBounds(180, 20, 250, 25);
        mainPanel.add(txtIdAgendamento);

        JLabel lblFormaPagamento = new JLabel("Forma de Pagamento:");
        lblFormaPagamento.setBounds(30, 60, 150, 25);
        mainPanel.add(lblFormaPagamento);

        txtFormaPagamento = new JTextField();
        txtFormaPagamento.setBounds(180, 60, 250, 25);
        mainPanel.add(txtFormaPagamento);

        JLabel lblDefeito = new JLabel("Defeito Solucionado:");
        lblDefeito.setBounds(30, 100, 150, 25);
        mainPanel.add(lblDefeito);

        txtDefeito = new JTextField();
        txtDefeito.setBounds(180, 100, 250, 25);
        mainPanel.add(txtDefeito);

        JLabel lblVendaPeca = new JLabel("Venda de Peça?");
        lblVendaPeca.setBounds(30, 140, 150, 25);
        mainPanel.add(lblVendaPeca);

        chkVendaPeca = new JCheckBox();
        chkVendaPeca.setBounds(180, 140, 20, 25);
        chkVendaPeca.setBackground(Color.decode("#C0C0C0")); // Mesma cor do painel principal
        chkVendaPeca.setOpaque(false); // Deixa o fundo do checkbox transparente
        mainPanel.add(chkVendaPeca);

        JLabel lblCodPeca = new JLabel("Código da Peça:");
        lblCodPeca.setBounds(30, 180, 150, 25);
        mainPanel.add(lblCodPeca);

        txtCodPeca = new JTextField();
        txtCodPeca.setBounds(180, 180, 250, 25);
        mainPanel.add(txtCodPeca);

        JLabel lblQtdPeca = new JLabel("Quantidade:");
        lblQtdPeca.setBounds(30, 220, 150, 25);
        mainPanel.add(lblQtdPeca);

        txtQtdPeca = new JTextField();
        txtQtdPeca.setBounds(180, 220, 250, 25);
        mainPanel.add(txtQtdPeca);

        // Botão para adicionar produto à venda
        JButton btnAdicionarProduto = new JButton("Adicionar Produto");
        btnAdicionarProduto.setBounds(180, 260, 150, 30);
        mainPanel.add(btnAdicionarProduto);

        // Exibir valor total da venda
        JLabel lblValorTotal = new JLabel("Valor Total: R$ 0,00");
        lblValorTotal.setBounds(30, 300, 250, 25);
        mainPanel.add(lblValorTotal);

        // Ação do botão Adicionar Produto
        btnAdicionarProduto.addActionListener((ActionEvent e) -> {
            try {
                String codPeca = txtCodPeca.getText();
                int qtdPeca = Integer.parseInt(txtQtdPeca.getText());

                if (codPeca.isEmpty() || qtdPeca <= 0) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um código de peça válido e quantidade maior que 0.");
                    return;
                }

                // Adicionar produto à lista de produtos e calcular valor total
                double valorProduto = buscarValorProduto(codPeca);
                if (valorProduto > 0) {
                    codigosProdutos.add(codPeca);
                    quantidadesProdutos.add(qtdPeca);
                    valorTotal += valorProduto * qtdPeca;

                    // Atualizar o valor total na tela
                    lblValorTotal.setText("Valor Total: R$ " + String.format("%.2f", valorTotal));
                    txtCodPeca.setText("");
                    txtQtdPeca.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Produto não encontrado no estoque.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira uma quantidade válida.");
            }
        });

        // Botão de Confirmar
        JButton btnConfirmar = new JButton("Confirmar Venda");
        btnConfirmar.setBounds(180, 340, 150, 30);
        mainPanel.add(btnConfirmar);

        // Ação do botão Confirmar
        btnConfirmar.addActionListener((ActionEvent e) -> {
            try {
                int idAgendamento = Integer.parseInt(txtIdAgendamento.getText());
                String formaPagamento = txtFormaPagamento.getText();
                String defeito = txtDefeito.getText();

                if (formaPagamento.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, informe a forma de pagamento.");
                    return;
                }

                // Criar objeto VendaNFE e realizar a venda
                VendaNFE vendaNFE = new VendaNFE(conexao);
                boolean sucesso = vendaNFE.realizarVenda(idAgendamento, formaPagamento, defeito, codigosProdutos, quantidadesProdutos);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Venda NF-e realizada com sucesso!");
                    dispose(); // Fecha a janela
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao realizar a venda.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID do Agendamento inválido.");
            }
        });

        // Adicionar painel à janela
        add(mainPanel);
    }

    // Método para buscar o valor de uma peça no estoque
    private double buscarValorProduto(String codPeca) {
        double valorProduto = 0.0;
        try (Connection conn = conexao.obterConexao();
             PreparedStatement stmt = conn.prepareStatement("SELECT prc_venda FROM estoque WHERE codigo_pro = ?")) {

            stmt.setString(1, codPeca);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    valorProduto = rs.getDouble("prc_venda");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar o valor do produto: " + e.getMessage());
        }
        return valorProduto;
    }
}