package modelo.dominio.dao.conexao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface que define os métodos necessários para gerenciar a conexão com o banco de dados.
 */
public interface Conexao {

    /**
     * Obtém uma nova conexão com o banco de dados.
     *
     * @return uma instância de {@link Connection} conectada ao banco de dados
     * @throws SQLException se ocorrer um erro ao estabelecer a conexão
     */
    Connection obterConexao() throws SQLException;

    /**
     * Retorna a conexão atual com o banco de dados.
     *
     * @return uma instância de {@link Connection} representando a conexão ativa
     */
    Connection getConnection();
}
