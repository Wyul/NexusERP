package front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import construtores.InicializadorFrame;
import modelo.dominio.dao.conexao.ConexaoSQL;

/**
 * Classe responsável por fornecer a interface principal do sistema de gestão.
 * Inclui opções para cadastro, visualização, backup e restauração de dados, além de gerenciamento de produtos e vendas.
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private InicializadorFrame inicializador;
    private ConexaoSQL conexao;

    /**
     * Construtor da classe `MainFrame`.
     * Configura a interface gráfica principal e inicializa os componentes necessários.
     */
    public MainFrame() {
        setTitle("Sistema de Gestão");
        // Configura a janela para tela cheia sem borda
        setUndecorated(true); // Remove a barra de título
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela
        setResizable(true); // Permite que a janela seja redimensionada

        conexao = new ConexaoSQL(); // Instância da conexão com o banco
        inicializarComponentes(conexao);

        setLayout(new BorderLayout());

        // Criação do painel de menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(13, 1, 10, 10)); // Ajustado para 13 opções
        menuPanel.setBackground(Color.decode("#FFFFFF"));

        // Definição das opções do menu
        String[] opcoes = {
            "Cadastrar Cliente",
            "Cadastrar Fornecedor",
            "Cadastrar Agendamento",
            "Cadastrar Produto",
            "Visualizar Clientes",
            "Visualizar Fornecedores",
            "Visualizar Agendamentos",
            "Visualizar Produtos",
            "Realizar Venda NF-e",
            "Visualizar Vendas",
            "Backup do Banco de Dados",
            "Restaurar Backup do Banco de Dados",
            "Sair"
        };

        // Adiciona os botões ao menu
        for (String opcao : opcoes) {
            JButton botao = criarBotaoArredondado(opcao);
            botao.addActionListener(e -> executarAcaoBotao(opcao));
            menuPanel.add(botao);
        }

        add(menuPanel, BorderLayout.WEST);

     // Painel central com imagem
        JPanel centralPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Caminho da imagem
                String caminhoImagem = "C:\\Users\\Ivan\\Documents\\ERP MECANICA\\java-jdbc\\logo7.png";
                try {
                    ImageIcon logo = new ImageIcon(caminhoImagem);
                    Image img = logo.getImage();

                    // Defina o tamanho desejado para a imagem
                    int larguraImagem = getWidth();  // Ajuste para preencher a largura do painel
                    int alturaImagem = getHeight();  // Ajuste para preencher a altura do painel

                    // Desenha a imagem redimensionada
                    g.drawImage(img, 0, 0, larguraImagem, alturaImagem, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        centralPanel.setBackground(Color.white); // Define o fundo branco
        centralPanel.setLayout(new BorderLayout());
        add(centralPanel, BorderLayout.CENTER);

        setVisible(true);
    }


    /**
     * Inicializa os componentes necessários para a aplicação.
     *
     * @param conexao Conexão com o banco de dados.
     */
    private void inicializarComponentes(ConexaoSQL conexao) {
        try {
            inicializador = new InicializadorFrame(conexao, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inicializar os componentes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cria um botão com estilo arredondado e formatação de texto.
     *
     * @param texto Texto a ser exibido no botão.
     * @return Um botão configurado com estilo.
     */
    private JButton criarBotaoArredondado(String texto) {
        String textoFormatado;
  

        // Adicionar quebras de linha apenas para textos longos
        switch (texto) {
            case "Cadastrar Agendamento":
                textoFormatado = "<html><center>Cadastrar<br>Agendamento</center></html>";
                break;
            case "Visualizar Fornecedores":
                textoFormatado = "<html><center>Visualizar<br>Fornecedores</center></html>";
                break;
            case "Visualizar Agendamentos":
                textoFormatado = "<html><center>Visualizar<br>Agendamentos</center></html>";
                break;
            case "Backup do Banco de Dados":
                textoFormatado = "<html><center>Backup<br>do Banco<br>de Dados</center></html>";
                break;
            case "Restaurar Backup do Banco de Dados":
                textoFormatado = "<html><center>Restaurar<br>Backup<br>do Banco<br>de Dados</center></html>";
                break;
            default:
                textoFormatado = texto;
        }

        JButton botao = new JButton(textoFormatado);

        // Definindo o ícone
        String caminhoImagem = "C:\\Users\\Ivan\\Documents\\ERP MECANICA\\java-jdbc\\chave.png";
        try {
            ImageIcon icon = new ImageIcon(caminhoImagem);
            botao.setIcon(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))); // Redimensionando a imagem para se ajustar ao botão
        } catch (Exception e) {
            e.printStackTrace();
        }

        botao.setFocusPainted(false);
        botao.setBorderPainted(true);  // Corrigido para borda visível
        botao.setContentAreaFilled(true);  // Fundo transparente
        botao.setOpaque(true);  // Removendo a opacidade do botão
        botao.setForeground(Color.black);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setPreferredSize(new Dimension(200, 70));  // Ajuste do tamanho
        botao.setBorder(new javax.swing.border.LineBorder(new Color(173, 216, 230), 2, true));  // Bordas cinzas e arredondadas

        return botao;
    }


    /**
     * Executa uma ação com base no botão clicado.
     *
     * @param acao Texto do botão clicado.
     */
    private void executarAcaoBotao(String acao) {
    	String textoFormatado;
        switch (acao) {
            case "Cadastrar Cliente":
                abrirCadastroCliente();
                textoFormatado = "<html><center>Cadastrar<br>Agendamento</center></html>";
                break;
            case "Cadastrar Fornecedor":
                abrirCadastroFornecedor();
                break;
            case "Cadastrar Agendamento":
                abrirCadastroAgendamento();
                break;
            case "Cadastrar Produto":
                abrirCadastroProduto();
                break;
            case "Visualizar Clientes":
                executarVisualizarClientes();
                break;
            case "Visualizar Fornecedores":
                executarVisualizarFornecedores();
                break;
            case "Visualizar Agendamentos":
                executarVisualizarAgendamentos();
                break;
            case "Visualizar Produtos":
                abrirVisualizarProduto();
                break;
            case "Realizar Venda NF-e":
                abrirVendaNFE();
                break;
            case "Visualizar Vendas":
                executarVisualizarVendas();
                break;
            case "Backup do Banco de Dados":
                abrirBackupBancoDeDados();
                break;
            case "Restaurar Backup do Banco de Dados":
                abrirRestaurarBackupBancoDeDados();
                break;
            case "Sair":
                System.exit(0);
                break;
        }
    }

    /**
     * Abre a tela de cadastro de clientes.
     */
    private void abrirCadastroCliente() {
        new ClienteFrame(conexao);
    }

    /**
     * Abre a tela de cadastro de fornecedores.
     */
    private void abrirCadastroFornecedor() {
        new FornecedorFrame(conexao);
    }

    /**
     * Abre a tela de cadastro de agendamentos.
     */
    private void abrirCadastroAgendamento() {
        new AgendamentoFrame(conexao);
    }

    /**
     * Abre a tela de cadastro de produtos.
     */
    private void abrirCadastroProduto() {
        new ProdutoFrame(conexao);
    }

    /**
     * Abre a tela de visualização de produtos.
     */
    private void abrirVisualizarProduto() {
        try {
            new TelaEstoque(conexao).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a tela para realizar vendas NF-e.
     */
    private void abrirVendaNFE() {
        try {
            VendaNFEFrame vendaNFEFrame = new VendaNFEFrame(conexao);
            vendaNFEFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir a tela de venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa a visualização de clientes.
     */
    private void executarVisualizarClientes() {
        try {
            inicializador.getVisualizarClientes().listarClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa a visualização de fornecedores.
     */
    private void executarVisualizarFornecedores() {
        try {
            inicializador.getVisualizarFornecedores().exibirFornecedores();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar fornecedores: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa a visualização de agendamentos.
     */
    private void executarVisualizarAgendamentos() {
        try {
            inicializador.getVisualizarAgendamentos().exibirAgendamentos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar agendamentos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa a visualização de vendas.
     */
    private void executarVisualizarVendas() {
        try {
            inicializador.getVisualizarVendaNFE().exibirVendas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar vendas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a interface de backup do banco de dados.
     */
    private void abrirBackupBancoDeDados() {
        new BackupPostgresUI().setVisible(true);
    }

    /**
     * Abre a interface para restaurar backups do banco de dados.
     */
    private void abrirRestaurarBackupBancoDeDados() {
        new RestaurarBackupUI().setVisible(true);
    }

    /**
     * Método principal que inicia a aplicação.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setLoginListener(() -> new MainFrame());
        });
    }
}