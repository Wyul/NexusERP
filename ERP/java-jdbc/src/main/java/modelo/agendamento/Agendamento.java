package modelo.agendamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import modelo.dominio.dao.conexao.Conexao;

/**
 * Classe responsável por gerenciar o agendamento de serviços no sistema.
 */
public class Agendamento {

    private Conexao conexao;

    /**
     * Construtor para inicializar a conexão com o banco de dados.
     *
     * @param conexao Instância de {@link Conexao} para interação com o banco de dados.
     */
    public Agendamento(Conexao conexao) {
        this.conexao = conexao;
    }

    /**
     * Solicita entrada do usuário no console com uma mensagem específica.
     * 
     * @param scanner Instância de {@link Scanner} para entrada de dados do usuário.
     * @param mensagem Mensagem a ser exibida ao usuário.
     * @return Entrada fornecida pelo usuário ou {@code null} se ele optar por voltar ao menu principal.
     */
    private String solicitarEntrada(Scanner scanner, String mensagem) {
        System.out.print(mensagem + " (ou digite 'voltar' para retornar): ");
        String entrada = scanner.nextLine();
        if ("voltar".equalsIgnoreCase(entrada)) {
            System.out.println("Retornando ao menu principal...");
            return null;
        }
        return entrada;
    }

    /**
     * Realiza o processo de agendamento, coletando dados do usuário e salvando no banco de dados.
     *
     * @param scanner Instância de {@link Scanner} para entrada de dados do usuário.
     * @return {@code true} se o agendamento for realizado com sucesso, {@code false} caso contrário.
     */
    public boolean agendar(Scanner scanner) {
        Connection conn = null;
        PreparedStatement stmtConsulta = null;
        PreparedStatement stmtInsercao = null;
        ResultSet rs = null;

        try {
            // Coleta informações do agendamento
            String clienteIdStr = solicitarEntrada(scanner, "Informe o ID do cliente:");
            if (clienteIdStr == null) return false;
            int clienteId = Integer.parseInt(clienteIdStr);

            String nomeVeiculo = solicitarEntrada(scanner, "Informe o nome do veículo:");
            if (nomeVeiculo == null) return false;

            String marcaVeiculo = solicitarEntrada(scanner, "Informe a marca do veículo:");
            if (marcaVeiculo == null) return false;

            String dataEntradaStr = solicitarEntrada(scanner, "Informe a data de entrada (dd/MM/yyyy):");
            if (dataEntradaStr == null) return false;
            Date dataEntrada = new SimpleDateFormat("dd/MM/yyyy").parse(dataEntradaStr);

            String status = solicitarEntrada(scanner, "Informe o status (A - Agendado, P - Pendente, F - Finalizado):");
            if (status == null) return false;
            status = status.toUpperCase();

            String observacao = solicitarEntrada(scanner, "Relato do cliente (ou pressione Enter para pular):");
            if (observacao == null) return false;

            // Conecta ao banco de dados
            conn = conexao.obterConexao();
            if (conn == null) {
                throw new SQLException("Conexão ao banco de dados não foi estabelecida.");
            }
            conn.setAutoCommit(false);

            // Consulta dados do cliente
            String sqlConsulta = "SELECT id, cnpj, telefone, nome FROM cliente WHERE id = ?";
            stmtConsulta = conn.prepareStatement(sqlConsulta);
            stmtConsulta.setInt(1, clienteId);
            rs = stmtConsulta.executeQuery();

            if (rs.next()) {
                int idCliente = rs.getInt("id");
                String cnpj = rs.getString("cnpj");
                String telefone = rs.getString("telefone");
                String nomeCliente = rs.getString("nome");

                // Exibe os dados para confirmação
                System.out.println("Confirmar o agendamento com as seguintes informações:");
                System.out.println("Cliente: " + nomeCliente + " (CPF/CNPJ: " + cnpj + ")");
                System.out.println("Veículo: " + nomeVeiculo + " - Marca: " + marcaVeiculo);
                System.out.println("Data de Entrada: " + dataEntrada);
                System.out.println("Status: " + status);
                System.out.print("Deseja confirmar o agendamento? (S/N): ");
                String confirmacao = scanner.nextLine().trim().toUpperCase();

                if (!confirmacao.equals("S")) {
                    System.out.println("Agendamento cancelado pelo usuário.");
                    return false;
                }

                // Inserindo dados na tabela agendamento
                String sqlInsercao = "INSERT INTO agendamento (id_cliente, cnpj, telefone, nome_cliente, nome_veiculo, marca_veiculo, data_entrada, status, obs_age) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stmtInsercao = conn.prepareStatement(sqlInsercao);
                stmtInsercao.setInt(1, idCliente);
                stmtInsercao.setString(2, cnpj);
                stmtInsercao.setString(3, telefone);
                stmtInsercao.setString(4, nomeCliente);
                stmtInsercao.setString(5, nomeVeiculo);
                stmtInsercao.setString(6, marcaVeiculo);
                stmtInsercao.setDate(7, new java.sql.Date(dataEntrada.getTime()));
                stmtInsercao.setString(8, status);
                stmtInsercao.setString(9, observacao);

                int linhasAfetadas = stmtInsercao.executeUpdate();
                if (linhasAfetadas > 0) {
                    conn.commit();
                    System.out.println("Agendamento realizado com sucesso.");
                    return true;
                } else {
                    System.out.println("Erro ao realizar agendamento.");
                    conn.rollback();
                    return false;
                }
            } else {
                System.out.println("Cliente não encontrado.");
                return false;
            }

        } catch (SQLException | ParseException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmtConsulta != null) stmtConsulta.close();
                if (stmtInsercao != null) stmtInsercao.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
