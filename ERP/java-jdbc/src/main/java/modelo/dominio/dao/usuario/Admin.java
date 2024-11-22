package modelo.dominio.dao.usuario;

import java.time.LocalDateTime;

import gestao.Usuario;

public class Admin extends Usuario {

    public Admin(Long id, String nome, String username, String senha, boolean estado, LocalDateTime dataHoraCriacao) {
        super(id, nome, username, senha, estado, dataHoraCriacao);
    }

    @Override
    public void realizarAcao() {
        System.out.println("Ações de administração disponíveis.");
        // Ex.: Visualizar relatórios, gerenciar usuários, etc.
    }
}

