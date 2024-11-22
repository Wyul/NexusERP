package modelo.dominio.dao.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados PostgreSQL.
 * Implementa a interface {@link Conexao}.
 */
public class ConexaoSQL implements Conexao {
    // Informações para se conectar ao banco
    private static final String URL = "jdbc:postgresql://localhost:5432/erp"; // URL do banco
    private static final String USER = "postgres"; // Usuário do banco
    private static final String PASSWORD = "1234"; // Senha do banco
    private static Connection connection = null;

    /**
     * Bloco estático responsável por estabelecer a conexão inicial com o banco de dados.
     */
    static {
        conectar();
    }

    /**
     * Construtor padrão para a classe {@code ConexaoSQL}.
     * A conexão inicial é tratada no bloco estático.
     */
    public ConexaoSQL() {
        // Construtor vazio, pois o bloco estático já lida com a conexão
    }

    /**
     * Método privado responsável por estabelecer a conexão com o banco de dados.
     * Caso a conexão seja bem-sucedida, o modo de auto commit é desabilitado.
     */
    private static void conectar() {
        try {
            if (connection == null || connection.isClosed()) {
                // Carregar o driver do PostgreSQL
                Class.forName("org.postgresql.Driver");
                // Estabelecer a conexão com o banco
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // Desabilitar auto commit para controle manual de transações
                connection.setAutoCommit(false);
                System.out.println("Conectado ao banco de dados com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    /**
     * Obtém uma conexão válida com o banco de dados.
     * Caso a conexão esteja fechada ou nula, uma nova conexão é estabelecida.
     *
     * @return uma instância de {@link Connection} conectada ao banco de dados
     * @throws SQLException se ocorrer um erro ao tentar estabelecer a conexão
     */
    @Override
    public Connection obterConexao() throws SQLException {
        if (connection == null || connection.isClosed()) {
            conectar();
        }
        return connection;
    }

    /**
     * Retorna a conexão atual com o banco de dados.
     *
     * @return a conexão ativa ou {@code null} se não houver conexão estabelecida
     */
    @Override
    public Connection getConnection() {
        return connection;
    }
}
