package modelo.dominio.dao.usuario;

import java.time.LocalDateTime;

import gestao.Usuario;

public class Tecnico extends Usuario {

    public Tecnico(Long id, String nome, String username, String senha, boolean estado, LocalDateTime dataHoraCriacao) {
        super(id, nome, username, senha, estado, dataHoraCriacao);
    }

    @Override
    public void realizarAcao() {
        System.out.println("Ações técnicas disponíveis.");
        // Ex.: Gerenciar estoque, realizar vendas, etc.
    }
}

