package front;

import modelo.dominio.dao.conexao.BackupPostgres;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que fornece uma interface gráfica para restaurar backups do banco de dados.
 * Permite ao usuário selecionar um arquivo de backup e iniciar o processo de restauração.
 */
public class RestaurarBackupUI extends JFrame {

    private static String backupFile = ""; // Caminho do arquivo de backup padrão
    private JTextField fileField;
    private JButton chooseFileButton;
    private JButton restoreButton;

    /**
     * Método principal para inicializar e exibir a interface gráfica.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RestaurarBackupUI().setVisible(true);
        });
    }

    /**
     * Construtor da classe `RestaurarBackupUI`.
     * Configura a interface gráfica e inicializa os componentes necessários.
     */
    public RestaurarBackupUI() {
        setTitle("Restaurar Backup do Banco de Dados");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criação do painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Campo de texto para exibir o arquivo de backup selecionado
        fileField = new JTextField(30);
        fileField.setEditable(false);
        fileField.setBackground(Color.WHITE);
        fileField.setText(backupFile);

        // Botão para escolher o arquivo de backup
        chooseFileButton = new JButton("Escolher Arquivo");
        chooseFileButton.setBackground(new Color(76, 175, 80)); // Cor verde
        chooseFileButton.setForeground(Color.WHITE);

        // Botão para iniciar a restauração do backup
        restoreButton = new JButton("Restaurar Backup");
        restoreButton.setBackground(new Color(33, 150, 243)); // Cor azul
        restoreButton.setForeground(Color.WHITE);

        // Adicionar os componentes ao painel
        panel.add(fileField);
        panel.add(chooseFileButton);
        panel.add(restoreButton);

        // Adicionar o painel à janela
        add(panel);

        // Configuração da ação do botão para escolher o arquivo
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir diálogo para seleção do arquivo
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Escolher arquivo de backup");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new java.io.File(backupFile));

                int result = fileChooser.showOpenDialog(RestaurarBackupUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    backupFile = fileChooser.getSelectedFile().getAbsolutePath();
                    fileField.setText(backupFile);
                }
            }
        });

        // Configuração da ação do botão para restaurar o backup
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Atualizar o caminho do arquivo de backup e iniciar a restauração
                BackupPostgres.BACKUP_FILE = backupFile;
                BackupPostgres.restaurarBackup();
            }
        });
    }
}
