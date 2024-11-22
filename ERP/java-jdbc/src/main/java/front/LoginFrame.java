package front;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import modelo.dominio.dao.conexao.ConexaoSQL;
import modelo.dominio.dao.usuario.UsuarioDAO;

/**
 * Classe responsável por fornecer uma interface gráfica para login no sistema.
 * Valida as credenciais do usuário através de uma conexão com o banco de dados.
 */
public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JButton btnLogin;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Runnable loginListener; // Listener para notificar o sucesso no login
    private UsuarioDAO usuarioDAO;

    /**
     * Construtor da classe `LoginFrame`.
     * Configura a interface gráfica para login e inicializa os componentes.
     */
    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Criação dos componentes da tela de login
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Login");

        // Conexão com o banco de dados
        ConexaoSQL conexao = new ConexaoSQL();
        usuarioDAO = new UsuarioDAO(conexao);

        // Adicionando os componentes na tela
        JPanel panel = new JPanel();
        panel.add(new JLabel("Usuário:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Senha:"));
        panel.add(txtPassword);
        panel.add(btnLogin);

        add(panel);

        // Ação do botão de login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                // Verifica as credenciais no banco de dados
                if (usuarioDAO.validarCredenciais(username, password)) {
                    if (loginListener != null) {
                        loginListener.run(); // Chama o listener (abre o MainFrame)
                    }
                    dispose(); // Fecha a tela de login
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    /**
     * Define o listener que será chamado quando o login for bem-sucedido.
     *
     * @param listener Runnable que será executado após o sucesso no login.
     */
    public void setLoginListener(Runnable listener) {
        this.loginListener = listener;
    }
}
