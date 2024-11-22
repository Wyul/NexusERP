package modelo.dominio.dao.usuario;

import java.util.List;
import java.util.Scanner;

import gestao.Usuario;

public class ListarUsuario {
    private final UsuarioDAO usuarioDAO;
    private final Scanner scanner;

    public ListarUsuario(Scanner scanner) {
        this.usuarioDAO = new UsuarioDAO(null);
        this.scanner = scanner;
    }

    public void listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioDAO.buscarTodosUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário encontrado.");
            return;
        }

        System.out.printf("%-5s %-20s %-15s %-10s %-10s%n", "ID", "Nome", "Usuário", "Perfil", "Estado");
        System.out.println("---------------------------------------------------------");
        for (Usuario usuario : usuarios) {
            System.out.printf("%-5d %-20s %-15s %-10s %-10s%n", 
                usuario.getId(), 
                usuario.getNome(), 
                usuario.getUsername(),
                usuario.isEstado() ? "Ativo" : "Inativo");
        }

        System.out.println("---------------------------------------------------------");
    }

    public Scanner getScanner() {
        return scanner;
    }
}
	