package front;

import modelo.dominio.dao.conexao.BackupPostgres;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe responsável por fornecer uma interface gráfica para realizar o backup do banco de dados PostgreSQL.
 * Permite que o usuário escolha um diretório de destino para o arquivo de backup e execute o processo de backup.
 */
public class BackupPostgresUI extends JFrame {

    private static String backupDir = ""; // Diretório padrão para backup
    private JTextField directoryField;
    private JButton chooseDirectoryButton;
    private JButton backupButton;

    /**
     * Método principal para iniciar a aplicação de backup.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BackupPostgresUI().setVisible(true);
        });
    }

    /**
     * Construtor da classe `BackupPostgresUI`.
     * Configura a interface gráfica e inicializa os componentes necessários para o processo de backup.
     */
    public BackupPostgresUI() {
        setTitle("Backup do Banco de Dados");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas a janela de backup
        setLocationRelativeTo(null);

        // Criar o painel de layout
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Caixa de texto para exibir o diretório de backup
        directoryField = new JTextField(30);
        directoryField.setEditable(false);
        directoryField.setBackground(Color.WHITE);
        directoryField.setText(backupDir);

        // Botão para escolher o diretório
        chooseDirectoryButton = new JButton("Escolher Diretório");
        chooseDirectoryButton.setBackground(new Color(76, 175, 80)); // Cor verde
        chooseDirectoryButton.setForeground(Color.WHITE);

        // Botão para realizar o backup
        backupButton = new JButton("Realizar Backup");
        backupButton.setBackground(new Color(33, 150, 243)); // Cor azul
        backupButton.setForeground(Color.WHITE);

        // Adicionar os componentes ao painel
        panel.add(directoryField);
        panel.add(chooseDirectoryButton);
        panel.add(backupButton);

        // Adicionar o painel à janela
        add(panel);

        // Configurar ação para escolher o diretório
        configureChooseDirectoryAction();

        // Configurar ação para realizar o backup
        configureBackupAction();
    }

    /**
     * Configura a ação para o botão "Escolher Diretório".
     * Abre um diálogo para que o usuário selecione o diretório onde o backup será salvo.
     */
    private void configureChooseDirectoryAction() {
        chooseDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser directoryChooser = new JFileChooser();
                directoryChooser.setDialogTitle("Escolher diretório para backup");
                directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                directoryChooser.setCurrentDirectory(new java.io.File(backupDir));

                int result = directoryChooser.showOpenDialog(BackupPostgresUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    backupDir = directoryChooser.getSelectedFile().getAbsolutePath() + "\\";
                    directoryField.setText(backupDir);
                }
            }
        });
    }

    /**
     * Configura a ação para o botão "Realizar Backup".
     * Atualiza o diretório de backup e executa o método de backup no objeto `BackupPostgres`.
     */
    private void configureBackupAction() {
        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Atualizar diretório de backup e realizar o processo
                BackupPostgres.BACKUP_DIR = backupDir;
                BackupPostgres.realizarBackup();
            }
        });
    }
}
