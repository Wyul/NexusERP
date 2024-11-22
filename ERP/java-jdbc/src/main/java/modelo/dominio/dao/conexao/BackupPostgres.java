package modelo.dominio.dao.conexao;

import java.io.IOException;

/**
 * Classe responsável por realizar backup e restauração do banco de dados PostgreSQL.
 * Utiliza as ferramentas pg_dump para backup e pg_restore para restauração.
 * <p>
 * Configurações como caminho dos executáveis e informações do banco são definidas como constantes.
 * O diretório de backup e o arquivo de backup para restauração são configuráveis.
 * </p>
 */
public class BackupPostgres {

    /**
     * Caminho para o executável do pg_dump.
     */
    private static final String PG_DUMP_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump.exe";

    /**
     * Caminho para o executável do pg_restore.
     */
    private static final String PG_RESTORE_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_restore.exe";

    /**
     * Endereço do servidor do banco de dados PostgreSQL.
     */
    private static final String DB_HOST = "localhost";

    /**
     * Porta utilizada para a conexão com o banco de dados PostgreSQL.
     */
    private static final String DB_PORT = "5432";

    /**
     * Nome do banco de dados a ser utilizado.
     */
    private static final String DB_NAME = "erp";

    /**
     * Nome do usuário do banco de dados PostgreSQL.
     */
    private static final String DB_USER = "postgres";

    /**
     * Senha do usuário do banco de dados PostgreSQL.
     */
    private static final String DB_PASSWORD = "1234";

    /**
     * Diretório padrão onde os arquivos de backup serão salvos.
     */
    public static String BACKUP_DIR = "";

    /**
     * Caminho completo para o arquivo de backup que será utilizado na restauração.
     */
    public static String BACKUP_FILE;

    /**
     * Realiza o backup do banco de dados PostgreSQL.
     * <p>
     * Gera um arquivo de backup no formato comprimido (.backup) no diretório especificado
     * na variável {@code BACKUP_DIR}. O nome do arquivo é gerado com base no nome do banco de dados.
     * </p>
     */
    public static void realizarBackup() {
        String comando = String.format("\"%s\" -h %s -p %s -U %s -F c -b -v -f \"%s%s.backup\" %s",
                PG_DUMP_PATH, DB_HOST, DB_PORT, DB_USER, BACKUP_DIR, DB_NAME, DB_NAME);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.environment().put("PGPASSWORD", DB_PASSWORD);
        processBuilder.environment().put("PGUSER", DB_USER);
        processBuilder.environment().put("PGHOST", DB_HOST);
        processBuilder.environment().put("PGPORT", DB_PORT);

        try {
            processBuilder.command("cmd", "/c", comando);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup realizado com sucesso!");
            } else {
                System.err.println("Erro ao realizar o backup. Código de saída: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar o backup: " + e.getMessage());
        }
    }

    /**
     * Restaura o banco de dados PostgreSQL a partir de um arquivo de backup.
     * <p>
     * O arquivo de backup deve ser especificado na variável {@code BACKUP_FILE}.
     * O processo utiliza o comando pg_restore para executar a restauração no banco de dados.
     * </p>
     */
    public static void restaurarBackup() {
        if (BACKUP_FILE == null || BACKUP_FILE.isEmpty()) {
            System.err.println("Arquivo de backup não especificado.");
            return;
        }

        String comando = String.format("\"%s\" -h %s -p %s -U %s -d %s -v \"%s\"",
                PG_RESTORE_PATH, DB_HOST, DB_PORT, DB_USER, DB_NAME, BACKUP_FILE);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.environment().put("PGPASSWORD", DB_PASSWORD);
        processBuilder.environment().put("PGUSER", DB_USER);
        processBuilder.environment().put("PGHOST", DB_HOST);
        processBuilder.environment().put("PGPORT", DB_PORT);

        try {
            processBuilder.command("cmd", "/c", comando);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup restaurado com sucesso!");
            } else {
                System.err.println("Erro ao restaurar o backup. Código de saída: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar a restauração: " + e.getMessage());
        }
    }
}
