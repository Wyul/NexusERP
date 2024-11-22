package modelo.dominio.dao.conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import gestao.Fornecedor;

/**
 * Classe responsável por gerenciar o cadastro de fornecedores no sistema.
 * Permite a coleta de informações de fornecedores e a inserção dessas informações no banco de dados.
 */
public class CadastroFornecedor {

    /**
     * Conexão com o banco de dados.
     */
    private Conexao conexao;

    /**
     * Scanner para coleta de entradas do usuário.
     */
    private Scanner scanner;

    /**
     * Construtor da classe que inicializa a conexão e o scanner.
     *
     * @param conexao a instância de conexão com o banco de dados
     */
    public CadastroFornecedor(Conexao conexao) {
        this.conexao = conexao;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Solicita uma entrada do usuário com base em uma mensagem exibida no console.
     * Permite o retorno ao menu principal se o usuário digitar "voltar".
     *
     * @param mensagem a mensagem exibida ao usuário
     * @return a entrada do usuário ou {@code null} se o usuário optar por voltar
     */
    private String solicitarEntrada(String mensagem) {
        System.out.print(mensagem + " (ou digite 'voltar' para retornar): ");
        String entrada = scanner.nextLine();
        if ("voltar".equalsIgnoreCase(entrada)) {
            System.out.println("Retornando ao menu principal...");
            return null;
        }
        return entrada;
    }

    /**
     * Coleta os dados do fornecedor a partir das entradas do usuário e realiza a validação das informações.
     * Permite o cancelamento ou confirmação do cadastro.
     */
    public void coletarDadosFornecedor() {
        while (true) {
            try {
                // Coleta e validação de CNPJ
                String cnpjInput = solicitarEntrada("Digite o CNPJ:");
                if (cnpjInput == null) return;
                cnpjInput = cnpjInput.replaceAll("[^0-9]", "");
                if (cnpjInput.length() != 14) {
                    System.out.println("CNPJ inválido! Deve ter 14 dígitos.");
                    continue;
                }
                long cnpj = Long.parseLong(cnpjInput);

                // Coleta de outros dados do fornecedor
                String razaoSocial = solicitarEntrada("Digite a Razão Social:");
                if (razaoSocial == null) return;

                String inscricaoEstadual = solicitarEntrada("Digite a Inscrição Estadual:");
                if (inscricaoEstadual == null) return;

                String representante = solicitarEntrada("Digite o Representante:");
                if (representante == null) return;

                String telefoneInput = solicitarEntrada("Digite o Telefone:");
                if (telefoneInput == null) return;
                telefoneInput = telefoneInput.replaceAll("[^0-9]", "");
                if (telefoneInput.length() < 10 || telefoneInput.length() > 11) {
                    System.out.println("Número de telefone inválido! Deve ter entre 10 e 11 dígitos.");
                    continue;
                }
                long telefone = Long.parseLong(telefoneInput);

                String email = solicitarEntrada("Digite o Email:");
                if (email == null) return;

                String rua = solicitarEntrada("Digite a Rua:");
                if (rua == null) return;

                String bairro = solicitarEntrada("Digite o Bairro:");
                if (bairro == null) return;

                String cidade = solicitarEntrada("Digite a Cidade:");
                if (cidade == null) return;

                String nome = solicitarEntrada("Digite o Nome do Fornecedor:");
                if (nome == null) return;

                // Criação do objeto Fornecedor
                Fornecedor fornecedor = new Fornecedor(cnpj, razaoSocial, inscricaoEstadual, representante, telefone,
                        email, rua, bairro, cidade, nome);

                // Confirmação do cadastro
                System.out.println("Você deseja: ");
                System.out.println("1. Cadastrar");
                System.out.println("2. Cancelar");
                System.out.print("Escolha uma opção (1 ou 2): ");
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        try (Connection conn = conexao.obterConexao()) {
                            inserirFornecedor(fornecedor, conn);
                            System.out.println("Fornecedor cadastrado com sucesso!");
                        } catch (SQLException e) {
                            System.out.println("Erro ao cadastrar o fornecedor: " + e.getMessage());
                        }
                        return;

                    case 2:
                        System.out.println("Cadastro cancelado.");
                        return;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, tente novamente.");
            }
        }
    }

    /**
     * Insere os dados do fornecedor no banco de dados.
     *
     * @param fornecedor o objeto fornecedor a ser cadastrado
     * @param conn       a conexão com o banco de dados
     * @throws SQLException se ocorrer um erro ao executar a operação no banco de dados
     */
    public void inserirFornecedor(Fornecedor fornecedor, Connection conn) throws SQLException {
        String inserirSQL = "INSERT INTO fornecedor (cnpj, razao_social, ie_for, representante, telefone, email, rua, bairro, cidade, nome) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(inserirSQL)) {
            stmt.setLong(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getRazaoSocial());
            stmt.setString(3, fornecedor.getInscricaoEstadual());
            stmt.setString(4, fornecedor.getRepresentante());
            stmt.setLong(5, fornecedor.getTelefone());
            stmt.setString(6, fornecedor.getEmail());
            stmt.setString(7, fornecedor.getRua());
            stmt.setString(8, fornecedor.getBairro());
            stmt.setString(9, fornecedor.getCidade());
            stmt.setString(10, fornecedor.getNome());

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            System.out.println("Linhas inseridas: " + rowsAffected);
        }
    }
}
