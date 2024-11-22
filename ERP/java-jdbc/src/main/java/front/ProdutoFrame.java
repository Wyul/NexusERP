package front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import modelo.controller.Categoria;
import modelo.controller.Produto;
import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por fornecer a interface gráfica para o cadastro de produtos.
 * Permite que o usuário insira informações sobre produtos ou serviços e os salve no banco de dados.
 */
public class ProdutoFrame extends JFrame {

    private final Conexao conexao;
    private static final long serialVersionUID = 1L;
    private JTextField txtNome, txtDescricao, txtPreco, txtQuantidade, txtCategoria, txtCusto;
    private JComboBox<String> comboTipo;

    /**
     * Construtor da classe `ProdutoFrame`.
     * Configura a interface gráfica e inicializa os componentes necessários.
     *
     * @param conexao Instância de conexão com o banco de dados.
     */
    public ProdutoFrame(Conexao conexao) {
        this.conexao = conexao;

        setTitle("Cadastro de Produto");
        setSize(600, 500); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuração do painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(10, 2, 10, 10)); 
        mainPanel.setBackground(Color.decode("#C0C0C0"));

        // Adicionar campos de entrada e rótulos
        mainPanel.add(createLabel("Nome:"));
        txtNome = new JTextField();
        mainPanel.add(txtNome);

        mainPanel.add(createLabel("Código:"));
        txtDescricao = new JTextField();
        mainPanel.add(txtDescricao);

        mainPanel.add(createLabel("Preço de Venda:"));
        txtPreco = new JTextField();
        mainPanel.add(txtPreco);

        mainPanel.add(createLabel("Preço de Custo:"));
        txtCusto = new JTextField();
        mainPanel.add(txtCusto);

        mainPanel.add(createLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        mainPanel.add(txtQuantidade);

        mainPanel.add(createLabel("Tipo de Embalagem:"));
        txtCategoria = new JTextField();
        mainPanel.add(txtCategoria);

        // ComboBox para selecionar o tipo (Produto ou Serviço)
        mainPanel.add(createLabel("Tipo (Produto/Serviço):"));
        comboTipo = new JComboBox<>(new String[] { "Produto", "Serviço" });
        mainPanel.add(comboTipo);

        // Configuração do painel de botões
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
        btnSalvar.addActionListener(this::salvarProduto);

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

        // Adicionar os painéis à janela
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Cria um rótulo (JLabel) configurado com fonte e estilo padrão.
     *
     * @param text O texto a ser exibido no rótulo.
     * @return JLabel configurado.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    /**
     * Lida com o evento de salvar o produto, validando os dados de entrada e inserindo no banco de dados.
     *
     * @param e Evento de ação acionado pelo botão "Salvar".
     */
    private void salvarProduto(ActionEvent e) {
        try {
            // Validação e captura dos dados
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText(); 
            String precoVendaStr = txtPreco.getText().replace(",", ".");
            String precoCustoStr = txtCusto.getText().replace(",", ".");
            BigDecimal preco = new BigDecimal(precoVendaStr); 
            BigDecimal precoCusto = new BigDecimal(precoCustoStr); 
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            String embalagem = txtCategoria.getText(); 
            String tipo = (String) comboTipo.getSelectedItem();

            // Criar categoria
            Categoria categoria = new Categoria();
            categoria.setNome(embalagem);

            // Criar objeto Produto
            Produto produto = new Produto(0, nome, descricao, preco, quantidade, categoria, null, LocalDateTime.now());

            // Inserir produto no banco de dados
            try (Connection conn = conexao.obterConexao()) {
                String inserirSQL = "INSERT INTO estoque (codigo_pro, nome_pro, quant_pro, prc_custo, prc_venda, tipo_emba, prod_ser, estado) VALUES (?, ?, ?, ?, ?, ?, ?, true)";
                try (PreparedStatement stmt = conn.prepareStatement(inserirSQL)) {
                    stmt.setString(1, descricao);
                    stmt.setString(2, nome);
                    stmt.setInt(3, quantidade);
                    stmt.setBigDecimal(4, precoCusto);
                    stmt.setBigDecimal(5, preco);
                    stmt.setString(6, embalagem);
                    stmt.setString(7, tipo);

                    stmt.executeUpdate();
                    conn.commit();

                    JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os campos numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o produto: " + ex.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
