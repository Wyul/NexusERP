package modelo.dominio.dao.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import gestao.Usuario;
import modelo.dominio.dao.conexao.Conexao;

public class UsuarioDAO {

	private final Conexao conexao;

	public UsuarioDAO(Conexao conexao) {
		this.conexao = conexao;
	}

	public String salvar(Usuario usuario) {
		return usuario.getId() == null || usuario.getId() == 0L ? adicionar(usuario) : editar(usuario);
	}

	private String adicionar(Usuario usuario) {
		String sql = "INSERT INTO usuario(nome, usuario, senha, perfil, estado, data_criacao) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = conexao.obterConexao();
				PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

			conn.setAutoCommit(false);
			preencherValoresPreparedStatement(preparedStatement, usuario);
			preparedStatement.setObject(6, LocalDateTime.now()); // Data de criação

			int resultado = preparedStatement.executeUpdate();
			conn.commit();

			return resultado == 1 ? "Usuário adicionado com sucesso" : "Não foi possível adicionar";
		} catch (SQLException e) {
			return tratarErroTransacao(e);
		}
	}

	private String editar(Usuario usuario) {
		String sql = "UPDATE usuario SET nome = ?, usuario = ?, senha = ?, perfil = ?, estado = ? WHERE id = ?";
		try (Connection conn = conexao.obterConexao();
				PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

			preencherValoresPreparedStatement(preparedStatement, usuario);
			preparedStatement.setLong(6, usuario.getId());

			int resultado = preparedStatement.executeUpdate();
			return resultado == 1 ? "Usuário editado com sucesso" : "Não foi possível editar";
		} catch (SQLException e) {
			return String.format("Erro: %s", e.getMessage());
		}
	}

	private void preencherValoresPreparedStatement(PreparedStatement preparedStatement, Usuario usuario)
			throws SQLException {
		preparedStatement.setString(1, usuario.getNome());
		preparedStatement.setString(2, usuario.getUsername());
		preparedStatement.setString(3, usuario.getSenha());

		preparedStatement.setBoolean(5, usuario.isEstado());
	}

	public List<Usuario> buscarTodosUsuarios() {
		String sql = "SELECT * FROM usuario";
		List<Usuario> usuarios = new ArrayList<>();
		try (Connection conn = conexao.obterConexao();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				usuarios.add(getUsuario(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar todos os usuários: " + e.getMessage());
		}
		return usuarios;
	}

	public Usuario buscarUsuarioId(Long id) {
		String sql = "SELECT * FROM usuario WHERE id = ?";
		try (Connection conn = conexao.obterConexao();
				PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

			preparedStatement.setLong(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return getUsuario(resultSet);
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar usuário por ID: " + e.getMessage());
		}
		return null;
	}

	public Usuario buscarUsuarioUser(String username) {
		String sql = "SELECT * FROM usuario WHERE usuario = ?";
		try (Connection conn = conexao.obterConexao();
				PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

			preparedStatement.setString(1, username);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return getUsuario(resultSet);
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar usuário por nome de usuário: " + e.getMessage());
		}
		return null;
	}
	
	public boolean validarCredenciais(String username, String password) {
	    String sql = "SELECT * FROM usuario WHERE usuario = ? AND senha = ?";
	    try (Connection conn = conexao.obterConexao();
	         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, password);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            return resultSet.next(); // Retorna verdadeiro se um usuário for encontrado
	        }
	    } catch (SQLException e) {
	        System.out.println("Erro ao validar credenciais: " + e.getMessage());
	    }
	    return false; // Caso ocorra um erro ou não seja encontrado nenhum usuário
	}


	private Usuario getUsuario(ResultSet resultSet) throws SQLException {
		String perfil = resultSet.getString("perfil");
		if ("ADMIN".equalsIgnoreCase(perfil)) {
			return new Admin(resultSet.getLong("id"), resultSet.getString("nome"), resultSet.getString("usuario"),
					resultSet.getString("senha"), resultSet.getBoolean("estado"),
					resultSet.getObject("data_criacao", LocalDateTime.class));
		} else if ("TECNICO".equalsIgnoreCase(perfil)) {
			return new Tecnico(resultSet.getLong("id"), resultSet.getString("nome"), resultSet.getString("usuario"),
					resultSet.getString("senha"), resultSet.getBoolean("estado"),
					resultSet.getObject("data_criacao", LocalDateTime.class));
		}
		throw new IllegalArgumentException("Perfil desconhecido: " + perfil);
	}

	private String tratarErroTransacao(SQLException e) {
		return String.format("Erro: %s", e.getMessage());
	}
}
